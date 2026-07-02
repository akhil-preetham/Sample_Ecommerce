# MODULE_INDEX.md

Quick lookup table. Use this to find files without scanning the workspace.
Format: `module → layer → file path`

---

## common-library

| Layer      | File                                                                                      |
|------------|-------------------------------------------------------------------------------------------|
| Entity     | `common-library/src/main/java/com/ecommerce/common/entity/BaseEntity.java`                |
| Exception  | `common-library/src/main/java/com/ecommerce/common/exception/BaseException.java`          |
| Exception  | `common-library/src/main/java/com/ecommerce/common/exception/ResourceNotFoundException.java` |
| Exception  | `common-library/src/main/java/com/ecommerce/common/exception/ValidationException.java`   |
| Exception  | `common-library/src/main/java/com/ecommerce/common/exception/ServiceException.java`      |
| Exception  | `common-library/src/main/java/com/ecommerce/common/exception/GlobalExceptionHandler.java`|
| DTO        | `common-library/src/main/java/com/ecommerce/common/dto/ResponseWrapper.java`             |
| DTO        | `common-library/src/main/java/com/ecommerce/common/dto/PaginationResponse.java`          |
| DTO        | `common-library/src/main/java/com/ecommerce/common/dto/ErrorResponse.java`               |
| Config     | `common-library/src/main/java/com/ecommerce/common/config/JwtUtil.java`                  |
| Constant   | `common-library/src/main/java/com/ecommerce/common/constant/AppConstants.java`           |
| Constant   | `common-library/src/main/java/com/ecommerce/common/constant/ErrorCodes.java`             |
| Repository | `common-library/src/main/java/com/ecommerce/common/repository/BaseRepository.java`       |
| Service    | `common-library/src/main/java/com/ecommerce/common/service/BaseService.java`             |
| Util       | `common-library/src/main/java/com/ecommerce/common/util/UUIDUtil.java`                   |
| Util       | `common-library/src/main/java/com/ecommerce/common/util/ValidationUtil.java`             |
| Resources  | `common-library/src/main/resources/logback-spring.xml`                                   |
| POM        | `common-library/pom.xml`                                                                  |

---

## discovery-server

| Layer   | File                                                                                                    |
|---------|---------------------------------------------------------------------------------------------------------|
| Main    | `discovery-server/src/main/java/com/ecommerce/discovery/DiscoveryServerApplication.java`               |
| Config  | `discovery-server/src/main/resources/application.yml`                                                  |
| POM     | `discovery-server/pom.xml`                                                                              |

---

## config-server

| Layer      | File                                                                                               |
|------------|----------------------------------------------------------------------------------------------------|
| Main       | `config-server/src/main/java/com/ecommerce/config/ConfigServerApplication.java`                   |
| Config     | `config-server/src/main/resources/application.yml`                                                |
| Repo       | `config-server/config-repo/application.yml`          ← global defaults                           |
| Repo       | `config-server/config-repo/user-service.yml`                                                      |
| Repo       | `config-server/config-repo/product-service.yml`                                                   |
| Repo       | `config-server/config-repo/inventory-service.yml`                                                 |
| Repo       | `config-server/config-repo/order-service.yml`                                                     |
| Repo       | `config-server/config-repo/payment-service.yml`                                                   |
| Repo       | `config-server/config-repo/notification-service.yml`                                              |
| POM        | `config-server/pom.xml`                                                                            |

---

## api-gateway

| Layer      | File                                                                                               |
|------------|----------------------------------------------------------------------------------------------------|
| Main       | `api-gateway/src/main/java/com/ecommerce/gateway/ApiGatewayApplication.java`                      |
| Config     | `api-gateway/src/main/resources/application.yml`                                                  |
| POM        | `api-gateway/pom.xml`                                                                              |

Routes defined as `RouteLocator` bean inside `ApiGatewayApplication`.

---

## user-service

