package com.nagarro.peertopeerapplication.dto;

import java.math.BigInteger;

public class AccountDTO {

    private Long id;
    private BigInteger balance;
    private String currency;
    private Long ownerId;

    public AccountDTO(Long id, BigInteger balance, String currency, Long ownerId) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
