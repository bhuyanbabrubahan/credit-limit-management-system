package com.example.creditlimit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creditlimit.dto.AccountResponse;
import com.example.creditlimit.dto.BookPolicyRequest;
import com.example.creditlimit.dto.CreateChildRequest;
import com.example.creditlimit.dto.CreateParentRequest;
import com.example.creditlimit.dto.CreditResponse;
import com.example.creditlimit.dto.PaymentRequest;
import com.example.creditlimit.dto.PaymentResponse;
import com.example.creditlimit.dto.PolicyResponse;
import com.example.creditlimit.service.CreditService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CreditController {

    private final CreditService credit;

    public CreditController(CreditService credit) {
        this.credit = credit;
    }

    @PostMapping("/accounts/parents")
    public ResponseEntity<AccountResponse> createParent(
            @Valid @RequestBody CreateParentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(credit.createParent(request));
    }

    @PostMapping("/accounts/children")
    public ResponseEntity<AccountResponse> createChild(
            @Valid @RequestBody CreateChildRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(credit.createChild(request));
    }

    @GetMapping("/accounts/{userId}/credit")
    public CreditResponse credit(@PathVariable Long userId) {
        return credit.getCredit(userId);
    }

    @GetMapping("/accounts/{parentId}/children")
    public List<AccountResponse> children(@PathVariable Long parentId) {
        return credit.getChildren(parentId);
    }

    @PostMapping("/policies")
    public ResponseEntity<PolicyResponse> book(
            @Valid @RequestBody BookPolicyRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(credit.bookPolicy(request));
    }

    @PostMapping("/payments")
    public PaymentResponse pay(
            @Valid @RequestBody PaymentRequest request) {

        return credit.payPolicy(request);
    }
    
    @GetMapping("/accounts")
    public List<AccountResponse> allAccounts() {
        return credit.getAllAccounts();
    }
}