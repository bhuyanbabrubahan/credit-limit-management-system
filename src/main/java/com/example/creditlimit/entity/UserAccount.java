package com.example.creditlimit.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "user_accounts",
        indexes = {
                @Index(
                        name = "idx_user_parent",
                        columnList = "parent_id"),
                @Index(
                        name = "uk_user_username",
                        columnList = "username",
                        unique = true)
        })
public class UserAccount extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /**
     * A child belongs to one parent. A parent has many children. This is the
     * business hierarchy used for credit allocation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private UserAccount parent;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedToChildren = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal usedLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private LocalDate effectiveFrom;

    public BigDecimal getAvailableLimit() {
        return creditLimit
                .subtract(usedLimit)
                .max(BigDecimal.ZERO);
    }

    public void increaseUsed(BigDecimal amount) {
        usedLimit = usedLimit.add(amount);
    }

    public void decreaseUsed(BigDecimal amount) {
        usedLimit = usedLimit
                .subtract(amount)
                .max(BigDecimal.ZERO);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserAccount getParent() {
        return parent;
    }

    public void setParent(UserAccount parent) {
        this.parent = parent;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAllocatedToChildren() {
        return allocatedToChildren;
    }

    public void setAllocatedToChildren(BigDecimal allocatedToChildren) {
        this.allocatedToChildren = allocatedToChildren;
    }

    public BigDecimal getUsedLimit() {
        return usedLimit;
    }

    public void setUsedLimit(BigDecimal usedLimit) {
        this.usedLimit = usedLimit;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}