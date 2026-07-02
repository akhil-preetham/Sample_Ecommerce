package com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r
                .path("/api/users/**", "/auth/**")
                .uri("lb://user-service"))
            
            .route("product-service", r -> r
                .path("/api/products/**", "/api/categories/**", "/api/brands/**")
                .uri("lb://product-service"))
            
            .route("inventory-service", r -> r
                .path("/api/inventory/**")
                .uri("lb://inventory-service"))
            
            .route("order-service", r -> r
                .path("/api/orders/**", "/api/cart/**", "/api/wishlist/**")
                .uri("lb://order-service"))
            
            .route("payment-service", r -> r
                .path("/api/payments/**")
                .uri("lb://payment-service"))
            
            .route("notification-service", r -> r
                .path("/api/notifications/**")
                .uri("lb://notification-service"))
            
            .build();
    }

}
