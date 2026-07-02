package com.ecommerce.payment.repository;

import com.ecommerce.payment.entity.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {
    List<PaymentHistory> findByPaymentIdOrderByTimestampDesc(String paymentId);
    Page<PaymentHistory> findByPaymentId(String paymentId, Pageable pageable);
}
