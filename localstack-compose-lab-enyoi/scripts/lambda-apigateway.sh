#! /usr/bin/env bash
# Upload lambda .zip to localstack S3 bucket:
export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

aws s3 cp ./lambda/python/healthcheck.zip s3://lambda-code-dev/lambda-healthcheck.zip \
  --endpoint-url=http://localhost:4566

# List content s3
aws s3 ls s3://lambda-code-dev --endpoint-url=http://localhost:4566

# Deploy lambda function with CloudFormation:
export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

aws cloudformation deploy \
  --template-file cloudformation/lambda-apigateway.yaml \
  --stack-name lambda-apigateway-stack \
  --capabilities CAPABILITY_NAMED_IAM \
  --endpoint-url=http://localhost:4566

# curl a Rest API Gateway endpoint:
# El ID de la URL va cambiando, se puede conseguir en:
# https://app.localstack.cloud/inst/default/resources/cloudformation/stacks/lambda-apigateway-stack/outputs
curl -X GET https://xe6mm0swon.execute-api.localhost.localstack.cloud:4566/dev/health
