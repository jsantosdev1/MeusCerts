output "api_url" {
  description = "URL base da API Gateway"
  value       = aws_apigatewayv2_api.gateway.api_endpoint
}