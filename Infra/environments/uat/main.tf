terraform {
  backend "s3" {
    bucket = "meuscerts-terraform-state"
    key    = "uat/terraform.tfstate"
    region = "sa-east-1"
  }
}

provider "aws" {
  region = "sa-east-1"
}

module "app" {
  source = "../../modules/app"
  env              = var.env
  r2_endpoint      = var.r2_endpoint
  r2_access_key    = var.r2_access_key
  r2_secret_key    = var.r2_secret_key
  r2_bucket_name   = var.r2_bucket_name
  r2_public_domain = var.r2_public_domain
  app_version = var.app_version
}

variable "env" {}
variable "r2_endpoint" {}
variable "r2_access_key" {}
variable "r2_secret_key" {}
variable "r2_bucket_name" {}
variable "r2_public_domain" {}
variable "app_version" { type = string }

output "url_api" {
  value = module.app.api_url
}