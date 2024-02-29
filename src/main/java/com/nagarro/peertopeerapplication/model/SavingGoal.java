package com.nagarro.peertopeerapplication.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "saving_goals")
public class SavingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String purpose;
    private float targetAmount;

    @ManyToOne
    @JoinColumn(name = "saving_goal_on_group_id")
    private SavingsGroup savingsGroup;

    @Temporal(TemporalType.DATE)
    private Date deadline;

    public void addFunds(float amount) {
    }

    public float checkProgress() {
        return 0;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurpose() {
        return purpose;
    }

    public float getTargetAmount() {
        return targetAmount;
    }

    public Date getDeadline() {
        return deadline;
    }
}
