package com.nagarro.peertopeerapplication.model;

public class Account {
    private String accountId;
    private float balance;
    private String currency;
    private String ownerId;

    public Account(String accountId, float balance, String currency, String ownerId) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
        this.ownerId = ownerId;
    }

    public Account() {
        this.balance = 0;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
