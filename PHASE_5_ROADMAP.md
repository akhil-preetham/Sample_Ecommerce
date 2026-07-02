# Phase 5: Core Service Implementation - Ready to Begin

## Overview
With the project foundation complete in Phase 4, Phase 5 focuses on implementing the core business logic and endpoints for all microservices.

## Phase 5 Scope

### 1. User Service (Port 8081)
**Priority: HIGHEST - Authentication & Identity Foundation**

#### Entities to Create
- User (id, email, passwordHash, firstName, lastName, phone, isActive, createdAt, updatedAt, createdBy, updatedBy)
- UserRole (userId, role)
- UserAddress (id, userId, addressType, street, city, state, zipCode, country, isDefault, createdAt, updatedAt)
- RefreshToken (id, userId, token, expiryDate, createdAt)

#### Repositories
- UserRepository
- UserRoleRepository
- UserAddressRepository
- RefreshTokenRepository

#### Services
- UserService (CRUD, authentication)
- AuthenticationService (login, register, refresh token)
- PasswordService (change password, forgot password)

#### Controllers
- AuthController
  - POST /auth/register
  - POST /auth/login
  - POST /auth/refresh
  - POST /auth/logout
  - POST /auth/forgot-password
  - POST /auth/change-password
- UserController
  - GET /api/users/{id}
  - PUT /api/users/{id}
  - GET /api/users/{id}/profile
  - GET /api/users/{id}/addresses
  - POST /api/users/{id}/addresses
  - PUT /api/users/{id}/addresses/{addressId}
  - DELETE /api/users/{id}/addresses/{addressId}

#### Database Migration
- V1__create_user_tables.sql (from Phase 3 DDL)

---

### 2. Product Service (Port 8082)
**Priority: HIGH - Catalog Foundation**

#### Entities to Create
- Category (id, name, description, isActive, createdAt, updatedAt)
- Brand (id, name, description, isActive, createdAt, updatedAt)
- Product (id, name, description, categoryId, brandId, basePrice, rating, reviewCount, isActive, createdAt, updatedAt)
- ProductVariant (id, productId, variantName, price, stock, createdAt, updatedAt)
- ProductImage (id, productId, imageUrl, altText, isDefault, createdAt, updatedAt)
- ProductSpecification (id, productId, key, value, createdAt, updatedAt)

#### Repositories
- CategoryRepository (with caching)
- BrandRepository (with caching)
- ProductRepository (with search/filter support)
- ProductVariantRepository
- ProductImageRepository
- ProductSpecificationRepository

#### Services
- CategoryService (list, search, caching)
- BrandService (list, caching)
- ProductService (search, filter, sort, pagination, rating calculation)
- SearchService (full-text search with Elasticsearch-ready patterns)

#### Controllers
- CategoryController
  - GET /api/categories
  - GET /api/categories/{id}
  - GET /api/categories/{id}/products
- BrandController
  - GET /api/brands
  - GET /api/brands/{id}
- ProductController
  - GET /api/products (with filters: category, brand, price range, rating)
  - GET /api/products/{id}
  - GET /api/products/search (query parameter)
  - GET /api/products/suggestions (autocomplete)

#### Database Migration
- V1__create_product_tables.sql

---

### 3. Inventory Service (Port 8083)
**Priority: HIGH - Stock Management Critical for Order Processing**

#### Entities to Create
- Warehouse (id, name, location, isActive, createdAt, updatedAt)
- InventoryItem (id, productVariantId, warehouseId, availableStock, reservedStock, reorderLevel, createdAt, updatedAt)
- StockMovement (id, inventoryItemId, movementType, quantity, reference, reason, createdAt)
- StockReservation (id, orderId, inventoryItemId, quantity, status, createdAt, updatedAt)

#### Repositories
- WarehouseRepository
- InventoryItemRepository (with low stock queries)
- StockMovementRepository
- StockReservationRepository

#### Services
- InventoryService (reserve stock, release stock, check availability)
- StockMovementService (log all stock changes)
- ReservationService (manage order-level stock reservations)
- ReportingService (low stock alerts, inventory reports)

#### Controllers
- InventoryController
  - POST /api/inventory/check-availability (for order processing)
  - POST /api/inventory/reserve (reserve stock for order)
  - POST /api/inventory/release (release reserved stock)
  - GET /api/inventory/status/{variantId}
  - GET /api/inventory/low-stock (admin only)

