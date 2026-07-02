# PROJECT_RULES.md

## Tech Stack

| Layer          | Technology                          |
|----------------|-------------------------------------|
| Backend        | Spring Boot 3.2.0, Java 21          |
| Frontend       | React 18 + Vite (not yet built)     |
| Database       | MySQL 8.0 (schema-per-service)      |
| Authentication | JWT (jjwt 0.12.3) + Refresh Tokens  |
| Service Mesh   | Spring Cloud 2023.0.0               |
| Discovery      | Netflix Eureka                      |
| Config         | Spring Cloud Config Server          |
| Gateway        | Spring Cloud Gateway                |
| Messaging      | RabbitMQ (spring-boot-starter-amqp) |
| Cache          | Redis (@EnableCaching, Spring Cache)|
| Migrations     | Flyway                              |
| Mapping        | MapStruct 1.5.5                     |
| Docs           | SpringDoc OpenAPI 2.1.0 (Swagger)   |
| Build          | Maven 3.9+ (multi-module)           |

---

## Module List

| Module               | Artifact ID            | Port |
|----------------------|------------------------|------|
| Parent POM           | enterprise-ecommerce-parent | —  |
| Common Library       | common-library         | —    |
| Discovery Server     | discovery-server       | 8761 |
| Config Server        | config-server          | 8888 |
| API Gateway          | api-gateway            | 8080 |
| User Service         | user-service           | 8081 |
| Product Service      | product-service        | 8082 |
| Inventory Service    | inventory-service      | 8083 |
| Order Service        | order-service          | 8084 |
| Payment Service      | payment-service        | 8085 |
| Notification Service | notification-service   | 8086 |

---

## Package Naming Convention

```
com.ecommerce.<service>.<layer>
```

Examples:
- `com.ecommerce.user.controller`
- `com.ecommerce.product.service.impl`
- `com.ecommerce.common.exception`

---

## Layer Architecture (per service)

```
controller/       REST endpoints, no business logic
service/          Interfaces only
service/impl/     Business logic implementations
repository/       Spring Data JPA interfaces
entity/           JPA entities (extend BaseEntity)
dto/              Request/Response DTOs (Jakarta Validation)
mapper/           MapStruct interfaces (entity <-> DTO)
exception/        Service-specific exceptions (extend BaseException)
config/           Spring @Configuration classes
```

---

## Coding Standards

- **Injection**: Constructor injection only. Never `@Autowired` field injection.
- **Lombok**: Use `@RequiredArgsConstructor` for constructor injection in services/controllers.
- **Transactions**: `@Transactional` on service impl methods. Read-only queries use `@Transactional(readOnly = true)`.
- **Logging**: `@Slf4j` on every class that logs. Use `log.info/debug/warn/error`.
- **IDs**: All entity IDs are `String` (UUID). Use `UUIDUtil.generateUUID()` to generate.
- **Base class**: All entities (except audit-only ones) extend `com.ecommerce.common.entity.BaseEntity`.
- **Exceptions**: Extend `BaseException`. Never throw raw `RuntimeException` in service layer.
- **Validation**: Jakarta Validation annotations on DTOs. `@Valid` on controller `@RequestBody`.
- **Response format**: Wrap all responses in `ResponseWrapper<T>` from common-library.
- **Pagination**: Return `PaginationResponse<T>` from common-library for paginated endpoints.
- **Error handling**: `GlobalExceptionHandler` in common-library handles all exceptions globally.
- **No TODO comments** in committed code.
- **No magic strings**: Use constants from `AppConstants` and `ErrorCodes`.
- **REST naming**: Plural nouns for collections (`/api/products`), kebab-case for multi-word paths.
- **HTTP methods**: GET=read, POST=create, PUT=full update, PATCH=partial update, DELETE=remove.
- **Security**: Each service has its own `SecurityConfig`. Product/Category/Brand GET endpoints are public.

---

## Common Library — What It Provides

| Class                  | Purpose                                      |
|------------------------|----------------------------------------------|
| `BaseEntity`           | `createdAt`, `updatedAt`, `createdBy`, `updatedBy` |
| `BaseException`        | Root exception with `errorCode` + `httpStatus` |
| `ResourceNotFoundException` | 404 wrapper                             |
| `ValidationException`  | 400 wrapper                                  |
| `ServiceException`     | 500 wrapper                                  |
| `GlobalExceptionHandler` | `@RestControllerAdvice` for all services   |
| `JwtUtil`              | Generate/validate/parse JWT tokens           |
| `ResponseWrapper<T>`   | Standard API response envelope               |
| `PaginationResponse<T>`| Standard paginated response                  |
| `ErrorResponse`        | Standard error body                          |
| `AppConstants`         | Auth headers, page sizes, date formats, Role enum |
| `ErrorCodes`           | String constants for error codes             |
| `UUIDUtil`             | `generateUUID()`, `isValidUUID()`            |
| `ValidationUtil`       | Email, phone, password regex validators      |
| `BaseRepository<T,ID>` | Extends JpaRepository (no-op, for consistency) |
| `BaseService<T,ID>`    | Generic CRUD interface (not enforced)        |

---

## Security Flow

```
Client → API Gateway (port 8080)
       → JWT validated at gateway OR per-service SecurityConfig
       → Service receives X-User-Id / X-User-Roles headers (future)
```

- JWT secret: `${JWT_SECRET}` env var (default: `your-secret-key-change-in-production`)
- Access token expiry: 1 hour (3600000 ms)
- Refresh token expiry: 7 days (604800000 ms)
- Refresh tokens are hashed (SHA-256) before storage
- Roles: `CUSTOMER`, `ADMINISTRATOR`, `VENDOR`, `WAREHOUSE_MANAGER`, `DELIVERY_EXECUTIVE`

---

## Database Rules

- One MySQL database per service (schema-per-service pattern)
- All tables use `utf8mb4` / `utf8mb4_unicode_ci`
- All PKs are `VARCHAR(36)` (UUID)
- Flyway manages all schema changes (`db/migration/V{n}__description.sql`)
- `ddl-auto: validate` in all services (never `create` or `update` in production)
- Cross-service references are by ID only — no cross-database foreign keys in JPA

---

## API Gateway Routes

| Path Pattern                                    | Upstream Service     |
|-------------------------------------------------|----------------------|
| `/api/users/**`, `/auth/**`                     | user-service         |
| `/api/products/**`, `/api/categories/**`, `/api/brands/**` | product-service |
| `/api/inventory/**`                             | inventory-service    |
| `/api/orders/**`, `/api/cart/**`, `/api/wishlist/**` | order-service   |
| `/api/payments/**`                              | payment-service      |
| `/api/notifications/**`                         | notification-service |

Discovery locator is also enabled (`spring.cloud.gateway.discovery.locator.enabled: true`).

---

## Environment Variables

```
DB_HOST          localhost
DB_PORT          3306
DB_USERNAME      root
DB_PASSWORD      rootpassword
JWT_SECRET       your-secret-key-change-in-production
EUREKA_PASSWORD  admin
CONFIG_SERVER_PASSWORD  admin
```

---

## What NOT to Do

- Never use `@Autowired` field injection.
- Never use `ddl-auto: create` or `ddl-auto: update`.
- Never add cross-service JPA relationships (`@ManyToOne` across databases).
- Never put business logic in controllers.
- Never return raw entities from controllers — always use DTOs.
- Never regenerate unchanged files.
- Never rewrite existing code unless the task requires it.
- Never use `List<Object>` as a return type — define proper DTOs.
- Never hardcode secrets or credentials in source files.
