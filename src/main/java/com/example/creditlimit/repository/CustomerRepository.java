package com.example.creditlimit.repository;

import com.example.creditlimit.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByCustomerCode(String code);
}
