package com.ecommerce.common.constant;

public class AppConstants {
    public static final String TRACE_ID_HEADER = "X-Trace-ID";
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    public enum Role {
        CUSTOMER("CUSTOMER"),
        ADMINISTRATOR("ADMINISTRATOR"),
        VENDOR("VENDOR"),
        WAREHOUSE_MANAGER("WAREHOUSE_MANAGER"),
        DELIVERY_EXECUTIVE("DELIVERY_EXECUTIVE");
        
        public final String value;
        
        Role(String value) {
            this.value = value;
        }
    }
}
