# Install Guide

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 22+
- Docker Desktop with Docker Compose

## Environment

The root `.env` file is used by Docker Compose:

```env
MYSQL_ROOT_PASSWORD=rootpassword
RABBITMQ_DEFAULT_USER=admin
RABBITMQ_DEFAULT_PASS=admin
JWT_SECRET=yourSuperSecretKeyThatIsAtLeast32CharactersLongAndShouldBeChangedInProduction
```

## Backend Build

```bash
mvn clean install
```

## Frontend Install

```bash
cd frontend
npm install
```

## Optional Local Service Ports

- User DB: `3312`
- Product DB: `3307`
- Inventory DB: `3308`
- Order DB: `3309`
- Payment DB: `3310`
- Notification DB: `3311`
- RabbitMQ: `5672`
- Redis: `6379`
