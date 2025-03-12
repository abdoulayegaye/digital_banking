package com.banking.digitalbankingproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.banking.digitalbankingproject.database.Constants.*;

public class Db {
    private Connection cnx;
    private PreparedStatement pstm;
    private ResultSet rs;
    private int ok;

    private void connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Pilote JDBC non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("Erreur de connexion à la BD : " + e.getMessage());
        }
    }

    public void initPrepar(String sql) {
        try {
            connect();
            pstm = cnx.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeSelect() {
        rs = null;
        try {
            rs = pstm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int executeMaj() {
        try {
            ok = pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public void closeConnection() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }

    public Connection getConnection() {
        try {
            if (cnx == null || cnx.isClosed()) {
                connect();
            }
            return cnx;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
            return null;
        }
    }
}