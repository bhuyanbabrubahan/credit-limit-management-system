package com.example.creditlimit.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.creditlimit.dto.CreateCustomerRequest;
import com.example.creditlimit.dto.CustomerResponse;
import com.example.creditlimit.entity.Customer;
import com.example.creditlimit.exception.BusinessException;
import com.example.creditlimit.mapper.DtoMapper;
import com.example.creditlimit.repository.CustomerRepository;
import com.example.creditlimit.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customers;
    private final DtoMapper mapper;

    public CustomerServiceImpl(
            CustomerRepository customers,
            DtoMapper mapper) {

        this.customers = customers;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CustomerResponse create(CreateCustomerRequest r) {

        if (customers.findByCustomerCode(r.customerCode()).isPresent())
            throw new BusinessException("Customer code already exists");

        Customer c = new Customer();
        c.setCustomerCode(r.customerCode());
        c.setFullName(r.fullName());
        c.setEmail(r.email());
        c.setMobile(r.mobile());
        c.setDateOfBirth(r.dateOfBirth());
        c.setAddress(r.address()); 

        return mapper.customer(customers.save(c));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(Long id) {

		var c = customers.findById(id)
				.orElseThrow(() -> new BusinessException("Customer not found: " + id, HttpStatus.NOT_FOUND));
		
		return mapper.customer(c);
    } 

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> all() {

        return customers.findAll()
                .stream()
                .map(mapper::customer)
                .toList();
    }
}