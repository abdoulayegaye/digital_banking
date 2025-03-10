package com.banking.digitalbankingproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.banking.digitalbankingproject.database.Constants.*;

public class Db {
    private Connection cnx;
    private PreparedStatement pstm;
    private ResultSet rs;
    private int ok;

    private void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (Exception e){
            System.out.println("Erreur de connexion á la BD : " + e.getMessage());
        }
    }

    public void initPrepar(String sql){
        try{
            connect();
            pstm = cnx.prepareStatement(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet executeSelect(){
        rs = null;
        try{
            rs = pstm.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public int executeMaj(){
        try{
            ok = pstm.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    public void closeConnection(){
        try{
            if (cnx != null)
                cnx.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }

    public void beginTransaction() {
        try {
            if (cnx == null || cnx.isClosed()) {
                connect(); // S'assurer que la connexion est bien établie
            }
            cnx.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        try {
            if (cnx != null) {
                cnx.commit();
                cnx.setAutoCommit(true); // Remet en mode auto-commit après la transaction
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        try {
            if (cnx != null) {
                cnx.rollback();
                cnx.setAutoCommit(true); // Remet en mode auto-commit après l'annulation
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return cnx;
    }


}
