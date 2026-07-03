# Architecture

## Core Components

- `config-server`: serves centralized service configuration from the local `config-repo`
- `discovery-server`: Eureka registry for service discovery
- `api-gateway`: single ingress for frontend and service routing
- `user-service`: authentication, user profiles, addresses, refresh tokens
- `product-service`: catalog, brands, categories, products
- `inventory-service`: warehouses, stock, reservations, inventory movements
- `order-service`: carts, wishlist, checkout, orders, tracking
- `payment-service`: payment processing and refunds
- `notification-service`: notification persistence and RabbitMQ integration
- `frontend`: React + Vite storefront shell proxied through the API gateway

## Data Model Strategy

- One MySQL schema per service
- Flyway-managed migrations per service
- Seed data for demo users, catalog entries, and inventory levels
- No cross-schema foreign keys between microservices

## Runtime Flow

1. Frontend calls `/auth/*` and `/api/*`
2. Nginx or Vite proxies requests to `api-gateway`
3. Gateway routes by path to the matching service
4. Services register with Eureka and can resolve each other dynamically
5. Config Server supplies docker-time datasource and shared JWT settings
