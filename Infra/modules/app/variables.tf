variable "env" {
  description = "Nome do ambiente (uat ou prod)"
}
variable "aws_region" {
  default = "sa-east-1"
}

variable "r2_endpoint" {}
variable "r2_access_key" {}
variable "r2_secret_key" {}
variable "r2_bucket_name" {}
variable "r2_public_domain" {}