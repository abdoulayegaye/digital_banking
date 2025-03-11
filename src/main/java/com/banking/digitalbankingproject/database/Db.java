package com.banking.digitalbankingproject.database;

import java.sql.*;

import static com.banking.digitalbankingproject.database.Constants.*;

public class Db {
    private static Connection cnx;
    private static PreparedStatement pstm;
    private static ResultSet rs;
    private int ok;

    private static void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (Exception e){
            System.out.println("Erreur de connexion á la BD : " + e.getMessage());
        }
    }

    public static void initPrepar(String sql){
        try{
            connect();
            pstm = cnx.prepareStatement(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ResultSet executeSelect(){
        rs = null;
        try{
            rs = pstm.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public static CallableStatement getPstmt() {
        return null;
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

    public PreparedStatement getPstm() {return pstm;}


    public static Connection getConnection() {
        return null;
    }
}