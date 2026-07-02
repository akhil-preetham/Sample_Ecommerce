# ARCHITECTURE.md

## System Overview

Enterprise-grade microservices e-commerce platform.
Spring Boot 3.2 backend, React 18 frontend (not yet built), MySQL per service, Eureka discovery, Spring Cloud Config, RabbitMQ messaging, Redis cache.

---

## Repository Structure

```
enterprise-ecommerce/
├── pom.xml                        ← Parent POM (multi-module)
├── docker-compose.yml             ← Infrastructure only (DBs, Redis, RabbitMQ)
├── PROJECT_RULES.md               ← Coding conventions (source of truth)
├── ARCHITECTURE.md                ← This file
├── MODULE_INDEX.md                ← Module-to-file quick index
│
├── common-library/                ← Shared utilities, DTOs, exceptions, JwtUtil
├── discovery-server/              ← Eureka Server (port 8761)
├── config-server/                 ← Spring Cloud Config (port 8888)
│   └── config-repo/               ← Per-service YAML configs
├── api-gateway/                   ← Spring Cloud Gateway (port 8080)
│
├── user-service/                  ← Auth + user profiles (port 8081)
├── product-service/               ← Catalog: products, categories, brands (port 8082)
├── inventory-service/             ← Stock, warehouses, reservations (port 8083)
├── order-service/                 ← Cart, wishlist, orders (port 8084)
├── payment-service/               ← Payments, refunds (port 8085)
├── notification-service/          ← Email templates, notifications (port 8086)
│
├── frontend/                      ← React 18 + Vite (not yet implemented)
├── database/                      ← (empty — schemas managed by Flyway)
├── docs/                          ← (empty)
└── scripts/                       ← (empty)
```

---

## Maven Parent Structure

```
enterprise-ecommerce-parent (pom.xml)
  ├── parent: spring-boot-starter-parent 3.2.0
  ├── java.version: 21
  ├── spring-cloud.version: 2023.0.0
  │
  ├── dependencyManagement:
  │     spring-cloud-dependencies (BOM)
  │     jjwt-api / jjwt-impl / jjwt-jackson  0.12.3
  │     mapstruct  1.5.5.Final
  │     springdoc-openapi-starter-webmvc-ui  2.1.0
  │
  ├── global dependencies (all modules inherit):
  │     spring-boot-starter-web
  │     spring-boot-starter-data-jpa
  │     spring-boot-starter-security
  │     spring-boot-starter-validation
  │     spring-boot-starter-actuator
  │     mysql-connector-java  8.0.33
  │     flyway-mysql  9.22.3
  │     lombok  1.18.30
  │     jackson-datatype-jsr310
  │     spring-boot-starter-test (test)
  │     spring-security-test (test)
  │     junit-jupiter-api (test)
  │     mockito-core (test)
  │
  └── build plugins:
        spring-boot-maven-plugin (lombok excluded from fat jar)
        maven-compiler-plugin 3.11.0
          annotationProcessorPaths: lombok + mapstruct-processor
```

**Note:** `discovery-server` and `config-server` do NOT inherit web/jpa/security from parent — they only declare what they need. `api-gateway` uses WebFlux (reactive), not WebMVC.

---

## Service-by-Service Architecture

### common-library
No Spring Boot app. Pure library jar.
```
config/     JwtUtil           — JWT generate/validate/parse
constant/   AppConstants      — headers, page sizes, Role enum
            ErrorCodes        — error code string constants
dto/        ResponseWrapper   — standard API envelope
            PaginationResponse — paginated list wrapper
            ErrorResponse     — error body
entity/     BaseEntity        — createdAt, updatedAt, createdBy, updatedBy
exception/  BaseException     — root (errorCode + httpStatus)
            ResourceNotFoundException (404)
            ValidationException (400)
            ServiceException (500)
            GlobalExceptionHandler — @RestControllerAdvice
repository/ BaseRepository    — extends JpaRepository (marker)
service/    BaseService       — generic CRUD interface (not enforced)
util/       UUIDUtil          — generateUUID(), isValidUUID()
            ValidationUtil    — email/phone/password regex
resources/  logback-spring.xml — async file + console appenders
```

