package com.ecommerce.user.controller;

import com.ecommerce.common.constant.AppConstants;
import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.dto.ResponseWrapper;
import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserProfileDTO;
import com.ecommerce.user.service.AddressService;
import com.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User profile and address management endpoints")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping("/{userId}/profile")
    @Operation(summary = "Get user profile", description = "Retrieve user profile information")
    public ResponseEntity<ResponseWrapper<UserProfileDTO>> getUserProfile(
            @PathVariable String userId) {
        log.info("Get profile endpoint called for user: {}", userId);

        UserProfileDTO profile = userService.getUserProfile(userId);

        return ResponseEntity.ok(ResponseWrapper.success(profile, "Profile retrieved successfully"));
    }

    @PutMapping("/{userId}/profile")
    @Operation(summary = "Update user profile", description = "Update user profile information")
    public ResponseEntity<ResponseWrapper<UserProfileDTO>> updateUserProfile(
            @PathVariable String userId,
            @Valid @RequestBody UserProfileDTO profileDTO) {
        log.info("Update profile endpoint called for user: {}", userId);

        UserProfileDTO updated = userService.updateUserProfile(userId, profileDTO);

        return ResponseEntity.ok(ResponseWrapper.success(updated, "Profile updated successfully"));
    }

    @GetMapping("/{userId}/addresses")
    @Operation(summary = "Get user addresses", description = "Retrieve all addresses for user")
    public ResponseEntity<ResponseWrapper<PaginationResponse<AddressDTO>>> getUserAddresses(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Get addresses endpoint called for user: {}", userId);

        Pageable pageable = PageRequest.of(page, Math.min(size, AppConstants.MAX_PAGE_SIZE));
        PaginationResponse<AddressDTO> addresses = addressService.getUserAddresses(userId, pageable);

        return ResponseEntity.ok(ResponseWrapper.success(addresses, "Addresses retrieved successfully"));
    }

    @PostMapping("/{userId}/addresses")
    @Operation(summary = "Create address", description = "Add new address for user")
    public ResponseEntity<ResponseWrapper<AddressDTO>> createAddress(
            @PathVariable String userId,
            @Valid @RequestBody AddressDTO addressDTO) {
        log.info("Create address endpoint called for user: {}", userId);

        AddressDTO created = addressService.createAddress(userId, addressDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseWrapper.success(created, "Address created successfully"));
    }

    @GetMapping("/{userId}/addresses/{addressId}")
    @Operation(summary = "Get address", description = "Retrieve specific address for user")
    public ResponseEntity<ResponseWrapper<AddressDTO>> getAddress(
            @PathVariable String userId,
            @PathVariable String addressId) {
        log.info("Get address endpoint called for user: {}, address: {}", userId, addressId);

        AddressDTO address = addressService.getAddress(userId, addressId);

        return ResponseEntity.ok(ResponseWrapper.success(address, "Address retrieved successfully"));
    }

    @PutMapping("/{userId}/addresses/{addressId}")
    @Operation(summary = "Update address", description = "Update existing address for user")
    public ResponseEntity<ResponseWrapper<AddressDTO>> updateAddress(
            @PathVariable String userId,
            @PathVariable String addressId,
            @Valid @RequestBody AddressDTO addressDTO) {
        log.info("Update address endpoint called for user: {}, address: {}", userId, addressId);

        AddressDTO updated = addressService.updateAddress(userId, addressId, addressDTO);

        return ResponseEntity.ok(ResponseWrapper.success(updated, "Address updated successfully"));
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    @Operation(summary = "Delete address", description = "Remove address for user")
    public ResponseEntity<ResponseWrapper<String>> deleteAddress(
            @PathVariable String userId,
            @PathVariable String addressId) {
        log.info("Delete address endpoint called for user: {}, address: {}", userId, addressId);

        addressService.deleteAddress(userId, addressId);

        return ResponseEntity.ok(ResponseWrapper.success(null, "Address deleted successfully"));
    }

    @PutMapping("/{userId}/addresses/{addressId}/set-default")
    @Operation(summary = "Set default address", description = "Set address as default for user")
    public ResponseEntity<ResponseWrapper<String>> setDefaultAddress(
            @PathVariable String userId,
            @PathVariable String addressId) {
        log.info("Set default address endpoint called for user: {}, address: {}", userId, addressId);

        addressService.setDefaultAddress(userId, addressId);

        return ResponseEntity.ok(ResponseWrapper.success(null, "Default address set successfully"));
    }

    @GetMapping("/{userId}/addresses/default")
    @Operation(summary = "Get default address", description = "Retrieve default address for user")
    public ResponseEntity<ResponseWrapper<AddressDTO>> getDefaultAddress(
            @PathVariable String userId) {
        log.info("Get default address endpoint called for user: {}", userId);

        AddressDTO address = addressService.getDefaultAddress(userId);

        return ResponseEntity.ok(ResponseWrapper.success(address, "Default address retrieved successfully"));
    }
}
