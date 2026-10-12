package com.example.creditlimit.controller;

import com.example.creditlimit.dto.PolicyResponse;
import com.example.creditlimit.service.PolicyService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService service;

    public PolicyController(PolicyService service) {
        this.service = service;
    }

    @GetMapping("/{policyId}")
    public PolicyResponse get(@PathVariable Long policyId) {
        return service.get(policyId);
    }

    @GetMapping("/agent/{agentId}")
    public List<PolicyResponse> byAgent(@PathVariable Long agentId) {
        return service.byAgent(agentId);
    }

    @GetMapping("/customer/{customerId}")
    public List<PolicyResponse> byCustomer(@PathVariable Long customerId) {
        return service.byCustomer(customerId);
    }
}