package com.nagarro.peertopeerapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @NotNull
    @Size(min = 3, max = 50)
    private String Id;

    @PositiveOrZero
    private float balance;
    @NotBlank
    private String currency;

    @NotNull
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "user_accout_id")
    private User user;


    public Account() {
        this.balance = 0;
    }

    public String getAccountId() {
        return Id;
    }

    public void setAccountId(String accountId) {
        this.Id = accountId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
