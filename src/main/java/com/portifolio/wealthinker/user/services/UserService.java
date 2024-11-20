package com.portifolio.wealthinker.user.services;

import java.util.Optional;

import com.portifolio.wealthinker.user.models.User;

public interface UserService {
    Optional<User> getUserById(String id);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    User saveUser(User user);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    boolean isUserExistByUsername(String username);
    boolean isUserExistByPhoneNumber(String phoneNumber);
}