---

### discovery-server (port 8761)
```
DiscoveryServerApplication  @EnableEurekaServer
application.yml             self-preservation OFF, basic auth admin/admin
```
No custom Java classes beyond the main application.

---

### config-server (port 8888)
```
ConfigServerApplication     @EnableConfigServer
application.yml             git uri = file://${user.home}/config-repo
config-repo/
  application.yml           — global: JPA, Jackson, JWT, logging, actuator
  user-service.yml
  product-service.yml
  inventory-service.yml
  order-service.yml
  payment-service.yml
  notification-service.yml
```
No custom Java classes beyond the main application.

---

### api-gateway (port 8080)
```
ApiGatewayApplication       @EnableDiscoveryClient
                            RouteLocator bean — 6 service routes
config/                     (empty — no custom config yet)
filter/                     (empty — no custom filters yet)
exception/                  (empty — no custom error handling yet)
application.yml             discovery locator enabled, DEBUG gateway logging
```
Depends on: `common-library` (for JwtUtil), `spring-cloud-starter-gateway`, `spring-cloud-starter-circuitbreaker-resilience4j`.

---

### user-service (port 8081)
```
UserServiceApplication      @EnableDiscoveryClient, @ComponentScan(user + common)
config/
  SecurityConfig            BCryptPasswordEncoder(strength=12) bean only
controller/
  AuthController            POST /auth/register, /login, /refresh, /logout,
                            /forgot-password, /change-password
                            GET  /auth/validate-email
  UserController            GET/PUT /api/users/{id}/profile
                            GET/POST /api/users/{id}/addresses
                            GET/PUT/DELETE /api/users/{id}/addresses/{addrId}
                            PUT /api/users/{id}/addresses/{addrId}/set-default
                            GET /api/users/{id}/addresses/default
dto/
  LoginRequest              @Email, @NotBlank
  RegisterRequest           @Email, @NotBlank, @Size(min=8) password
  LoginResponse             userId, email, firstName, lastName, accessToken,
                            refreshToken, roles, accessTokenExpiration
  UserProfileDTO            id, email, firstName, lastName, phone,
                            emailVerified, isActive
  AddressDTO                @NotBlank fields: addressType, street, city,
                            state, zipCode, country; optional: isDefault,
                            recipientName, phone
entity/
  User                      extends BaseEntity; id(UUID), email, passwordHash,
                            firstName, lastName, phone, isActive, emailVerified
                            @OneToMany(EAGER) roles
  UserRole                  id(Long/IDENTITY), user(ManyToOne LAZY), role(String)
  UserAddress               extends BaseEntity; id(UUID), userId, addressType,
                            street, city, state, zipCode, country, isDefault,
                            recipientName, phone
  RefreshToken              id(UUID), userId, token(TEXT), tokenHash(VARCHAR64),
                            expiryDate, isRevoked, createdAt
repository/
  UserRepository            extends BaseRepository; findByEmail, existsByEmail,
                            findByIdAndIsActiveTrue
  UserAddressRepository     extends BaseRepository; findByUserId(Page),
                            findByIdAndUserId, findByUserIdAndIsDefaultTrue
  RefreshTokenRepository    extends JpaRepository; findByTokenHash,
                            deleteByUserId, deleteByIsRevokedTrue
  UserRoleRepository        extends JpaRepository; findByUserId, deleteByUserId
service/
  AuthenticationService     register, login, refreshToken, logout,
                            forgotPassword, resetPassword, changePassword,
                            validateEmail
  UserService               getUserProfile, updateUserProfile, findUserById,
                            findUserByEmail, existsByEmail, createUser,
                            deleteUser, deactivateUser
  AddressService            createAddress, updateAddress, deleteAddress,
                            getAddress, getUserAddresses, setDefaultAddress,
                            getDefaultAddress
mapper/                     (empty — manual mapping in service impls)
validation/                 (empty)
exception/                  (empty — uses common-library exceptions)
db/migration/
  V1__create_user_tables.sql  users, user_roles, user_addresses, refresh_tokens
```