| Layer      | File                                                                                                        |
|------------|-------------------------------------------------------------------------------------------------------------|
| Main       | `user-service/src/main/java/com/ecommerce/user/UserServiceApplication.java`                                 |
| Config     | `user-service/src/main/java/com/ecommerce/user/config/SecurityConfig.java`                                  |
| Controller | `user-service/src/main/java/com/ecommerce/user/controller/AuthController.java`                              |
| Controller | `user-service/src/main/java/com/ecommerce/user/controller/UserController.java`                              |
| DTO        | `user-service/src/main/java/com/ecommerce/user/dto/LoginRequest.java`                                       |
| DTO        | `user-service/src/main/java/com/ecommerce/user/dto/RegisterRequest.java`                                    |
| DTO        | `user-service/src/main/java/com/ecommerce/user/dto/LoginResponse.java`                                      |
| DTO        | `user-service/src/main/java/com/ecommerce/user/dto/UserProfileDTO.java`                                     |
| DTO        | `user-service/src/main/java/com/ecommerce/user/dto/AddressDTO.java`                                         |
| Entity     | `user-service/src/main/java/com/ecommerce/user/entity/User.java`                                            |
| Entity     | `user-service/src/main/java/com/ecommerce/user/entity/UserRole.java`                                        |
| Entity     | `user-service/src/main/java/com/ecommerce/user/entity/UserAddress.java`                                     |
| Entity     | `user-service/src/main/java/com/ecommerce/user/entity/RefreshToken.java`                                    |
| Repository | `user-service/src/main/java/com/ecommerce/user/repository/UserRepository.java`                              |
| Repository | `user-service/src/main/java/com/ecommerce/user/repository/UserRoleRepository.java`                          |
| Repository | `user-service/src/main/java/com/ecommerce/user/repository/UserAddressRepository.java`                       |
| Repository | `user-service/src/main/java/com/ecommerce/user/repository/RefreshTokenRepository.java`                      |
| Service    | `user-service/src/main/java/com/ecommerce/user/service/AuthenticationService.java`                          |
| Service    | `user-service/src/main/java/com/ecommerce/user/service/UserService.java`                                    |
| Service    | `user-service/src/main/java/com/ecommerce/user/service/AddressService.java`                                 |
| Impl       | `user-service/src/main/java/com/ecommerce/user/service/impl/AuthenticationServiceImpl.java`                 |
| Impl       | `user-service/src/main/java/com/ecommerce/user/service/impl/UserServiceImpl.java`                           |
| Impl       | `user-service/src/main/java/com/ecommerce/user/service/impl/AddressServiceImpl.java`                        |
| Migration  | `user-service/src/main/resources/db/migration/V1__create_user_tables.sql`                                   |
| App Config | `user-service/src/main/resources/application.yml`                                                           |
| POM        | `user-service/pom.xml`                                                                                       |

APIs: `/auth/**`, `/api/users/**`
Depends on: `common-library`

---

## product-service

