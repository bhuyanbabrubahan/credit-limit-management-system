package com.example.creditlimit.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment_transactions",
        indexes = {
                @Index(
                        name = "uk_transaction_id",
                        columnList = "transaction_id",
                        unique = true),
                @Index(
                        name = "uk_idempotency_key",
                        columnList = "idempotency_key",
                        unique = true),
                @Index(
                        name = "idx_payment_policy",
                        columnList = "policy_id"),
                @Index(
                        name = "idx_payment_customer",
                        columnList = "customer_id")
        })
public class PaymentTransaction extends BaseEntity {

    @Column(
            name = "transaction_id",
            nullable = false,
            unique = true,
            length = 60)
    private String transactionId;

    @Column(
            name = "idempotency_key",
            nullable = false,
            unique = true,
            length = 100)
    private String idempotencyKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent_id")
    private UserAccount agent;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(length = 100)
    private String gatewayReference;

    @Column(length = 500)
    private String failureReason;

    private LocalDateTime paymentDate;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public UserAccount getAgent() {
        return agent;
    }

    public void setAgent(UserAccount agent) {
        this.agent = agent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getGatewayReference() {
        return gatewayReference;
    }

    public void setGatewayReference(String gatewayReference) {
        this.gatewayReference = gatewayReference;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

	@Override
	public String toString() {
		return "PaymentTransaction [transactionId=" + transactionId + ", idempotencyKey=" + idempotencyKey + ", policy="
				+ policy + ", customer=" + customer + ", agent=" + agent + ", amount=" + amount + ", paidAmount="
				+ paidAmount + ", status=" + status + ", gatewayReference=" + gatewayReference + ", failureReason="
				+ failureReason + ", paymentDate=" + paymentDate + "]";
	}
    
}