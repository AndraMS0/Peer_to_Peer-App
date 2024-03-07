package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.model.SavingGoal;
import com.nagarro.peertopeerapplication.model.SavingsGroup;
import com.nagarro.peertopeerapplication.repositories.SavingGoalRepository;
import com.nagarro.peertopeerapplication.repositories.SavingsGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SavingsGroupService {
    private final SavingsGroupRepository savingsGroupRepository;
    private final SavingGoalRepository savingGoalRepository;

    @Autowired
    public SavingsGroupService(SavingsGroupRepository savingsGroupRepository, SavingGoalRepository savingGoalRepository) {
        this.savingsGroupRepository = savingsGroupRepository;
        this.savingGoalRepository = savingGoalRepository;
    }

    @Transactional
    public SavingsGroup createSavingsGroup(SavingsGroup group) {
        return savingsGroupRepository.save(group);
    }

    public SavingGoal addSavingGoalToGroup(Long groupId, SavingGoal goal) {
        Optional<SavingsGroup> savingsGroupOptional = savingsGroupRepository.findById(groupId);
        if (!savingsGroupOptional.isPresent()) {
            throw new RuntimeException("SavingsGroup not found for id " + groupId); // Consider a more specific exception
        }
        SavingsGroup savingsGroup = savingsGroupOptional.get();
        goal.setSavingsGroup(savingsGroup);
        savingsGroup.getSavingGoals().add(goal);
        return savingGoalRepository.save(goal);
    }

    public Optional<SavingsGroup> getSavingsGroupById(Long id) {
        return savingsGroupRepository.findById(id);
    }

    @Transactional
    public void deleteSavingsGroup(Long id) {
        savingsGroupRepository.deleteById(id);
    }
}
