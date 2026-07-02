package com.ecommerce.common.util;

import java.util.UUID;

public class UUIDUtil {
    
    private UUIDUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
