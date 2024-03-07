package com.nagarro.peertopeerapplication.model;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    private LocalDate deadline;

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

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setSavingsGroup(SavingsGroup savingsGroup) {
        this.savingsGroup = savingsGroup;
    }

    public SavingsGroup getSavingsGroup() {
        return this.savingsGroup;
    }
}
