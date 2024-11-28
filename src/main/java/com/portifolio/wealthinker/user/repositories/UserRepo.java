package com.portifolio.wealthinker.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.user.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{

    // Optional<User> findById(String id);

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumberAndPassword(String phoneNumber, String password);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
}
