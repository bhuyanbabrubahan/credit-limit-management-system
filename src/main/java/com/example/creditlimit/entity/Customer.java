package com.example.creditlimit.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "customers",
        indexes = {
                @Index(
                        name = "uk_customer_code",
                        columnList = "customer_code",
                        unique = true),
                @Index(
                        name = "idx_customer_mobile",
                        columnList = "mobile")
        })
public class Customer extends BaseEntity {

    @Column(
            name = "customer_code",
            nullable = false,
            unique = true,
            length = 30)
    private String customerCode;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 20)
    private String mobile;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 250)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomerStatus status = CustomerStatus.ACTIVE;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }
}