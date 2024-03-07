package com.nagarro.peertopeerapplication.repositories;

import com.nagarro.peertopeerapplication.model.SavingsGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsGroupRepository extends JpaRepository<SavingsGroup, Long> {

}
