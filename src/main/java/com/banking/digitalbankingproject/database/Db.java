package com.banking.digitalbankingproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private static final String URL = "jdbc:mysql://localhost:3306/digital_banking_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Remplacez par votre mot de passe

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}