| Layer      | File                                                                                                           |
|------------|----------------------------------------------------------------------------------------------------------------|
| Main       | `product-service/src/main/java/com/ecommerce/product/ProductServiceApplication.java`                          |
| Config     | `product-service/src/main/java/com/ecommerce/product/config/SecurityConfig.java`                              |
| Config     | `product-service/src/main/java/com/ecommerce/product/config/CacheConfig.java`                                 |
| Controller | `product-service/src/main/java/com/ecommerce/product/controller/ProductController.java`                       |
| Controller | `product-service/src/main/java/com/ecommerce/product/controller/CategoryController.java`                      |
| Controller | `product-service/src/main/java/com/ecommerce/product/controller/BrandController.java`                         |
| DTO        | `product-service/src/main/java/com/ecommerce/product/dto/ProductDTO.java`                                     |
| DTO        | `product-service/src/main/java/com/ecommerce/product/dto/CategoryDTO.java`                                    |
| DTO        | `product-service/src/main/java/com/ecommerce/product/dto/BrandDTO.java`                                       |
| DTO        | `product-service/src/main/java/com/ecommerce/product/dto/ProductVariantDTO.java`                              |
| DTO        | `product-service/src/main/java/com/ecommerce/product/dto/ProductImageDTO.java`                                |
| Entity     | `product-service/src/main/java/com/ecommerce/product/entity/Product.java`                                     |
| Entity     | `product-service/src/main/java/com/ecommerce/product/entity/Category.java`                                    |
| Entity     | `product-service/src/main/java/com/ecommerce/product/entity/Brand.java`                                       |
| Entity     | `product-service/src/main/java/com/ecommerce/product/entity/ProductVariant.java`                              |
| Entity     | `product-service/src/main/java/com/ecommerce/product/entity/ProductImage.java`                                |
| Mapper     | `product-service/src/main/java/com/ecommerce/product/mapper/ProductMapper.java`                               |
| Mapper     | `product-service/src/main/java/com/ecommerce/product/mapper/CategoryMapper.java`                              |
| Mapper     | `product-service/src/main/java/com/ecommerce/product/mapper/BrandMapper.java`                                 |
| Mapper     | `product-service/src/main/java/com/ecommerce/product/mapper/ProductVariantMapper.java`                        |
| Mapper     | `product-service/src/main/java/com/ecommerce/product/mapper/ProductImageMapper.java`                          |
| Repository | `product-service/src/main/java/com/ecommerce/product/repository/ProductRepository.java`                       |
| Repository | `product-service/src/main/java/com/ecommerce/product/repository/CategoryRepository.java`                      |
| Repository | `product-service/src/main/java/com/ecommerce/product/repository/BrandRepository.java`                         |
| Repository | `product-service/src/main/java/com/ecommerce/product/repository/ProductVariantRepository.java`                |
| Repository | `product-service/src/main/java/com/ecommerce/product/repository/ProductImageRepository.java`                  |
| Service    | `product-service/src/main/java/com/ecommerce/product/service/ProductService.java`                             |
| Service    | `product-service/src/main/java/com/ecommerce/product/service/CategoryService.java`                            |
| Service    | `product-service/src/main/java/com/ecommerce/product/service/BrandService.java`                               |
| Impl       | `product-service/src/main/java/com/ecommerce/product/service/impl/ProductServiceImpl.java`                    |
| Impl       | `product-service/src/main/java/com/ecommerce/product/service/impl/CategoryServiceImpl.java`                   |
| Impl       | `product-service/src/main/java/com/ecommerce/product/service/impl/BrandServiceImpl.java`                      |
| Migration  | `product-service/src/main/resources/db/migration/V1__create_product_tables.sql`                               |
| App Config | `product-service/src/main/resources/application.yml`                                                          |
| POM        | `product-service/pom.xml`                                                                                      |

APIs: `/api/products/**`, `/api/categories/**`, `/api/brands/**`
Depends on: `common-library`

---

## inventory-service

