package com.nagarro.peertopeerapplication.repositories;

import com.nagarro.peertopeerapplication.model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, Long> {
}
