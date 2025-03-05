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

    /**
     * Établit une connexion à la base de données.
     *
     * @return Une connexion à la base de données.
     * @throws SQLException Si la connexion échoue.
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Pilote JDBC introuvable", e);
            throw new SQLException("Pilote JDBC introuvable", e);
        }
    }

    /**
     * Exécute une requête SQL de type SELECT.
     *
     * @param sql La requête SQL à exécuter.
     * @return Un ResultSet contenant les résultats de la requête.
     */
    public ResultSet executeSelect(String sql) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'exécution de la requête SELECT", e);
            return null;
        }
    }

    /**
     * Exécute une requête SQL de type INSERT, UPDATE ou DELETE.
     *
     * @param sql La requête SQL à exécuter.
     * @return Le nombre de lignes affectées.
     */
    public int executeUpdate(String sql) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'exécution de la requête UPDATE/INSERT/DELETE", e);
            return -1; // Retourne -1 en cas d'erreur
        }
    }

    /**
     * Exécute une requête SQL de type INSERT, UPDATE ou DELETE avec des paramètres.
     *
     * @param sql    La requête SQL à exécuter.
     * @param params Les paramètres à passer à la requête.
     * @return Le nombre de lignes affectées.
     */
    public int executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Définir les paramètres
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'exécution de la requête paramétrée", e);
            return -1; // Retourne -1 en cas d'erreur
        }
    }

    /**
     * Ferme une connexion à la base de données.
     *
     * @param connection La connexion à fermer.
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erreur lors de la fermeture de la connexion", e);
            }
        }
    }

    /**
     * Ferme un ResultSet.
     *
     * @param resultSet Le ResultSet à fermer.
     */
    public void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erreur lors de la fermeture du ResultSet", e);
            }
        }
    }

    /**
     * Ferme un PreparedStatement.
     *
     * @param preparedStatement Le PreparedStatement à fermer.
     */
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