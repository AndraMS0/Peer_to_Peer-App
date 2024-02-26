package com.nagarro.peertopeerapplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "savings_groups")
public class SavingsGroup extends Group {

    @OneToMany(mappedBy = "savingsGroup")
    private List<SavingGoal> savingGoals;

    public SavingsGroup() {
        super();
    }
}
