package org.example.javafx.service.impl;

import org.example.javafx.dao.DBConnexion;
import org.example.javafx.entities.User;
import org.example.javafx.service.IUser;
import java.sql.ResultSet;

public class UserImpl implements IUser {
    private DBConnexion db = new DBConnexion();
    private ResultSet rs;

    @Override
    public User SeConnecter(String usernam, String pass) {
        User user = null;
        String sql = "select * from users where username = ? and password = ?";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, usernam);
            db.getPstm().setString(2, pass);
            rs = db.executeSelect();
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));

            }
            db.closeConnection();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return user;
    }
}
