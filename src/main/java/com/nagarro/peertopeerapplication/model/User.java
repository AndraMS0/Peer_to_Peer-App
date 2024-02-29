package com.nagarro.peertopeerapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

@Entity
@Table(name= "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "savings_group_id")
    private SavingsGroup savingsGroup;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;


    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    public User( String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){
        this.username = "DefaultUser";
        this.password = "DefaultPassword0";
    }

    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
