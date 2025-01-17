package com.portifolio.wealthinker.user.servicesImpl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.exceptions.ResourceNotFoundException;
import com.portifolio.wealthinker.user.models.Role;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.repositories.RoleRepo;
import com.portifolio.wealthinker.user.repositories.UserRepo;
import com.portifolio.wealthinker.user.services.UserService;
import com.portifolio.wealthinker.utils.AppConstants;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        // set the user role
        // Fetch default role
        Role defaultRole = roleRepo.findByName(AppConstants.ROLE_USER).orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        user.setRoles(Set.of(defaultRole));

        // user Id have to be generate
        user.setId(UUID.randomUUID().toString());
        // pasword encoding
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User save = userRepo.save(user);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepo.findById(userId).orElse(null);
        return user2 != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user2 = userRepo.findByEmail(email).orElse(null);
        return user2 != null ? true : false;
    }

    @Override
    public boolean isUserExistByUsername(String username) {
        User user2 = userRepo.findByUsername(username).orElse(null);
        return user2 != null ? true : false;
    }

    @Override
    public boolean isUserExistByPhoneNumber(String phoneNumber) {
        User user2 = userRepo.findByPhoneNumber(phoneNumber).orElse(null);
        return user2 != null ? true : false;
    }

    @Override
    public boolean isUserExists(String username, String email, String phoneNumber) {
        return userRepo.existsByUsername(username) || 
               userRepo.existsByEmail(email) || 
               userRepo.existsByPhoneNumber(phoneNumber);
    }

}
