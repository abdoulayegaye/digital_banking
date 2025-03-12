package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.User;

import java.util.List;

public interface IUser {
    public boolean createUser(User user);
    public List<User> getAllUsers();
    public User getUserByUsername(String username);
}
