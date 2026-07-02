package com.ecommerce.user.service.impl;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.entity.UserAddress;
import com.ecommerce.user.repository.UserAddressRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final UserAddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(UserAddressRepository addressRepository,
                             UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressDTO createAddress(String userId, AddressDTO addressDTO) {
        log.info("Creating address for user: {}", userId);

        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String addressId = UUIDUtil.generateUUID();
        UserAddress address = UserAddress.builder()
            .id(addressId)
            .userId(userId)
            .addressType(addressDTO.getAddressType())
            .street(addressDTO.getStreet())
            .city(addressDTO.getCity())
            .state(addressDTO.getState())
            .zipCode(addressDTO.getZipCode())
            .country(addressDTO.getCountry())
            .isDefault(addressDTO.getIsDefault() != null && addressDTO.getIsDefault())
            .recipientName(addressDTO.getRecipientName())
            .phone(addressDTO.getPhone())
            .build();

        address = addressRepository.save(address);

        return mapToDTO(address);
    }

    @Override
    public AddressDTO updateAddress(String userId, String addressId, AddressDTO addressDTO) {
        log.info("Updating address {} for user: {}", addressId, userId);

        UserAddress address = addressRepository.findByIdAndUserId(addressId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setAddressType(addressDTO.getAddressType());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setRecipientName(addressDTO.getRecipientName());
        address.setPhone(addressDTO.getPhone());

        address = addressRepository.save(address);

        return mapToDTO(address);
    }

    @Override
    public void deleteAddress(String userId, String addressId) {
        log.info("Deleting address {} for user: {}", addressId, userId);

        UserAddress address = addressRepository.findByIdAndUserId(addressId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        addressRepository.delete(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDTO getAddress(String userId, String addressId) {
        UserAddress address = addressRepository.findByIdAndUserId(addressId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        return mapToDTO(address);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<AddressDTO> getUserAddresses(String userId, Pageable pageable) {
        Page<UserAddress> page = addressRepository.findByUserId(userId, pageable);

        return PaginationResponse.<AddressDTO>builder()
            .content(page.getContent().stream().map(this::mapToDTO).toList())
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
    }

    @Override
    public void setDefaultAddress(String userId, String addressId) {
        log.info("Setting default address {} for user: {}", addressId, userId);

        addressRepository.findByUserIdAndIsDefaultTrue(userId)
            .ifPresent(address -> {
                address.setIsDefault(false);
                addressRepository.save(address);
            });

        UserAddress address = addressRepository.findByIdAndUserId(addressId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setIsDefault(true);
        addressRepository.save(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDTO getDefaultAddress(String userId) {
        UserAddress address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
            .orElseThrow(() -> new ResourceNotFoundException("No default address found"));

        return mapToDTO(address);
    }

    private AddressDTO mapToDTO(UserAddress address) {
        return AddressDTO.builder()
            .id(address.getId())
            .addressType(address.getAddressType())
            .street(address.getStreet())
            .city(address.getCity())
            .state(address.getState())
            .zipCode(address.getZipCode())
            .country(address.getCountry())
            .isDefault(address.getIsDefault())
            .recipientName(address.getRecipientName())
            .phone(address.getPhone())
            .build();
    }
}
