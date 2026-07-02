package com.ecommerce.payment.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends BaseRepository<Payment, String> {
    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findByTransactionId(String transactionId);
    Page<Payment> findByStatus(String status, Pageable pageable);
}