| Layer      | File                                                                                                              |
|------------|-------------------------------------------------------------------------------------------------------------------|
| Main       | `inventory-service/src/main/java/com/ecommerce/inventory/InventoryServiceApplication.java`                       |
| Client     | `inventory-service/src/main/java/com/ecommerce/inventory/client/ProductServiceClient.java`                       |
| Client     | `inventory-service/src/main/java/com/ecommerce/inventory/client/ProductServiceClientFallback.java`               |
| Client     | `inventory-service/src/main/java/com/ecommerce/inventory/client/ProductVariantResponse.java`                     |
| Controller | `inventory-service/src/main/java/com/ecommerce/inventory/controller/InventoryController.java`                    |
| Controller | `inventory-service/src/main/java/com/ecommerce/inventory/controller/WarehouseController.java`                    |
| DTO        | `inventory-service/src/main/java/com/ecommerce/inventory/dto/CheckAvailabilityRequest.java`                      |
| DTO        | `inventory-service/src/main/java/com/ecommerce/inventory/dto/ReserveStockRequest.java`                           |
| DTO        | `inventory-service/src/main/java/com/ecommerce/inventory/dto/ReleaseStockRequest.java`                           |
| DTO        | `inventory-service/src/main/java/com/ecommerce/inventory/dto/InventoryItemDTO.java`                              |
| DTO        | `inventory-service/src/main/java/com/ecommerce/inventory/dto/StockStatusResponse.java`                           |
| DTO        | `inventory-service/src/main/java/com/ecommerce/inventory/dto/WarehouseDTO.java`                                  |
| Entity     | `inventory-service/src/main/java/com/ecommerce/inventory/entity/InventoryItem.java`                              |
| Entity     | `inventory-service/src/main/java/com/ecommerce/inventory/entity/StockReservation.java`                           |
| Entity     | `inventory-service/src/main/java/com/ecommerce/inventory/entity/StockMovement.java`                              |
| Entity     | `inventory-service/src/main/java/com/ecommerce/inventory/entity/Warehouse.java`                                  |
| Exception  | `inventory-service/src/main/java/com/ecommerce/inventory/exception/InsufficientStockException.java`              |
| Exception  | `inventory-service/src/main/java/com/ecommerce/inventory/exception/ItemNotFoundException.java`                   |
| Repository | `inventory-service/src/main/java/com/ecommerce/inventory/repository/InventoryItemRepository.java`                |
| Repository | `inventory-service/src/main/java/com/ecommerce/inventory/repository/StockReservationRepository.java`             |
| Repository | `inventory-service/src/main/java/com/ecommerce/inventory/repository/StockMovementRepository.java`                |
| Repository | `inventory-service/src/main/java/com/ecommerce/inventory/repository/WarehouseRepository.java`                    |
| Service    | `inventory-service/src/main/java/com/ecommerce/inventory/service/InventoryService.java`                          |
| Service    | `inventory-service/src/main/java/com/ecommerce/inventory/service/ReservationService.java`                        |
| Service    | `inventory-service/src/main/java/com/ecommerce/inventory/service/StockMovementService.java`                      |
| Service    | `inventory-service/src/main/java/com/ecommerce/inventory/service/WarehouseService.java`                          |
| Service    | `inventory-service/src/main/java/com/ecommerce/inventory/service/ReportingService.java`                          |
| Impl       | `inventory-service/src/main/java/com/ecommerce/inventory/service/impl/InventoryServiceImpl.java`                 |
| Impl       | `inventory-service/src/main/java/com/ecommerce/inventory/service/impl/ReservationServiceImpl.java`               |
| Impl       | `inventory-service/src/main/java/com/ecommerce/inventory/service/impl/StockMovementServiceImpl.java`             |
| Impl       | `inventory-service/src/main/java/com/ecommerce/inventory/service/impl/WarehouseServiceImpl.java`                 |
| Impl       | `inventory-service/src/main/java/com/ecommerce/inventory/service/impl/ReportingServiceImpl.java`                 |
| Migration  | `inventory-service/src/main/resources/db/migration/V1__create_inventory_tables.sql`                              |
| Migration  | `inventory-service/src/main/resources/db/migration/V2__add_optimistic_locking.sql`                               |
| App Config | `inventory-service/src/main/resources/application.yml`                                                           |
| POM        | `inventory-service/pom.xml`                                                                                       |

APIs: `/api/inventory/**`, `/api/warehouses/**`
Depends on: `common-library`, Feign → `product-service`

---

## order-service