**Known issues:**
- `AuthController.extractUserIdFromToken()` extracts the raw token string, not the userId — it strips "Bearer " but returns the JWT, not the parsed subject. This is a bug.
- `forgotPassword` and `resetPassword` are stub implementations (log only, no email sent).
- `User.roles` is `FetchType.EAGER` — causes N+1 on every user load.
- No Spring Security filter chain — all endpoints are effectively unprotected at the service level.

---

### product-service (port 8082)
```
ProductServiceApplication   @EnableDiscoveryClient, @ComponentScan(product + common)
config/
  SecurityConfig            Full SecurityFilterChain; public: GET /api/products/**,
                            /api/categories/**, /api/brands/**, Swagger;
                            authenticated: everything else;
                            oauth2ResourceServer JWT with roles claim
  CacheConfig               @EnableCaching (uses default Spring Cache)
controller/
  ProductController         GET /api/products (filter by category/brand),
                            GET /api/products/{id},
                            GET /api/products/search?q=,
                            GET /api/products/suggestions?q=,
                            POST/PUT/DELETE /api/products (ADMINISTRATOR only)
  CategoryController        GET /api/categories, GET /api/categories/{id},
                            POST/PUT/DELETE /api/categories (ADMINISTRATOR only)
  BrandController           GET /api/brands, GET /api/brands/{id},
                            POST/PUT/DELETE /api/brands (ADMINISTRATOR only)
dto/
  ProductDTO                id, name(@NotBlank,@Size 3-255), description,
                            categoryId(@NotBlank), brandId(@NotBlank),
                            basePrice(@NotNull,@DecimalMin>0), rating(0-5),
                            reviewCount(@Min 0), isActive, sku(@Size max 100),
                            stock(@Min 0), variants(List), images(List)
  CategoryDTO               id, name(@NotBlank,@Size 2-255), description, isActive
  BrandDTO                  id, name(@NotBlank,@Size 2-255), description, isActive
  ProductVariantDTO         id, productId(@NotBlank), variantName(@NotBlank,@Size 2-255),
                            price(@NotNull,@DecimalMin>0), sku, stock(@NotNull,@Min 0)
  ProductImageDTO           id, productId(@NotBlank), imageUrl(@NotBlank,@Size max 2000),
                            altText(@Size max 255), isDefault, displayOrder(@Min 0)
entity/
  Product                   extends BaseEntity; id, name, description, categoryId,
                            brandId, basePrice(BigDecimal), rating, reviewCount,
                            isActive, sku(unique), stock
  Category                  extends BaseEntity; id, name(unique), description, isActive
  Brand                     extends BaseEntity; id, name(unique), description, isActive
  ProductVariant            extends BaseEntity; id, productId, variantName,
                            price(BigDecimal), sku(unique), stock
  ProductImage              extends BaseEntity; id, productId, imageUrl(TEXT),
                            altText, isDefault, displayOrder
mapper/
  ProductMapper             MapStruct: toDTO(Product), toEntity(ProductDTO)
  CategoryMapper            MapStruct: toDTO(Category), toEntity(CategoryDTO)
  BrandMapper               MapStruct: toDTO(Brand), toEntity(BrandDTO)
  ProductVariantMapper      MapStruct: toDTO/toEntity
  ProductImageMapper        MapStruct: toDTO/toEntity
repository/
  ProductRepository         extends BaseRepository; findByIsActiveTrue(Page),
                            findByIsActiveTrueAndCategoryId, findByIsActiveTrueAndBrandId,
                            findByIsActiveTrueAndNameContainingIgnoreCase,
                            findByIsActiveTrueAndCategoryIdAndBrandId,
                            countByIsActiveTrue
  CategoryRepository        extends BaseRepository; findByName, findByIsActiveTrue(List),
                            findByIsActiveTrue(Page); @Cacheable on list method
  BrandRepository           extends BaseRepository; findByName, findByIsActiveTrue(List),
                            findByIsActiveTrue(Page); @Cacheable on list method
  ProductVariantRepository  extends JpaRepository; findByProductId
  ProductImageRepository    extends JpaRepository; findByProductIdOrderByDisplayOrder
service/
  ProductService            createProduct, getProductById, getAllProducts,
                            searchProducts, getProductsByCategory, getProductsByBrand,
                            getProductsByCategoryAndBrand, updateProduct,
                            deleteProduct, getTotalProductCount
  CategoryService           createCategory, getCategoryById, getAllCategories,
                            updateCategory, deleteCategory
  BrandService              createBrand, getBrandById, getAllBrands,
                            updateBrand, deleteBrand
db/migration/
  V1__create_product_tables.sql  categories, brands, products, product_variants,
                                 product_images; FULLTEXT index on name+description
```

