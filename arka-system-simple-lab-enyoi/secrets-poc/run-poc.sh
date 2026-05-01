#!/usr/bin/env bash
set -euo pipefail

IMAGE_NAME="arka-secrets-poc"
CONTAINER_NAME="arka-secrets-poc"
NETWORK_NAME="arka-system-simple-lab_arka-network"
PORT=8900

echo "══════════════════════════════════════════="
echo "  Arka Lab — Secrets POC"
echo "══════════════════════════════════════════="

# 1. Construir la imagen
echo ""
echo "Construyendo imagen Docker..."
docker build --file deployment/Dockerfile -t "$IMAGE_NAME" .

# 2. Eliminar contenedor previo si existe
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "Eliminando contenedor previo..."
  docker rm -f "$CONTAINER_NAME"
fi

# 3. Ejecutar en la misma red de la infraestructura
echo ""
echo "══════════════════════════════════════════="
echo "Levantando contenedor '$CONTAINER_NAME'"
echo "   Puerto: http://localhost:$PORT"
echo "   Logs:   docker logs -f $CONTAINER_NAME"
echo "══════════════════════════════════════════="
echo ""

echo "Levantando contenedor en red '$NETWORK_NAME'..."
docker run -d \
  --name "$CONTAINER_NAME" \
  --network "$NETWORK_NAME" \
  -p "$PORT:$PORT" \
  -e AWS_ACCESS_KEY_ID="test" \
  -e AWS_SECRET_ACCESS_KEY="test" \
  -e AWS_REGION="us-east-1" \
  -e LOCALSTACK_PORT="4566" \
  -e LOCALSTACK_HOST="arka-localstack" \
  "$IMAGE_NAME"