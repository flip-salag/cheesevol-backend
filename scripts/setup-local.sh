#!/usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

ENV_FILE="$ROOT_DIR/.env.local"
ENV_EXAMPLE="$ROOT_DIR/.env.example"

cp "$ENV_EXAMPLE" "$ENV_FILE"

sed -i '' 's/^DB_PORT=.*/DB_PORT=22306/' "$ENV_FILE"
sed -i '' 's/^DB_NAME=.*/DB_NAME=flip/' "$ENV_FILE"
sed -i '' 's/^DB_USERNAME=.*/DB_USERNAME=root/' "$ENV_FILE"
sed -i '' 's/^DB_PASSWORD=.*/DB_PASSWORD=root123/' "$ENV_FILE"

echo 'Local setup completed successfully 🎉'