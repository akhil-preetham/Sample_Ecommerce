package com.ecommerce.order.service;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.order.dto.CheckoutRequest;
import com.ecommerce.order.dto.CheckoutResponse;
import com.ecommerce.order.dto.OrderDTO;

public interface OrderService {
    CheckoutResponse checkout(String userId, CheckoutRequest request);
    OrderDTO getOrder(String orderId, String userId);
    PaginationResponse<OrderDTO> getUserOrders(String userId, int page, int size);
    OrderDTO cancelOrder(String orderId, String userId);
}