| Layer      | File                                                                                                        |
|------------|-------------------------------------------------------------------------------------------------------------|
| Main       | `order-service/src/main/java/com/ecommerce/order/OrderServiceApplication.java`                              |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/AddToCartRequest.java`                                 |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/CartDTO.java`                                          |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/CartItemDTO.java`                                      |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/CheckoutRequest.java`                                  |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/CheckoutResponse.java`                                 |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/CreateOrderRequest.java`                               |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/OrderDTO.java`                                         |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/OrderItemDTO.java`                                     |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/UpdateCartItemRequest.java`                            |
| DTO        | `order-service/src/main/java/com/ecommerce/order/dto/WishlistItemDTO.java`                                  |
| Entity     | `order-service/src/main/java/com/ecommerce/order/entity/Cart.java`                                          |
| Entity     | `order-service/src/main/java/com/ecommerce/order/entity/CartItem.java`                                      |
| Entity     | `order-service/src/main/java/com/ecommerce/order/entity/Order.java`                                         |
| Entity     | `order-service/src/main/java/com/ecommerce/order/entity/OrderItem.java`                                     |
| Entity     | `order-service/src/main/java/com/ecommerce/order/entity/OrderTracking.java`                                 |
| Entity     | `order-service/src/main/java/com/ecommerce/order/entity/Wishlist.java`                                      |
| Entity     | `order-service/src/main/java/com/ecommerce/order/entity/WishlistItem.java`                                  |
| Exception  | `order-service/src/main/java/com/ecommerce/order/exception/CartEmptyException.java`                         |
| Exception  | `order-service/src/main/java/com/ecommerce/order/exception/InsufficientInventoryException.java`             |
| Exception  | `order-service/src/main/java/com/ecommerce/order/exception/InvalidOrderStateException.java`                 |
| Exception  | `order-service/src/main/java/com/ecommerce/order/exception/OrderNotFoundException.java`                     |
| Exception  | `order-service/src/main/java/com/ecommerce/order/exception/PaymentFailedException.java`                     |
| Repository | `order-service/src/main/java/com/ecommerce/order/repository/CartRepository.java`                            |
| Repository | `order-service/src/main/java/com/ecommerce/order/repository/CartItemRepository.java`                        |
| Repository | `order-service/src/main/java/com/ecommerce/order/repository/OrderRepository.java`                           |
| Repository | `order-service/src/main/java/com/ecommerce/order/repository/OrderItemRepository.java`                       |
| Repository | `order-service/src/main/java/com/ecommerce/order/repository/OrderTrackingRepository.java`                   |
| Repository | `order-service/src/main/java/com/ecommerce/order/repository/WishlistRepository.java`                        |
| Repository | `order-service/src/main/java/com/ecommerce/order/repository/WishlistItemRepository.java`                    |
| Migration  | `order-service/src/main/resources/db/migration/V1__create_order_tables.sql`                                 |
| App Config | `order-service/src/main/resources/application.yml`                                                          |
| POM        | `order-service/pom.xml`                                                                                      |

APIs: `/api/orders/**`, `/api/cart/**`, `/api/wishlist/**`  ← **NOT YET IMPLEMENTED**
Depends on: `common-library`, Feign → `inventory-service`, `payment-service`

---

## payment-service

| Layer      | File                                                                                                          |
|------------|---------------------------------------------------------------------------------------------------------------|
| Main       | `payment-service/src/main/java/com/ecommerce/payment/PaymentServiceApplication.java`                         |
| DTO        | `payment-service/src/main/java/com/ecommerce/payment/dto/CreatePaymentRequest.java`                          |
| DTO        | `payment-service/src/main/java/com/ecommerce/payment/dto/PaymentDTO.java`                                    |
| DTO        | `payment-service/src/main/java/com/ecommerce/payment/dto/PaymentHistoryDTO.java`                             |
| DTO        | `payment-service/src/main/java/com/ecommerce/payment/dto/RefundDTO.java`                                     |
| DTO        | `payment-service/src/main/java/com/ecommerce/payment/dto/RefundRequest.java`                                 |
| Entity     | `payment-service/src/main/java/com/ecommerce/payment/entity/Payment.java`                                    |
| Entity     | `payment-service/src/main/java/com/ecommerce/payment/entity/PaymentHistory.java`                             |
| Entity     | `payment-service/src/main/java/com/ecommerce/payment/entity/Refund.java`                                     |
| Repository | `payment-service/src/main/java/com/ecommerce/payment/repository/PaymentRepository.java`                      |
| Repository | `payment-service/src/main/java/com/ecommerce/payment/repository/PaymentHistoryRepository.java`               |
| Repository | `payment-service/src/main/java/com/ecommerce/payment/repository/RefundRepository.java`                       |
| Migration  | `payment-service/src/main/resources/db/migration/V1__create_payment_tables.sql`                              |
| App Config | `payment-service/src/main/resources/application.yml`                                                         |
| POM        | `payment-service/pom.xml`                                                                                     |

APIs: `/api/payments/**`  ← **NOT YET IMPLEMENTED**
Depends on: `common-library`

---

## notification-service

| Layer      | File                                                                                                               |
|------------|--------------------------------------------------------------------------------------------------------------------|
| Main       | `notification-service/src/main/java/com/ecommerce/notification/NotificationServiceApplication.java`               |
| Entity     | `notification-service/src/main/java/com/ecommerce/notification/entity/Notification.java`                          |
| Entity     | `notification-service/src/main/java/com/ecommerce/notification/entity/EmailTemplate.java`                         |
| Entity     | `notification-service/src/main/java/com/ecommerce/notification/entity/NotificationLog.java`                       |
| Migration  | `notification-service/src/main/resources/db/migration/V1__create_notification_tables.sql`                         |
| App Config | `notification-service/src/main/resources/application.yml`                                                         |
| POM        | `notification-service/pom.xml`                                                                                     |

APIs: `/api/notifications/**`  ← **NOT YET IMPLEMENTED**
Depends on: `common-library`, RabbitMQ

---

## Infrastructure

| File                  | Purpose                                      |
|-----------------------|----------------------------------------------|
| `docker-compose.yml`  | 6 MySQL DBs + Redis + RabbitMQ               |
| `pom.xml`             | Parent POM, all module declarations          |

---

## Known Bugs (Quick Reference)

| Location                                    | Issue                                                              |
|---------------------------------------------|--------------------------------------------------------------------|
| `AuthController.extractUserIdFromToken()`   | Returns JWT string, not parsed userId                              |
| `AuthenticationServiceImpl.forgotPassword`  | Stub — no email sent                                               |
| `AuthenticationServiceImpl.resetPassword`   | Stub — no token validation, no password update                     |
| `User.roles` FetchType.EAGER                | N+1 on every user load                                             |
| `InventoryController` anonymous classes     | `new Object(){...}` not reliably serialized by Jackson             |
| `ReportingServiceImpl.getLowStockByWarehouse` | In-memory filter instead of DB query                             |
| `ReservationServiceImpl.getReservationsByOrder` | Returns `List<Object>` anonymous — not serializable            |
| `InventoryController` role check            | Uses `hasRole('ADMIN')` but stored role is `ADMINISTRATOR`         |
| `Product.stock`                             | Duplicates inventory-service data — source of truth conflict       |
| `CategoryRepository` @Cacheable             | Anti-pattern: cache annotation on repository, not service          |
| `CacheConfig`                               | Empty — no Redis CacheManager, falls back to in-memory             |
| `ProductController` minPrice/maxPrice       | Accepted as params but never passed to service                     |
| `order_tables.sql` FK                       | Cross-DB FK `REFERENCES user_service_db.users(id)` — breaks on separate hosts |
| `notification-service` RabbitMQ credentials | `guest/guest` in app.yml vs `admin/admin` in docker-compose        |
| `config-server` git uri                     | `file://${user.home}/config-repo` — config-repo is inside project, not home dir |