**Known issues:**
- `Product.stock` duplicates inventory data — stock is managed by inventory-service.
- `@Cacheable` on repository method (`CategoryRepository`, `BrandRepository`) is an anti-pattern.
- `CacheConfig` is empty — no Redis `CacheManager` configured, falls back to in-memory `ConcurrentHashMap`.
- `minPrice`/`maxPrice` params accepted in `ProductController` but not passed to service.

---

### inventory-service (port 8083)
```
InventoryServiceApplication @EnableDiscoveryClient, @EnableFeignClients,
                            @EnableRetry, @EnableMethodSecurity,
                            @ComponentScan(inventory + common)
client/
  ProductServiceClient      Feign: GET /api/products/variants/{variantId}
  ProductServiceClientFallback  returns stub ProductVariantResponse on failure
  ProductVariantResponse    id, productId, sku, name, isActive
controller/
  InventoryController       POST /api/inventory/check-availability,
                            POST /api/inventory/reserve,
                            POST /api/inventory/release,
                            GET  /api/inventory/status/{variantId},
                            GET  /api/inventory/low-stock (ADMIN),
                            GET  /api/inventory/movements/{itemId}
  WarehouseController       GET/POST /api/warehouses,
                            GET/PUT/DELETE /api/warehouses/{id}
dto/
  CheckAvailabilityRequest  productVariantId(@NotNull), quantity(@NotNull,@Positive),
                            warehouseId(optional)
  ReserveStockRequest       orderId(@NotNull), inventoryItemId(@NotNull),
                            quantity(@NotNull,@Positive)
  ReleaseStockRequest       orderId(@NotNull), inventoryItemId(@NotNull),
                            quantity(@NotNull,@Positive)
  InventoryItemDTO          id, productVariantId, warehouseId, availableStock,
                            reservedStock, reorderLevel, totalStock,
                            createdAt, updatedAt, version
  StockStatusResponse       inventoryItemId, productVariantId, warehouseId,
                            availableStock, reservedStock, totalStock,
                            reorderLevel, lowStock, lastUpdated
  WarehouseDTO              id, name(@NotBlank), location(@NotBlank),
                            isActive, createdAt, updatedAt
entity/
  InventoryItem             extends BaseEntity; id, productVariantId, warehouseId,
                            availableStock, reservedStock, reorderLevel,
                            @Version version (optimistic locking)
  StockReservation          extends BaseEntity; id, orderId, inventoryItemId,
                            quantity, status
  StockMovement             id, inventoryItemId, movementType, quantity,
                            reference(TEXT), reason, createdAt (@PrePersist)
  Warehouse                 extends BaseEntity; id, name(unique), location, isActive
repository/
  InventoryItemRepository   extends BaseRepository; findByProductVariantIdAndWarehouseId,
                            findByProductVariantId, findByAvailableStockLessThanEqual,
                            findByProductVariantIdAndReservedStockGreaterThan
  StockReservationRepository extends BaseRepository; findByOrderId,
                            findByOrderIdAndStatus, findByStatus
  StockMovementRepository   extends JpaRepository; findByInventoryItemId,
                            findByMovementType
  WarehouseRepository       extends BaseRepository
service/
  InventoryService          checkAvailability, reserveStock, releaseStock,
                            getInventoryStatus, getInventoryItemById
  ReservationService        createReservation, releaseReservation,
                            confirmReservation, cancelReservation,
                            getReservationsByOrder
  StockMovementService      logMovement, getMovementHistory
  WarehouseService          createWarehouse, updateWarehouse, deleteWarehouse,
                            getWarehouseById, getAllWarehouses
  ReportingService          getLowStockItems, getLowStockItemsByWarehouse,
                            checkAndAlertLowStock
exception/
  InsufficientStockException  extends BaseException (400)
  ItemNotFoundException       extends BaseException (404)
db/migration/
  V1__create_inventory_tables.sql  warehouses, inventory_items, stock_movements,
                                   stock_reservations; default warehouse seed
  V2__add_optimistic_locking.sql   ALTER TABLE inventory_items ADD COLUMN version
application.yml             resilience4j circuitbreaker + retry for inventory-service
```

