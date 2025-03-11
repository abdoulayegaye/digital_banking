package com.example.projet_java_fx.service.impl;

import com.example.projet_java_fx.database.Db;
import com.example.projet_java_fx.entity.Users;
import com.example.projet_java_fx.service.IUser;

import java.sql.ResultSet;

public class UserImpl implements IUser {
    private Db db = new Db();
    private ResultSet rs;

    @Override
    public Users Login(String email, String password) {
        String sql = "select * from users where email = ? and password = ?";
        Users user = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, email);
            db.getPstm().setString(2, password);
            rs = db.executeSelect();
            if (rs.next()) {
                user = new Users();
                user.setIdU(rs.getInt("idU"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
