package com.banking.digitalbankingproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.banking.digitalbankingproject.database.Constants.*;

public class Db {

    private static final Logger logger = Logger.getLogger(Db.class.getName());

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Pilote JDBC introuvable", e);
            throw new SQLException("Pilote JDBC introuvable", e);
        }
    }

    public ResultSet executeSelect(String sql) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'exécution de la requête SELECT", e);
            return null;
        }
    }
    public int executeUpdate(String sql) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'exécution de la requête UPDATE/INSERT/DELETE", e);
            return -1;
        }
    }

    public int executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'exécution de la requête paramétrée", e);
            return -1; 
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erreur lors de la fermeture de la connexion", e);
            }
        }
    }

    public void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erreur lors de la fermeture du ResultSet", e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erreur lors de la fermeture du PreparedStatement", e);
            }
        }
    }
}
