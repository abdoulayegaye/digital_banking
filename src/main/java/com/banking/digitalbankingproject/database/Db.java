package com.banking.digitalbankingproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.banking.digitalbankingproject.database.Constants.*;

public class Db {

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Pilote JDBC introuvable", e);
        }
    }

    public ResultSet executeSelect(String sql) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String sql) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int rows = preparedStatement.executeUpdate();
            connection.close();
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int executeUpdate(String sql, Object... params) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for(int i = 0; i < params.length; i++){
                preparedStatement.setObject(i+1, params[i]);
            }
            int rows = preparedStatement.executeUpdate();
            connection.close();
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
