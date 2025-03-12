package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.tools.Utils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserImpl implements IUser {

    private static final Logger logger = Logger.getLogger(UserImpl.class.getName());
    private Db db = new Db();
    private int ok;

    @Override
    public boolean createUser(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            logger.warning("Le nom d'utilisateur est invalide");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.warning("Le mot de passe est invalide");
            return false;
        }
        if (user.getNom() == null || user.getNom().isEmpty()) {
            logger.warning("Le nom est invalide");
            return false;
        }
        if (user.getPrenom() == null || user.getPrenom().isEmpty()) {
            logger.warning("Le prénom est invalide");
            return false;
        }

        String sql = "INSERT INTO users (username, password, nom, prenom, role) VALUES(?, ?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, user.getUsername());
            db.getPstm().setString(2, Utils.hashPassword(user.getPassword()));
            db.getPstm().setString(3, user.getNom());
            db.getPstm().setString(4, user.getPrenom());
            db.getPstm().setString(5, user.getRole() != null ? user.getRole() : "USER");
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY username ASC";
        List<User> users = new ArrayList<>();
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, username);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setRole(rs.getString("role"));
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return user;
    }
    
    @Override
    public boolean authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        
        // Vérifier si le mot de passe correspond en utilisant la méthode checkPassword
        try {
            return Utils.checkPassword(password, user.getPassword());
        } catch (Exception e) {
            logger.severe("Erreur lors de l'authentification : " + e.getMessage());
            return false;
        }
    }
}