terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

resource "aws_dynamodb_table" "tabela_certs" {
  name         = "meuscerts-${var.env}"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id"

  attribute {
    name = "id"
    type = "S"
  }

  attribute {
    name = "categoria"
    type = "S"
  }

  global_secondary_index {
    name               = "CategoriaIndex"
    hash_key           = "categoria"
    projection_type    = "ALL"
  }
}

resource "aws_iam_role" "lambda_role" {
  name = "meuscerts-role-${var.env}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "lambda.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_iam_role_policy" "dynamo_access" {
  name = "dynamo-access"
  role = aws_iam_role.lambda_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action   = ["dynamodb:*"]
      Effect   = "Allow"
      Resource = aws_dynamodb_table.tabela_certs.arn
    }]
  })
}

resource "aws_cloudwatch_log_group" "lambda_log_group" {
  name              = "/aws/lambda/${aws_lambda_function.api_lambda.function_name}"
  retention_in_days = 3
  lifecycle {
    prevent_destroy = false
  }
}

resource "aws_lambda_function" "api_lambda" {
  function_name = "meuscerts-api-${var.env}"
  role          = aws_iam_role.lambda_role.arn
  handler       = "org.springframework.cloud.function.adapter.aws.FunctionInvoker"
  runtime       = "java21"
  memory_size   = 512
  timeout       = 30

  filename      = "../../../target/MeusCerts-0.0.1-SNAPSHOT.jar"
  source_code_hash = filebase64sha256("../../../target/MeusCerts-0.0.1-SNAPSHOT.jar")
  publish = true

  snap_start {
    apply_on = "PublishedVersions"
  }

  environment {
    variables = {
      DYNAMODB_TABLE_NAME         = aws_dynamodb_table.tabela_certs.name
      CLOUDFLARE_R2_ENDPOINT      = var.r2_endpoint
      CLOUDFLARE_R2_ACCESS_KEY    = var.r2_access_key
      CLOUDFLARE_R2_SECRET_KEY    = var.r2_secret_key
      CLOUDFLARE_R2_BUCKET_NAME   = var.r2_bucket_name
      CLOUDFLARE_R2_PUBLIC_DOMAIN = var.r2_public_domain
      SPRING_CLOUD_FUNCTION_DEFINITION = "processarCertificado"
    }
  }
}

resource "aws_lambda_alias" "snapstart_alias" {
  name             = "snapstart"
  description      = "Alias pointing to the latest published version with SnapStart"
  function_name    = aws_lambda_function.api_lambda.function_name
  function_version = aws_lambda_function.api_lambda.version
}

resource "aws_apigatewayv2_api" "gateway" {
  name          = "meuscerts-gateway-${var.env}"
  protocol_type = "HTTP"
  cors_configuration {
    allow_origins = ["*"]
    allow_methods = ["POST", "GET", "OPTIONS"]
    allow_headers = ["content-type", "authorization"]
    max_age       = 300
  }
}

resource "aws_apigatewayv2_stage" "gateway_stage" {
  api_id      = aws_apigatewayv2_api.gateway.id
  name        = "$default"
  auto_deploy = true
}

resource "aws_apigatewayv2_integration" "lambda_integration" {
  api_id           = aws_apigatewayv2_api.gateway.id
  integration_type = "AWS_PROXY"
  integration_uri    = aws_lambda_alias.snapstart_alias.invoke_arn
  integration_method = "POST"
  payload_format_version = "2.0"
}

resource "aws_apigatewayv2_route" "default_route" {
  api_id    = aws_apigatewayv2_api.gateway.id
  route_key = "$default"
  target    = "integrations/${aws_apigatewayv2_integration.lambda_integration.id}"
}

resource "aws_lambda_permission" "api_gw" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.api_lambda.function_name
  principal     = "apigateway.amazonaws.com"
  qualifier     = aws_lambda_alias.snapstart_alias.name
  source_arn    = "${aws_apigatewayv2_api.gateway.execution_arn}/*/*"
}