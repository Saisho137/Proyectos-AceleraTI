# lambda/healthcheck.py
import json
from datetime import datetime

def handler(event, context):
    print(event)
    print(context)
    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "body": json.dumps({
            "status": "healthy",
            "timestamp": datetime.now().isoformat(),
            "service": "arka-api"
        })
    }