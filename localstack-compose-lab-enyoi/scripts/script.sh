#! /usr/bin/env bash

echo "Building Infra"

export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

#pwd
cd /etc/localstack/init/ready.d

aws cloudformation deploy \
  --template-file cloudformation/s3-buckets.yaml \
  --stack-name s3-stack \
  --parameter-overrides pEnvironment=dev \
  --endpoint-url=http://localhost:4566

cd lambda_python zip healthcheck.zip healthcheck.py && cd ..

aws s3 cp ./lambda_python/healthcheck.zip s3://lambda-code-dev/lambda-healthcheck.zip \
  --endpoint-url=http://localhost:4566

aws cloudformation deploy \
  --template-file cloudformation/sqs-queues.yaml \
  --stack-name sqs-stack \
  --endpoint-url=http://localhost:4566

aws cloudformation deploy \
  --template-file cloudformation/secrets-manager.yaml \
  --stack-name secrets-stack \
  --endpoint-url=http://localhost:4566

aws cloudformation deploy \
  --template-file cloudformation/lambda-apigateway.yaml \
  --stack-name lambda-apigateway-stack \
  --capabilities CAPABILITY_NAMED_IAM \
  --endpoint-url=http://localhost:4566

echo "Infra built successfully"