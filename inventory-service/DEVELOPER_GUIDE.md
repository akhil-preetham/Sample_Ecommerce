# Inventory Service - Developer Guide

## Service Overview

The Inventory Service manages all stock-related operations for the Enterprise E-Commerce platform, including:
- Real-time inventory availability checking
- Order-level stock reservations
- Stock movement auditing
- Warehouse management
- Low stock reporting

## Key Endpoints

### Inventory Management (`/api/inventory`)

#### 1. Check Availability
**POST** `/api/inventory/check-availability`
```json
{
  "productVariantId": "var-123",
  "quantity": 5,
  "warehouseId": "wh-001"  // optional, if null checks all warehouses
}
```
Response: `{ "available": true, "productVariantId": "var-123", "requestedQuantity": 5 }`

#### 2. Reserve Stock
**POST** `/api/inventory/reserve`
```json
{
  "orderId": "order-456",
  "inventoryItemId": "inv-789",
  "quantity": 5
}
```
Response: `{ "reservationId": "order-456", "message": "Stock reserved successfully" }`

**Note**: This operation is transactional and logged for auditing.

#### 3. Release Stock
**POST** `/api/inventory/release`
```json
{
  "orderId": "order-456",
  "inventoryItemId": "inv-789",
  "quantity": 5
}
```
Response: `{ "message": "Stock released successfully", "orderId": "order-456" }`

#### 4. Get Inventory Status
**GET** `/api/inventory/status/{variantId}`

Response:
```json
{
  "inventoryItemId": "inv-789",
  "productVariantId": "var-123",
  "warehouseId": "wh-001",
  "availableStock": 100,
  "reservedStock": 5,
  "totalStock": 105,
  "reorderLevel": 10,
  "lowStock": false,
  "lastUpdated": "2024-01-15T10:30:00"
}
```

#### 5. Get Low Stock Items (Admin Only)
**GET** `/api/inventory/low-stock`

Response: Array of low stock items
```json
[
  {
    "id": "inv-789",
    "productVariantId": "var-123",
    "warehouseId": "wh-001",
    "availableStock": 8,
    "reorderLevel": 10,
    "reservedStock": 2
  }
]
```

#### 6. Get Stock Movement History
**GET** `/api/inventory/movements/{itemId}`

Response: Array of all stock movements
```json
[
  {
    "id": "mov-001",
    "movementType": "RESERVED",
    "quantity": 5,
    "reference": "order-456",
    "reason": "Order reserved",
    "createdAt": "2024-01-15T10:30:00"
  }
]
```

### Warehouse Management (`/api/warehouses`)

#### 1. Get All Warehouses
**GET** `/api/warehouses`

#### 2. Get Warehouse by ID
**GET** `/api/warehouses/{id}`

#### 3. Create Warehouse (Admin Only)
**POST** `/api/warehouses`
```json
{
  "name": "Main Warehouse",
  "location": "New York"
}
```

#### 4. Update Warehouse (Admin Only)
**PUT** `/api/warehouses/{id}`
```json
{
  "name": "Main Warehouse - Updated",
  "location": "Boston",
  "isActive": true
}
```

#### 5. Delete Warehouse (Admin Only)
**DELETE** `/api/warehouses/{id}`

## Error Handling

### Response Status Codes

| Status | Meaning | Example |
|--------|---------|---------|
| 200 | Success | Availability check completed |
| 201 | Created | Warehouse created successfully |
| 400 | Bad Request | Insufficient stock, invalid input |
| 401 | Unauthorized | Missing or invalid authentication token |
| 403 | Forbidden | Admin access required |
| 404 | Not Found | Inventory item or warehouse not found |
| 500 | Server Error | Internal server error |

### Exception Handling

```java
// InsufficientStockException
{
  "errorCode": "INSUFFICIENT_STOCK",
  "message": "Insufficient stock. Available: 5 Requested: 10"
}

// ItemNotFoundException
{
  "errorCode": "ITEM_NOT_FOUND",
  "message": "Inventory item not found: inv-789"
}
```

## Thread Safety Features

### Optimistic Locking
- Every stock update increments a version number
- Concurrent updates on the same item will result in a retry
- Maximum 3 retries with exponential backoff (100ms, 200ms, 400ms)

