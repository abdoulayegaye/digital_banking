package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.User;

import java.util.List;

public interface IUser {
    boolean createUser(User user);
    List<User> getAllUsers();
    User getUserByUsername(String username);
}