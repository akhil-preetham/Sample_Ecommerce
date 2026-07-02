package com.ecommerce.payment.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.payment.entity.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RefundRepository extends BaseRepository<Refund, String> {
    List<Refund> findByPaymentId(String paymentId);
    Optional<Refund> findByTransactionId(String transactionId);
    Page<Refund> findByStatus(String status, Pageable pageable);
}
