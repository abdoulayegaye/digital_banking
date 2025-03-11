package com.example.projet_java_fx.service;

import com.example.projet_java_fx.entity.Users;

import java.util.List;

public interface IUser {
    public Users Login(String email, String password);
}
