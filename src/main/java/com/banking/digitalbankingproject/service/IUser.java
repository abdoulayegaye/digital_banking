package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.User;

public interface IUser {
    User getUserByUsername(String username);
    boolean createUser(User user);
}