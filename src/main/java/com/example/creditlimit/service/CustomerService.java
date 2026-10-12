package com.example.creditlimit.service;
import com.example.creditlimit.dto.*;
import java.util.List;
public interface CustomerService {
    CustomerResponse create(CreateCustomerRequest request);
    CustomerResponse get(Long id);
    List<CustomerResponse> all();
}
