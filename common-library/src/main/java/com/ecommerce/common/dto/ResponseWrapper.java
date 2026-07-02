package com.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
    private boolean success;
    private T data;
    private String message;
    private LocalDateTime timestamp;
    private String traceId;

    public static <T> ResponseWrapper<T> success(T data, String message) {
        return ResponseWrapper.<T>builder()
            .success(true)
            .data(data)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }

    public static <T> ResponseWrapper<T> error(String message, String traceId) {
        return ResponseWrapper.<T>builder()
            .success(false)
            .message(message)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();
    }
}
