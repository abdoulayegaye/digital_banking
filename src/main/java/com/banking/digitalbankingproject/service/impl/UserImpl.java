package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.tools.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserImpl implements IUser {

    private static final Logger logger = Logger.getLogger(UserImpl.class.getName());
    private Db db = new Db();

    @Override
    public boolean createUser(User user) {
        if (user == null) {
            logger.warning("Tentative de création d'un utilisateur null.");
            return false;
        }

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, Utils.hashPassword(user.getPassword()));

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la création de l'utilisateur", e);
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY username ASC";
        List<User> users = new ArrayList<>();

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des utilisateurs", e);
        }

        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            logger.warning("Tentative de récupération d'un utilisateur avec un nom d'utilisateur null ou vide.");
            return null;
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération de l'utilisateur par nom d'utilisateur", e);
        }

        return user;
    }
}