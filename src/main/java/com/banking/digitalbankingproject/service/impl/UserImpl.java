package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.tools.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserImpl implements IUser {

    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO users VALUES(NULL, ?, ?)";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, user.getUsername());
            db.getPstm().setString(2, Utils.hashPassword(user.getPassword()));
            ok = db.executeMaj();
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok == 1;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY username ASC";
        List<User> users = new ArrayList<User>();
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
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
            rs = db.executeSelect();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateUserPassword(User user) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, user.getPassword());
            db.getPstm().setString(2, user.getUsername());
            ok = db.executeMaj();
            db.closeConnection();
            if (ok != 1) {
                throw new RuntimeException("La mise à jour du mot de passe a échoué");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la mise à jour du mot de passe", e);
        }
    }
}
