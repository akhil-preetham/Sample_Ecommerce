package com.ecommerce.common.util;

import com.ecommerce.common.exception.ValidationException;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[+]?[0-9]{10,15}$");
    
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }
    
    public static void validatePhoneNumber(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Invalid phone number format");
        }
    }
    
    public static void validatePassword(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new ValidationException(
                "Password must be at least 8 characters with uppercase, lowercase, digit, and special character"
            );
        }
    }
    
    public static void validateNotNull(Object object, String fieldName) {
        if (object == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
    }
    
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be blank");
        }
    }
}
