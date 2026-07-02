package com.ecommerce.user.service;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.user.dto.AddressDTO;
import org.springframework.data.domain.Pageable;

public interface AddressService {

    AddressDTO createAddress(String userId, AddressDTO addressDTO);

    AddressDTO updateAddress(String userId, String addressId, AddressDTO addressDTO);

    void deleteAddress(String userId, String addressId);

    AddressDTO getAddress(String userId, String addressId);

    PaginationResponse<AddressDTO> getUserAddresses(String userId, Pageable pageable);

    void setDefaultAddress(String userId, String addressId);

    AddressDTO getDefaultAddress(String userId);
}
