package com.example.creditlimit.repository;

import com.example.creditlimit.entity.*;
import org.springframework.data.jpa.repository.*;
import java.util.*;

public interface PolicyRepository extends JpaRepository<Policy, Long>, JpaSpecificationExecutor<Policy> {
	Optional<Policy> findByPolicyNumber(String number);

	Optional<Policy> findByQuotationNumber(String number);

	Optional<Policy> findByAcdNumber(String number);

	List<Policy> findByAgentIdOrderByCreatedAtDesc(Long agentId);

	List<Policy> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
