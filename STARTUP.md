# Startup Order

## Docker Compose Order

1. MySQL containers
2. Redis and RabbitMQ
3. Eureka discovery server
4. Spring Cloud Config server
5. API gateway
6. Domain services
7. Frontend

The Compose file uses health checks and `depends_on` conditions so later services wait for earlier ones to be healthy.

## Helper Scripts

- `start-all.bat`
- `stop-all.bat`
- `start-all.sh`
- `stop-all.sh`

The start scripts:

1. Build and start the full stack with Docker Compose
2. Poll core health endpoints
3. Open the storefront in the default browser

They also run the required Maven and frontend builds before `docker compose up -d --build`.
