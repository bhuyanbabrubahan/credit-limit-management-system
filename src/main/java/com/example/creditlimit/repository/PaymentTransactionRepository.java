package com.example.creditlimit.repository;

import com.example.creditlimit.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
	Optional<PaymentTransaction> findByIdempotencyKey(String key);

	List<PaymentTransaction> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

	List<PaymentTransaction> findByAgentIdOrderByCreatedAtDesc(Long agentId);
}
