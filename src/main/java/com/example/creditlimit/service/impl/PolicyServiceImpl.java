package com.example.creditlimit.service.impl;

import com.example.creditlimit.dto.PolicyResponse;
import com.example.creditlimit.exception.BusinessException;
import com.example.creditlimit.mapper.DtoMapper;
import com.example.creditlimit.repository.PolicyRepository;
import com.example.creditlimit.service.PolicyService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policies;
    private final DtoMapper mapper;

    public PolicyServiceImpl(
            PolicyRepository policies,
            DtoMapper mapper) {

        this.policies = policies;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> byAgent(Long agentId) {

    	var policyList = policies.findByAgentIdOrderByCreatedAtDesc(agentId);
    	
		policyList.forEach(policy -> System.out.println("Policy Entity: " + policy));
    	
        return policyList
                .stream()
                .map(mapper::policy)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> byCustomer(Long customerId) {

    	var policyList = policies.findByCustomerIdOrderByCreatedAtDesc(customerId);
    	
    	policyList.forEach(policy -> System.out.println("Customer Policy Entity" + policy));
    	
        return policyList
                .stream()
                .map(mapper::policy)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyResponse get(Long policyId) {

		var policy = policies.findById(policyId)
				.orElseThrow(() -> new BusinessException("Policy not found: " + policyId, HttpStatus.NOT_FOUND));
		
		System.out.println("Policy by ID: " + policy);

		return mapper.policy(policy);
    }
}