**Known issues:**
- `InventoryController` returns `ResponseEntity<Object>` with anonymous inner classes — not serializable by Jackson reliably. Needs proper DTOs.
- `ReportingServiceImpl.getLowStockItemsByWarehouse` filters in-memory instead of using a DB query.
- `ReservationService.getReservationsByOrder` returns `List<Object>` (anonymous class) — not serializable.
- Role check uses `hasRole('ADMIN')` but the role stored is `ADMINISTRATOR`.

---

### order-service (port 8084)
```
OrderServiceApplication     @EnableDiscoveryClient, @EnableFeignClients,
                            @ComponentScan(order + common)
controller/                 (empty — not yet implemented)
dto/
  AddToCartRequest, CartDTO, CartItemDTO, CheckoutRequest, CheckoutResponse,
  CreateOrderRequest, OrderDTO, OrderItemDTO, UpdateCartItemRequest,
  WishlistItemDTO
entity/
  Cart                      extends BaseEntity; id, userId, isGuestCart, mergedAt
  CartItem                  extends BaseEntity; id, cartId, productVariantId,
                            quantity, price(BigDecimal)
  Order                     extends BaseEntity; id, userId, orderNumber(unique),
                            status, totalAmount, tax, shipping, discount, notes
  OrderItem                 extends BaseEntity; id, orderId, productVariantId,
                            quantity, price(BigDecimal)
  OrderTracking             id, orderId, status, timestamp, notes (@PrePersist)
  Wishlist                  extends BaseEntity; id, userId
  WishlistItem              extends BaseEntity; id, wishlistId, productVariantId
repository/
  CartRepository, CartItemRepository, OrderRepository, OrderItemRepository,
  OrderTrackingRepository, WishlistRepository, WishlistItemRepository
exception/
  CartEmptyException (400), InsufficientInventoryException (409),
  InvalidOrderStateException (400), OrderNotFoundException (404),
  PaymentFailedException (402)
service/impl/               (empty — not yet implemented)
db/migration/
  V1__create_order_tables.sql  carts, cart_items, wishlists, wishlist_items,
                               orders, order_items, order_tracking
                               NOTE: orders has cross-DB FK to user_service_db.users
```

**Known issues:**
- No controllers, no service implementations — skeleton only.
- Cross-database FK in SQL (`REFERENCES user_service_db.users(id)`) will fail if DBs are on separate hosts.

---

### payment-service (port 8085)
```
PaymentServiceApplication   @EnableDiscoveryClient, @ComponentScan(payment + common)
controller/                 (empty — not yet implemented)
dto/
  CreatePaymentRequest, PaymentDTO, PaymentHistoryDTO, RefundDTO, RefundRequest
entity/
  Payment                   extends BaseEntity; id, orderId(unique), amount,
                            currency(default INR), paymentMethod, status,
                            transactionId, errorMessage(TEXT)
  PaymentHistory            id, paymentId, previousStatus, newStatus,
                            timestamp, notes (@PrePersist)
  Refund                    extends BaseEntity; id, paymentId, amount,
                            reason, status, transactionId
repository/
  PaymentRepository, PaymentHistoryRepository, RefundRepository
exception/                  (empty)
service/impl/               (empty — not yet implemented)
db/migration/
  V1__create_payment_tables.sql  payments, payment_history, refunds
```

