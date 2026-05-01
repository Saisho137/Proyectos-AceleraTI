#! /usr/bin/env bash
# Script con aws cli wrapper para localstack:
awslocal cloudformation deploy \
  --template-file cloudformation/sqs-queues.yaml \
  --stack-name sqs-stack

# Alternativa con aws cli real:
export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

aws cloudformation deploy \
  --template-file cloudformation/sqs-queues.yaml \
  --stack-name sqs-stack \
  --endpoint-url=http://localhost:4566

# Listar las colas creadas:
aws sqs list-queues --endpoint-url=http://localhost:4566
awslocal sqs list-queues

# Mandar mensaje
aws sqs send-message \
  --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/arka-ordenes-queue \
  --message-body '{"ordenId": "123", "accion": "confirmar"}' \
  --endpoint-url=http://localhost:4566

# Recibir mensaje
aws sqs receive-message \
  --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/arka-ordenes-queue \
  --endpoint-url=http://localhost:4566

# Eliminar mensaje
aws sqs delete-message \
  --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/arka-ordenes-queue \
  --receipt-handle NjQ3MjBhMTMtZTk5ZS00ZThmLWFkMjMtZmI4NjIyNDU4MWE5IGFybjphd3M6c3FzOnVzLWVhc3QtMTowMDAwMDAwMDAwMDA6YXJrYS1vcmRlbmVzLXF1ZXVlIGViNDIzMzg0LTYzMDMtNGZiNy05ZGRkLTBmZmJhZjE5MjRmNiAxNzcwODY0NTg3Ljc3ODAxMDg= \
  --endpoint-url=http://localhost:4566

