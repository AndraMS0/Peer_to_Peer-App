package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.SavingGoal;
import com.nagarro.peertopeerapplication.model.SavingsGroup;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.SavingGoalRepository;
import com.nagarro.peertopeerapplication.repositories.SavingsGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        if (savingsGroupOptional.isEmpty()) {
            throw new RuntimeException("SavingsGroup not found for id " + groupId);
        }
        SavingsGroup savingsGroup = savingsGroupOptional.get();
        goal.setSavingsGroup(savingsGroup);
        savingsGroup.getSavingGoals().add(goal);
        return savingGoalRepository.save(goal);
    }

    public void addMemberToTheGroup(Long groupId, UserDTO newMember){
        Optional<SavingsGroup> savingsGroup =  savingsGroupRepository.findById(groupId);
        if(savingsGroup.isEmpty()){
            throw new RuntimeException("SavingsGroup not found");
        }
        SavingsGroup group = savingsGroup.get();
        User user = new User(newMember.getUsername(), newMember.getPassword());
        group.addMember(user);
    }

    public List<UserDTO> getAllGroupMembers(Long groupId){
        Optional<SavingsGroup> savingsGroup =  savingsGroupRepository.findById(groupId);
        if(savingsGroup.isEmpty()){
            throw new RuntimeException("SavingsGroup not found");
        }
        SavingsGroup group = savingsGroup.get();
         List<User> members = group.getMembers();
         List<UserDTO> membersDTO = new ArrayList<>();
         for(User member :  members) {
           membersDTO.add(new UserDTO(member.getUsername(), member.getPassword()));
         }
         return membersDTO;
    }



    public Optional<SavingsGroup> getSavingsGroupById(Long id) {
        return savingsGroupRepository.findById(id);
    }

    @Transactional
    public void deleteSavingsGroup(Long id) {
        savingsGroupRepository.deleteById(id);
    }
}
