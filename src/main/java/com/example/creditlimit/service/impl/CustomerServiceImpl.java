package com.example.creditlimit.service.impl;

import com.example.creditlimit.dto.*;
import com.example.creditlimit.entity.Customer;
import com.example.creditlimit.exception.BusinessException;
import com.example.creditlimit.mapper.DtoMapper;
import com.example.creditlimit.repository.CustomerRepository;
import com.example.creditlimit.service.CustomerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        return mapper.customer(
                customers.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Customer not found: " + id,
                                        org.springframework.http.HttpStatus.NOT_FOUND)));
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