**Known issues:**
- No controllers, no service implementations — skeleton only.
- No payment gateway integration (Stripe/Razorpay/etc.).

---

### notification-service (port 8086)
```
NotificationServiceApplication @EnableDiscoveryClient, @ComponentScan(notification + common)
controller/                 (empty — not yet implemented)
dto/                        (empty — not yet implemented)
entity/
  Notification              extends BaseEntity; id, userId, type, title,
                            message(TEXT), isRead, referenceId
  EmailTemplate             extends BaseEntity; id, templateName(unique),
                            subject, body(LONGTEXT), variables(TEXT)
  NotificationLog           id, notificationType, recipientEmail, status,
                            errorMessage(TEXT), createdAt (@PrePersist)
repository/                 (empty — not yet implemented)
service/impl/               (empty — not yet implemented)
application.yml             rabbitmq: host=localhost, port=5672, user=guest/guest
db/migration/
  V1__create_notification_tables.sql  notifications, email_templates,
                                      notification_logs; 5 default templates seeded
```

**Known issues:**
- No controllers, no service implementations — skeleton only.
- RabbitMQ credentials in application.yml use `guest/guest` but docker-compose creates `admin/admin`.

---

## Infrastructure (docker-compose.yml)

| Container              | Image                        | Port(s)       |
|------------------------|------------------------------|---------------|
| user-service-db        | mysql:8.0                    | 3306:3306     |
| product-service-db     | mysql:8.0                    | 3307:3306     |
| inventory-service-db   | mysql:8.0                    | 3308:3306     |
| order-service-db       | mysql:8.0                    | 3309:3306     |
| payment-service-db     | mysql:8.0                    | 3310:3306     |
| notification-service-db| mysql:8.0                    | 3311:3306     |
| ecommerce-redis        | redis:7-alpine               | 6379:6379     |
| ecommerce-rabbitmq     | rabbitmq:3-management-alpine | 5672, 15672   |

**Note:** Spring Boot services are NOT in docker-compose — run locally via `mvn spring-boot:run`.

---

## Startup Order

```
1. docker-compose up -d          (infrastructure)
2. discovery-server              (Eureka — port 8761)
3. config-server                 (Config — port 8888)
4. api-gateway                   (Gateway — port 8080)
5. user-service                  (port 8081)
6. product-service               (port 8082)
7. inventory-service             (port 8083)
8. order-service                 (port 8084)
9. payment-service               (port 8085)
10. notification-service         (port 8086)
```

---

## Implementation Status

| Module               | Entities | Repos | Services | Controllers | Tests |
|----------------------|----------|-------|----------|-------------|-------|
| common-library       | ✅       | ✅    | ✅       | N/A         | ❌    |
| discovery-server     | N/A      | N/A   | N/A      | N/A         | ❌    |
| config-server        | N/A      | N/A   | N/A      | N/A         | ❌    |
| api-gateway          | N/A      | N/A   | N/A      | ✅ (routes) | ❌    |
| user-service         | ✅       | ✅    | ✅       | ✅          | ❌    |
| product-service      | ✅       | ✅    | ✅       | ✅          | ❌    |
| inventory-service    | ✅       | ✅    | ✅       | ✅          | ❌    |
| order-service        | ✅       | ✅    | ❌       | ❌          | ❌    |
| payment-service      | ✅       | ✅    | ❌       | ❌          | ❌    |
| notification-service | ✅       | ❌    | ❌       | ❌          | ❌    |
| frontend             | ❌       | N/A   | N/A      | N/A         | ❌    |
