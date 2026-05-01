#! /usr/bin/env bash
# Script con aws cli wrapper para localstack:
awslocal cloudformation deploy \
  --template-file cloudformation/s3-buckets.yaml \
  --stack-name s3-stack \
  --parameter-overrides pEnvironment=dev

# Alternativa con aws cli real:
export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

aws cloudformation deploy \
  --template-file cloudformation/s3-buckets.yaml \
  --stack-name s3-stack \
  --parameter-overrides pEnvironment=dev \
  --endpoint-url=http://localhost:4566

# Listar los buckets creados:
aws s3 ls --endpoint-url=http://localhost:4566
awslocal s3 ls --endpoint-url=http://localhost:4566
