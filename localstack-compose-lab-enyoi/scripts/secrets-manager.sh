#! /usr/bin/env bash
# Script con aws cli wrapper para localstack:
awslocal cloudformation deploy \
  --template-file cloudformation/secrets-manager.yaml \
  --stack-name secrets-stack

# Alternativa con aws cli real:
export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

aws cloudformation deploy \
  --template-file cloudformation/secrets-manager.yaml \
  --stack-name secrets-stack \
  --endpoint-url=http://localhost:4566

# Listar los secretos creados:
aws secretsmanager list-secrets --endpoint-url=http://localhost:4566
# Listar secretos cifrados
aws secretsmanager get-secret-value \
  --secret-id arka/api/keys \
  --endpoint-url=http://localhost:4566
