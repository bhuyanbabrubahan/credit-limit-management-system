package com.example.creditlimit.mapper;

import com.example.creditlimit.dto.*;
import com.example.creditlimit.entity.*;

import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public AccountResponse account(UserAccount u) {

        return new AccountResponse(
                u.getId(),
                u.getUsername(),
                u.getFullName(),
                u.getRole().name(),
                u.getParent() == null ? null : u.getParent().getId(),
                u.getCreditLimit(),
                u.getAllocatedToChildren(),
                u.getUsedLimit(),
                u.getAvailableLimit(),
                u.getActive(),
                u.getEffectiveFrom());
    }

    public CustomerResponse customer(Customer c) {

        return new CustomerResponse(
                c.getId(),
                c.getCustomerCode(),
                c.getFullName(),
                c.getEmail(),
                c.getMobile(),
                c.getDateOfBirth(),
                c.getAddress(),
                c.getStatus().name());
    }

    public PolicyResponse policy(Policy p) {

        return new PolicyResponse(
                p.getId(),
                p.getPolicyNumber(),
                p.getQuotationNumber(),
                p.getAcdNumber(),
                p.getAgent().getId(),
                p.getAgent().getFullName(),
                p.getCustomer().getId(),
                p.getCustomer().getCustomerCode(),
                p.getCustomer().getFullName(),
                p.getPremiumAmount(),
                p.getPaidAmount(),
                p.getOutstanding(),
                p.getPolicyStartDate(),
                p.getPolicyEndDate(),
                p.getNextDueDate(),
                p.getStatus().name());
    }

    public TransactionResponse transaction(PaymentTransaction t) {

        return new TransactionResponse(
                t.getTransactionId(),
                t.getPolicy().getPolicyNumber(),
                t.getCustomer().getCustomerCode(),
                t.getAgent().getId(),
                t.getAmount(),
                t.getPaidAmount(),
                t.getStatus().name(),
                t.getGatewayReference(),
                t.getFailureReason(),
                t.getPaymentDate());
    }
}