#!/usr/bin/env bash
set -euo pipefail

wait_for_url() {
  local url="$1"
  local label="$2"
  echo "Waiting for ${label}..."
  until curl -fsS "$url" >/dev/null; do
    sleep 5
  done
}

mvn clean install
(cd frontend && npm install && npm run build)
docker compose up -d --build

wait_for_url "http://admin:admin@localhost:8761/actuator/health" "Eureka"
wait_for_url "http://admin:admin@localhost:8888/actuator/health" "Config Server"
wait_for_url "http://localhost:8080/actuator/health" "API Gateway"
wait_for_url "http://localhost:4200" "Frontend"

if command -v xdg-open >/dev/null 2>&1; then
  xdg-open "http://localhost:4200" >/dev/null 2>&1 || true
elif command -v open >/dev/null 2>&1; then
  open "http://localhost:4200" >/dev/null 2>&1 || true
fi

echo "Platform is ready at http://localhost:4200"
