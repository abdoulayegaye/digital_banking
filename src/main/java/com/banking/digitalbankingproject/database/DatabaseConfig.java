package com.banking.digitalbankingproject.database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    public static Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
} 