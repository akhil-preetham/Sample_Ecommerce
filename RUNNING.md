# Running Guide

## Recommended: Docker Compose

```bash
mvn clean install
cd frontend && npm install && npm run build
docker compose up -d --build
docker compose ps
```

Open:

- Frontend: [http://localhost:4200](http://localhost:4200)
- Eureka: [http://localhost:8761](http://localhost:8761)
- Config Server: [http://localhost:8888](http://localhost:8888)

## Local Development Mode

1. Start infrastructure:

```bash
docker compose up -d mysql-user-db mysql-product-db mysql-inventory-db mysql-order-db mysql-payment-db mysql-notification-db redis rabbitmq
```

2. Start discovery server:

```bash
mvn -pl discovery-server spring-boot:run
```

3. Start config server:

```bash
mvn -pl config-server spring-boot:run
```

4. Start gateway and services in separate terminals:

```bash
mvn -pl api-gateway spring-boot:run
mvn -pl user-service spring-boot:run
mvn -pl product-service spring-boot:run
mvn -pl inventory-service spring-boot:run
mvn -pl order-service spring-boot:run
mvn -pl payment-service spring-boot:run
mvn -pl notification-service spring-boot:run
```

5. Start the frontend:

```bash
cd frontend
npm install
npm run dev
```
