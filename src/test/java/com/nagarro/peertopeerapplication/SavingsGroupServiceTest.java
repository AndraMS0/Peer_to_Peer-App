package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.model.SavingGoal;
import com.nagarro.peertopeerapplication.model.SavingsGroup;
import com.nagarro.peertopeerapplication.repositories.SavingGoalRepository;
import com.nagarro.peertopeerapplication.repositories.SavingsGroupRepository;
import com.nagarro.peertopeerapplication.services.SavingsGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SavingsGroupServiceTest {

    @Mock
    private SavingsGroupRepository savingsGroupRepository;

    @Mock
    private SavingGoalRepository savingGoalRepository;

    @InjectMocks
    private SavingsGroupService savingsGroupService;


    @Test
    public void createSavingsGroupTest() {
        SavingsGroup mockGroup = new SavingsGroup();
        when(savingsGroupRepository.save(any(SavingsGroup.class))).thenReturn(mockGroup);

        SavingsGroup result = savingsGroupService.createSavingsGroup(mockGroup);

        assertNotNull(result);
        verify(savingsGroupRepository).save(any(SavingsGroup.class));
    }

    @Test
    public void addSavingGoalToGroupTest() {
        Long groupId = 1L;
        SavingGoal mockGoal = new SavingGoal();
        SavingsGroup mockGroup = new SavingsGroup();

        when(savingsGroupRepository.findById(groupId)).thenReturn(Optional.of(mockGroup));
        when(savingGoalRepository.save(any(SavingGoal.class))).thenReturn(mockGoal);

        SavingGoal result = savingsGroupService.addSavingGoalToGroup(groupId, mockGoal);

        assertNotNull(result);
        assertEquals(mockGroup, result.getSavingsGroup());
        verify(savingGoalRepository).save(mockGoal);

    }

    @Test
    public void addSavingGoalToGroup_throwsExceptionTest() {
        Long groupId = 1L;
        SavingGoal mockGoal = new SavingGoal();
        when(savingsGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> savingsGroupService.addSavingGoalToGroup(groupId, mockGoal));
        String expectedMessage = "SavingsGroup not found for id " + groupId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getSavingsGroupByIdTest() {
        Long groupId = 1L;
        Optional<SavingsGroup> mockGroup = Optional.of(new SavingsGroup());
        when(savingsGroupRepository.findById(groupId)).thenReturn(mockGroup);

        Optional<SavingsGroup> result = savingsGroupService.getSavingsGroupById(groupId);

        assertTrue(result.isPresent());
        verify(savingsGroupRepository).findById(groupId);
    }

    @Test
    void deleteSavingsGroupTest() {
        Long groupId = 1L;

        savingsGroupService.deleteSavingsGroup(groupId);

        verify(savingsGroupRepository).deleteById(groupId);
    }

}
