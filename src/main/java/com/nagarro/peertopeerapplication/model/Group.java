package com.nagarro.peertopeerapplication.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

import java.util.List;

@MappedSuperclass
public abstract class Group {

    @Id
    protected Long id;
    @OneToMany(mappedBy = "savingsGroup")
    protected List<User> members;

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }
}
