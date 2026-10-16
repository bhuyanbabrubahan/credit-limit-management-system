package com.example.creditlimit.service.impl;

import com.example.creditlimit.dto.*;
import com.example.creditlimit.entity.*;
import com.example.creditlimit.exception.BusinessException;
import com.example.creditlimit.gateway.*;
import com.example.creditlimit.mapper.DtoMapper;
import com.example.creditlimit.repository.*;
import com.example.creditlimit.service.CreditService;
import com.example.creditlimit.warehouse.DataWarehousePublisher;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CreditServiceImpl implements CreditService {

    private final UserAccountRepository users;
    private final CustomerRepository customers;
    private final PolicyRepository policies;
    private final PaymentTransactionRepository transactions;
    private final DtoMapper mapper;
    private final PaymentGateway paymentGateway;
    private final DataWarehousePublisher warehouse;

    public CreditServiceImpl(
            UserAccountRepository users,
            CustomerRepository customers,
            PolicyRepository policies,
            PaymentTransactionRepository transactions,
            DtoMapper mapper,
            PaymentGateway paymentGateway,
            DataWarehousePublisher warehouse) {

        this.users = users;
        this.customers = customers;
        this.policies = policies;
        this.transactions = transactions;
        this.mapper = mapper;
        this.paymentGateway = paymentGateway;
        this.warehouse = warehouse;
    }

    private UserAccount user(Long id) {
        return users.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found: " + id,
                                HttpStatus.NOT_FOUND));
    }

    private UserAccount lockedUser(Long id) {
        return users.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found: " + id,
                                HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public AccountResponse createParent(CreateParentRequest r) {

        if (users.findByUsername(r.username()).isPresent())
            throw new BusinessException("Username already exists");

        UserAccount p = new UserAccount();
        p.setUsername(r.username());
        p.setFullName(r.fullName());
        p.setRole(Role.PARENT);
        p.setCreditLimit(r.creditLimit());
        p.setEffectiveFrom(r.effectiveFrom());

        System.out.println("Parent before save: " + p);
        return mapper.account(users.save(p));
    }

    @Override
    @Transactional
    public AccountResponse createChild(CreateChildRequest r) {

        if (users.findByUsername(r.username()).isPresent())
            throw new BusinessException("Username already exists");

        UserAccount parent = lockedUser(r.parentId());

        if (parent.getRole() != Role.PARENT)
            throw new BusinessException("Selected account is not a parent");

        BigDecimal remainingAllocation =
                parent.getCreditLimit().subtract(parent.getAllocatedToChildren());

        if (r.creditLimit().compareTo(remainingAllocation) > 0)
            throw new BusinessException(
                    "Child allocation exceeds parent's remaining allocation: "
                            + remainingAllocation);

        UserAccount child = new UserAccount();
        child.setUsername(r.username());
        child.setFullName(r.fullName());
        child.setRole(Role.CHILD);
        child.setParent(parent);
        child.setCreditLimit(r.creditLimit());
        child.setEffectiveFrom(parent.getEffectiveFrom());

        System.out.println("Child before save: " + child);
        
        parent.setAllocatedToChildren(
                parent.getAllocatedToChildren().add(r.creditLimit()));

        users.save(parent);

        return mapper.account(users.save(child));
    }

    @Override
    @Transactional(readOnly = true)
    public CreditResponse getCredit(Long id) {

        UserAccount u = user(id);

        System.out.println("User account: " + u);
        
        return new CreditResponse(
                u.getId(),
                u.getUsername(),
                u.getRole().name(),
                u.getCreditLimit(),
                u.getAllocatedToChildren(),
                u.getUsedLimit(),
                u.getAvailableLimit());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getChildren(Long parentId) {

        UserAccount p = user(parentId);

        if (p.getRole() != Role.PARENT)
            throw new BusinessException("Selected account is not a parent");

        return users.findByParentId(parentId)
                .stream()
                .map(mapper::account)
                .toList();
    }

    @Override
    @Transactional
    public PolicyResponse bookPolicy(BookPolicyRequest r) {

        UserAccount agent = lockedUser(r.agentId());

        if (agent.getRole() != Role.CHILD)
            throw new BusinessException(
                    "Only a child/agent can book in this demo");

        if (!Boolean.TRUE.equals(agent.getActive()))
            throw new BusinessException("Agent is inactive");

        Customer customer = customers.findById(r.customerId())
                .orElseThrow(() ->
                        new BusinessException(
                                "Customer not found: " + r.customerId(),
                                HttpStatus.NOT_FOUND));

        System.out.println("Customer: " + customer);
        System.out.println("Agent: " + agent);
        
        if (r.policyEndDate().isBefore(r.policyStartDate()))
            throw new BusinessException(
                    "Policy end date cannot be before start date");

        if (r.nextDueDate().isBefore(r.policyStartDate())
                || r.nextDueDate().isAfter(r.policyEndDate()))
            throw new BusinessException(
                    "Next due date must be inside policy period");

        BigDecimal available = agent.getAvailableLimit();

        if (r.premiumAmount().compareTo(available) > 0)
            throw new BusinessException(
                    "Insufficient available credit. Available: " + available);

        if (policies.findByQuotationNumber(r.quotationNumber()).isPresent())
            throw new BusinessException("Quotation number already exists");

        if (policies.findByAcdNumber(r.acdNumber()).isPresent())
            throw new BusinessException("ACD number already exists");

        Policy p = new Policy();

        p.setPolicyNumber(
                "POL-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());

        p.setQuotationNumber(r.quotationNumber());
        p.setAcdNumber(r.acdNumber());
        p.setAgent(agent);
        p.setCustomer(customer);
        p.setPremiumAmount(r.premiumAmount());
        p.setPolicyStartDate(r.policyStartDate());
        p.setPolicyEndDate(r.policyEndDate());
        p.setNextDueDate(r.nextDueDate());
        p.setStatus(PolicyStatus.ACTIVE);

        agent.increaseUsed(r.premiumAmount());

        users.save(agent);

        return mapper.policy(policies.save(p));
    }

    @Override
    @Transactional
    public PaymentResponse payPolicy(PaymentRequest r) {

        Policy policy = policies.findById(r.policyId())
                .orElseThrow(() ->
                        new BusinessException(
                                "Policy not found: " + r.policyId(),
                                HttpStatus.NOT_FOUND));
        System.out.println("Policy: " + policy);
        
		var existing = transactions.findByIdempotencyKey(r.idempotencyKey());

        if (existing.isPresent()) {

            PaymentTransaction old = existing.get();

            return new PaymentResponse(
                    old.getTransactionId(),
                    old.getPolicy().getPolicyNumber(),
                    old.getStatus().name(),
                    old.getAmount(),
                    old.getPaidAmount(),
                    old.getGatewayReference(),
                    "Duplicate request ignored; existing transaction returned",
                    old.getPaymentDate());
        }

        if (policy.getStatus() == PolicyStatus.CANCELLED
                || policy.getStatus() == PolicyStatus.PAID)
            throw new BusinessException(
                    "Policy cannot be paid in its current status: "
                            + policy.getStatus());

        BigDecimal due = policy.getOutstanding();

        if (due.signum() <= 0)
        {
            throw new BusinessException("No outstanding amount");
        }
        PaymentTransaction tx = new PaymentTransaction();

        tx.setTransactionId(
                "TXN-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 10)
                        .toUpperCase());

        tx.setIdempotencyKey(r.idempotencyKey());
        tx.setPolicy(policy);
        tx.setCustomer(policy.getCustomer());
        tx.setAgent(policy.getAgent());
        tx.setAmount(due);
        tx.setPaidAmount(BigDecimal.ZERO);
        tx.setStatus(TransactionStatus.PENDING);
        
        System.out.println("Successful transaction: " + tx);
        
        transactions.save(tx);

        PaymentGatewayResult gateway = paymentGateway.pay(
                new PaymentGatewayRequest(
                        policy.getQuotationNumber(),
                        due,
                        policy.getCustomer().getCustomerCode()));

        if (!gateway.success()) {

            tx.setStatus(TransactionStatus.FAILED);
            tx.setFailureReason(gateway.failureReason());

            transactions.save(tx);
            warehouse.publishPaymentEvent(tx);

            return new PaymentResponse(
                    tx.getTransactionId(),
                    policy.getPolicyNumber(),
                    "FAILED",
                    due,
                    BigDecimal.ZERO,
                    null,
                    "Payment failed: " + gateway.failureReason(),
                    null);
        }

        UserAccount agent = lockedUser(policy.getAgent().getId());

        policy.setPaidAmount(
                policy.getPaidAmount().add(due));

        policy.setStatus(
                policy.getOutstanding().signum() == 0
                        ? PolicyStatus.PAID
                        : PolicyStatus.PARTIALLY_PAID);

        policy.getAgent().decreaseUsed(due);

        tx.setPaidAmount(due);
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setGatewayReference(gateway.gatewayReference());
        tx.setPaymentDate(LocalDateTime.now());

        policies.save(policy);
        users.save(agent);
        transactions.save(tx);

        warehouse.publishPaymentEvent(tx);

        return new PaymentResponse(
                tx.getTransactionId(),
                policy.getPolicyNumber(),
                "SUCCESS",
                due,
                due,
                tx.getGatewayReference(),
                "Payment successful",
                tx.getPaymentDate());
    }

	@Override
	@Transactional(readOnly = true)
	public List<AccountResponse> getAllAccounts() {
		
		return users.findAll()
				.stream()
				.map(mapper::account)
				.toList();
	}	
}