#### Feign Clients
- ProductServiceClient (get product variant details)

#### Database Migration
- V1__create_inventory_tables.sql

---

### 4. Order Service (Port 8084)
**Priority: CRITICAL - Core Business Logic**

#### Entities to Create
- Cart (id, userId, isGuestCart, mergedAt, createdAt, updatedAt)
- CartItem (id, cartId, productVariantId, quantity, price, createdAt, updatedAt)
- Wishlist (id, userId, createdAt, updatedAt)
- WishlistItem (id, wishlistId, productVariantId, createdAt)
- Order (id, userId, orderNumber, status, totalAmount, tax, shipping, discount, createdAt, updatedAt)
- OrderItem (id, orderId, productVariantId, quantity, price, createdAt)
- OrderTracking (id, orderId, status, timestamp, notes)

#### Repositories
- CartRepository, CartItemRepository
- WishlistRepository, WishlistItemRepository
- OrderRepository (with status queries)
- OrderItemRepository
- OrderTrackingRepository

#### Services
- CartService (add/remove items, calculate totals, merge guest cart)
- WishlistService (add/remove items, move to cart)
- OrderService (create order, state transitions, cancel, return)
- CheckoutService (payment integration, inventory reservation)
- OrderTrackingService (update status)

#### Controllers
- CartController
  - GET /api/cart (current user's cart)
  - POST /api/cart/items
  - PUT /api/cart/items/{itemId}
  - DELETE /api/cart/items/{itemId}
  - POST /api/cart/merge (merge guest + authenticated cart)
- WishlistController
  - GET /api/wishlist
  - POST /api/wishlist/items
  - DELETE /api/wishlist/items/{itemId}
  - POST /api/wishlist/items/{itemId}/move-to-cart
- OrderController
  - POST /api/orders (checkout)
  - GET /api/orders/{id}
  - GET /api/orders (user's order history)
  - POST /api/orders/{id}/cancel
  - POST /api/orders/{id}/return
  - GET /api/orders/{id}/tracking

#### Feign Clients
- InventoryServiceClient (reserve/release stock)
- PaymentServiceClient (process payment)

#### Circuit Breaker Configuration
- Inventory: fallback on stock check failure

#### Database Migration
- V1__create_order_tables.sql

---

### 5. Payment Service (Port 8085)
**Priority: CRITICAL - Transaction Management**

#### Entities to Create
- Payment (id, orderId, amount, currency, paymentMethod, status, transactionId, createdAt, updatedAt)
- PaymentHistory (id, paymentId, previousStatus, newStatus, timestamp, notes)
- Refund (id, paymentId, amount, reason, status, createdAt, updatedAt)

#### Repositories
- PaymentRepository (with status queries)
- PaymentHistoryRepository
- RefundRepository

#### Services
- PaymentService (process payment, validate amount, handle failures)
- MockPaymentGatewayService (simulate payment processing)
- RefundService (process refunds)

#### Controllers
- PaymentController
  - POST /api/payments (process payment)
  - GET /api/payments/{id}
  - POST /api/payments/{id}/refund
  - GET /api/payments/{orderId}/status (check payment status)

#### Payment States
- PENDING → AUTHORIZED → CAPTURED → REFUNDED or FAILED
- Compensating transaction: if payment fails, trigger inventory release via Order Service

#### Database Migration
- V1__create_payment_tables.sql

---

### 6. Notification Service (Port 8086)
**Priority: MEDIUM - Event-Driven Asynchronous**

#### Entities to Create
- Notification (id, userId, type, title, message, isRead, createdAt)
- EmailTemplate (id, templateName, subject, body, variables)
- NotificationLog (id, notificationType, recipientEmail, status, createdAt)

#### Repositories
- NotificationRepository
- EmailTemplateRepository
- NotificationLogRepository

#### Services
- NotificationService (create in-app notifications)
- EmailService (mock email sending)
- RabbitMQEventListener (listen to order/payment events)

#### Controllers
- NotificationController
  - GET /api/notifications (user's notifications)
  - PUT /api/notifications/{id}/mark-as-read
  - DELETE /api/notifications/{id}

#### RabbitMQ Consumers
- OrderCreatedEventConsumer (send order confirmation email)
- PaymentProcessedEventConsumer (send payment confirmation)
- OrderShippedEventConsumer (send shipping notification)
- OrderDeliveredEventConsumer (send delivery notification)

#### Database Migration
- V1__create_notification_tables.sql

---

## Phase 5 Implementation Checklist

### Common Steps for Each Service

- [ ] Create entity classes with @Entity, @Table, @Column annotations
- [ ] Add audit fields (createdAt, updatedAt, createdBy, updatedBy) extending BaseEntity
- [ ] Create repository interfaces extending BaseRepository
- [ ] Create repository custom implementation methods as needed
- [ ] Create DTO classes for request/response
- [ ] Create MapStruct mappers for entity ↔ DTO conversions
- [ ] Create service interfaces extending BaseService
- [ ] Implement service classes with business logic
- [ ] Create controller classes with @RestController
- [ ] Add @GetMapping, @PostMapping, @PutMapping, @DeleteMapping methods
- [ ] Add @Validated and Bean Validation annotations (@NotNull, @Email, etc.)
- [ ] Add Swagger @Operation, @ApiResponse annotations
- [ ] Add exception handling (throw custom exceptions from service)
- [ ] Create Flyway migration SQL scripts
- [ ] Write unit tests with MockMvc for controllers
- [ ] Write integration tests with TestRestTemplate
- [ ] Add @Transactional where needed
- [ ] Configure query optimization (@EntityGraph, pagination)

---

## Parallel Work Streams

### Stream 1: User Service
- [ ] User authentication & authorization
- [ ] JWT token endpoints
- [ ] User profile management
- [ ] Address management
- [ ] Password reset flow

### Stream 2: Product Service + Inventory
- [ ] Product catalog
- [ ] Category & brand management
- [ ] Search & filtering
- [ ] Stock management
- [ ] Low stock alerts

### Stream 3: Order Service
- [ ] Shopping cart functionality
- [ ] Wishlist management
- [ ] Order creation (checkout)
- [ ] Order state machine
- [ ] Order history & tracking

### Stream 4: Payment & Notifications
- [ ] Payment processing
- [ ] Mock payment gateway
- [ ] Refund handling
- [ ] Email notifications
- [ ] In-app notifications
- [ ] RabbitMQ event listeners

---

## Estimated Effort

| Service | Complexity | Estimated Lines | Priority |
|---------|------------|-----------------|----------|
| User | High | 800-1000 | 1 (Blocker) |
| Product | Medium | 600-800 | 2 |
| Inventory | High | 800-1000 | 2 |
| Order | Very High | 1200-1500 | 1 (Blocker) |
| Payment | High | 600-800 | 1 (Blocker) |
| Notification | Medium | 400-600 | 3 |
| **Total** | - | **4400-5700** | - |

---

## Testing Strategy

### Unit Tests (Minimum)
- Service layer: 70% coverage (mocked repositories)
- Utility classes: 90% coverage

### Integration Tests
- Controller endpoints: request/response validation
- Repository queries: database integration
- Service transactions: rollback scenarios

### Test Setup
- Use @SpringBootTest for integration tests
- Use MockMvc for controller tests
- Use TestRestTemplate for end-to-end tests
- H2 in-memory database for test isolation

---

## Development Order Recommendation

1. **Week 1**: User Service (foundation for all other services)
2. **Week 2**: Product Service + Inventory Service (parallel)
3. **Week 3**: Order Service (depends on User, Product, Inventory)
4. **Week 4**: Payment Service + Notification Service (parallel)
5. **Week 5**: Integration & Testing

---

## Performance Optimization Reminders

- Use pagination (default 20, max 100)
- Implement @EntityGraph for N+1 prevention
- Add caching with @Cacheable for products/categories
- Use JDBC for complex reporting queries
- Batch database operations
- Add database indexes (from Phase 3 DDL)

---

## Security Reminders

- All endpoints except login/register require JWT token
- Use @Secured or @PreAuthorize for role-based access
- Validate all input with @Valid
- Sanitize error messages (no stack traces in production)
- Use BCrypt for password hashing (Spring Security provides)
- Add CORS configuration to API Gateway

---

**Phase 5 is ready to commence!**

