package com.example.creditlimit.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "policies",
        indexes = {
                @Index(
                        name = "idx_policy_agent",
                        columnList = "agent_id"),
                @Index(
                        name = "idx_policy_customer",
                        columnList = "customer_id"),
                @Index(
                        name = "idx_policy_status",
                        columnList = "status"),
                @Index(
                        name = "uk_policy_number",
                        columnList = "policy_number",
                        unique = true),
                @Index(
                        name = "uk_quotation_number",
                        columnList = "quotation_number",
                        unique = true),
                @Index(
                        name = "uk_acd_number",
                        columnList = "acd_number",
                        unique = true)
        })
public class Policy extends BaseEntity {

    @Column(
            name = "policy_number",
            nullable = false,
            unique = true,
            length = 40)
    private String policyNumber;

    @Column(
            name = "quotation_number",
            nullable = false,
            unique = true,
            length = 40)
    private String quotationNumber;

    @Column(
            name = "acd_number",
            nullable = false,
            unique = true,
            length = 40)
    private String acdNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent_id")
    private UserAccount agent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate policyStartDate;

    @Column(nullable = false)
    private LocalDate policyEndDate;

    @Column(nullable = false)
    private LocalDate nextDueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private PolicyStatus status = PolicyStatus.ACTIVE;

    public BigDecimal getOutstanding() {
        return premiumAmount
                .subtract(paidAmount)
                .max(BigDecimal.ZERO);
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public String getAcdNumber() {
        return acdNumber;
    }

    public void setAcdNumber(String acdNumber) {
        this.acdNumber = acdNumber;
    }

    public UserAccount getAgent() {
        return agent;
    }

    public void setAgent(UserAccount agent) {
        this.agent = agent;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public LocalDate getPolicyStartDate() {
        return policyStartDate;
    }

    public void setPolicyStartDate(LocalDate policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public LocalDate getPolicyEndDate() {
        return policyEndDate;
    }

    public void setPolicyEndDate(LocalDate policyEndDate) {
        this.policyEndDate = policyEndDate;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }
}