### Transactional Boundaries
- All stock operations are wrapped in @Transactional
- Ensures ACID properties for all inventory updates
- Automatic rollback on exceptions

## Integration Examples

### Reserve Stock for Order

```java
// 1. Check availability first
CheckAvailabilityRequest availRequest = CheckAvailabilityRequest.builder()
    .productVariantId("var-123")
    .quantity(5)
    .warehouseId("wh-001")
    .build();
    
boolean available = inventoryService.checkAvailability(availRequest);

if (available) {
    // 2. Reserve stock
    ReserveStockRequest reserveRequest = ReserveStockRequest.builder()
        .orderId("order-456")
        .inventoryItemId("inv-789")
        .quantity(5)
        .build();
    
    inventoryService.reserveStock(reserveRequest);
}
```

### Get Low Stock Alert

```java
// Admin endpoint to monitor inventory
List<Object> lowStockItems = reportingService.getLowStockItems();
lowStockItems.forEach(item -> {
    // Send alert to procurement team
    // Trigger automatic reorder if configured
});
```

## Database Migrations

The service uses Flyway for database migrations:

1. `V1__create_inventory_tables.sql` - Initial schema
2. `V2__add_optimistic_locking.sql` - Optimistic locking support

All migrations are automatically applied on service startup.

## Configuration

### application.yml

Key configuration options:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Never auto-create/update schema in production
  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8083

feign:
  client:
    config:
      product-service:
        connect-timeout: 5000
        read-timeout: 5000
```

## Performance Optimization

### Database Indexes
- `idx_product_variant_id` - Fast lookup by product variant
- `idx_warehouse_id` - Fast lookup by warehouse
- `idx_available_stock` - Fast low-stock queries
- `idx_order_id` - Fast reservation lookup
- `unique_variant_warehouse` - Prevents duplicate inventory records

### Connection Pooling
- HikariCP with 20 max connections
- Minimum 5 idle connections
- Optimal for high-throughput stock operations

## Testing

### Unit Tests
```java
@SpringBootTest
class InventoryServiceTest {
    
    @MockBean
    private InventoryItemRepository repository;
    
    @InjectMocks
    private InventoryServiceImpl service;
    
    @Test
    void testCheckAvailability() {
        // Arrange
        InventoryItem item = InventoryItem.builder()
            .id("inv-789")
            .availableStock(10L)
            .build();
        when(repository.findById("inv-789")).thenReturn(Optional.of(item));
        
        // Act
        CheckAvailabilityRequest request = CheckAvailabilityRequest.builder()
            .productVariantId("var-123")
            .quantity(5)
            .build();
        boolean result = service.checkAvailability(request);
        
        // Assert
        assertTrue(result);
    }
}
```

### Integration Tests
```java
@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testCheckAvailabilityEndpoint() throws Exception {
        CheckAvailabilityRequest request = new CheckAvailabilityRequest(
            "var-123", 5L, null
        );
        
        mockMvc.perform(post("/api/inventory/check-availability")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request))
            .header("Authorization", "Bearer token"))
            .andExpect(status().isOk());
    }
}
```

## Troubleshooting

### Common Issues

1. **OptimisticLockingFailureException**
   - Cause: Concurrent updates to the same inventory item
   - Solution: This is handled automatically with retry mechanism
   - Check logs for "Retry attempt" messages

2. **InsufficientStockException**
   - Cause: Requested quantity exceeds available stock
   - Solution: Check availability before reserving
   - Implement queue management for back-orders

3. **ItemNotFoundException**
   - Cause: Inventory item or warehouse not found
   - Solution: Verify IDs exist before making requests
   - Initialize warehouses and inventory items first

## API Documentation

Swagger UI is available at: `http://localhost:8083/swagger-ui.html`

All endpoints include complete documentation with:
- Request/response schemas
- Error codes and descriptions
- Authentication requirements
- Example values

## Support

For issues or questions, refer to:
1. Service logs in `/logs/inventory-service.log`
2. Swagger documentation at `/swagger-ui.html`
3. Database migration logs for schema-related issues
4. Application configuration in `application.yml`
