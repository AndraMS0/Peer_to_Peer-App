package com.nagarro.peertopeerapplication.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_transaction_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private float amount;
    private float exchangeRate;

    public void executeTransaction(){
    }

    public Long getTransactionId() {
        return id;
    }

    public void setTransactionId(Long transactionId) {
        this.id = transactionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
