package com.nagarro.peertopeerapplication.repositories;

import com.nagarro.peertopeerapplication.model.User;
<<<<<<< Updated upstream
import jakarta.persistence.EntityManager;
=======
>>>>>>> Stashed changes
import org.springframework.stereotype.Repository;

@Repository
public class GenericUserRepository extends AbstractJpaDAO<User>{

    public GenericUserRepository(){
        this.setClazz(User.class);
    }

    public User findByUsername(String username) {
        return getEntityManager().createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }


}
