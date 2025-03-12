package com.banking.digitalbankingproject.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.stream.Collectors;

import static com.banking.digitalbankingproject.database.Constants.*;

public class Db {
    private static Connection cnx;
    private PreparedStatement pstm;
    private ResultSet rs;
    private int ok;

    public Db() {
        try {
            if (cnx == null || cnx.isClosed()) {
                connect();
                initializeDatabase();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Tentative de connexion à : " + URL);
            
            try {
                // D'abord, essayons de nous connecter au serveur MySQL
                String serverUrl = "jdbc:mysql://" + HOST + ":" + PORT;
                Connection serverConnection = DriverManager.getConnection(serverUrl, USER, PASSWORD);
                
                // Créer la base de données si elle n'existe pas
                PreparedStatement createDbStmt = serverConnection.prepareStatement(
                    "CREATE DATABASE IF NOT EXISTS " + DATABASE
                );
                createDbStmt.executeUpdate();
                createDbStmt.close();
                serverConnection.close();
                
                // Maintenant connectons-nous à la base de données spécifique
                cnx = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion établie avec succès");
            } catch (Exception e) {
                System.err.println("Erreur lors de la création/connexion à la base de données : " + e.getMessage());
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la BD : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossible de se connecter à la base de données", e);
        }
    }

    private void initializeDatabase() {
        try {
            // Supprimer d'abord toutes les tables
            String[] dropTables = {
                "DROP TABLE IF EXISTS operations",
                "DROP TABLE IF EXISTS comptes",
                "DROP TABLE IF EXISTS clients",
                "DROP TABLE IF EXISTS users"
            };

            for (String dropQuery : dropTables) {
                try {
                    cnx.createStatement().execute(dropQuery);
                    System.out.println("Table supprimée avec succès : " + dropQuery);
                } catch (Exception e) {
                    System.err.println("Erreur lors de la suppression de la table : " + e.getMessage());
                }
            }

            // Créer les tables
            String[] createTables = {
                "CREATE TABLE users (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
                "CREATE TABLE clients (id INT PRIMARY KEY AUTO_INCREMENT, nom VARCHAR(45) NOT NULL, prenom VARCHAR(65) NOT NULL, email VARCHAR(80) NOT NULL UNIQUE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
                "CREATE TABLE comptes (id INT PRIMARY KEY AUTO_INCREMENT, numero VARCHAR(200) NOT NULL UNIQUE, balance DOUBLE NOT NULL DEFAULT 5000, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, client_id INT NOT NULL, FOREIGN KEY (client_id) REFERENCES clients(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
                "CREATE TABLE operations (id INT PRIMARY KEY AUTO_INCREMENT, date_op TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, amount DOUBLE NOT NULL, type ENUM('DEPOT', 'RETRAIT', 'VERSEMENT') NOT NULL, compte_id INT NOT NULL, FOREIGN KEY (compte_id) REFERENCES comptes(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            };

            for (String createQuery : createTables) {
                try {
                    cnx.createStatement().execute(createQuery);
                    System.out.println("Table créée avec succès");
                } catch (Exception e) {
                    System.err.println("Erreur lors de la création de la table : " + e.getMessage());
                    System.err.println("Requête en erreur : " + createQuery);
                }
            }

            // Insérer l'utilisateur admin
            String insertAdmin = "INSERT INTO users (username, password) VALUES ('admin', '$2a$10$n9H5SyD.KQB0ZxOI3YhvPeXXJrU0Fy.89Vv6TdXDQJcOYqVXDtumu')";
            try {
                cnx.createStatement().execute(insertAdmin);
                System.out.println("Utilisateur admin créé avec succès");
            } catch (Exception e) {
                System.err.println("Erreur lors de la création de l'utilisateur admin : " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de la base de données", e);
        }
    }

    public void initPrepar(String sql) {
        try {
            if (cnx == null || cnx.isClosed()) {
                connect();
            }
            System.out.println("Préparation de la requête : " + sql);
            pstm = cnx.prepareStatement(sql);
        } catch (Exception e) {
            System.err.println("Erreur lors de la préparation de la requête : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la préparation de la requête", e);
        }
    }

    public ResultSet executeSelect() {
        try {
            if (pstm != null) {
                System.out.println("Exécution de la requête SELECT");
                rs = pstm.executeQuery();
                return rs;
            } else {
                throw new RuntimeException("PreparedStatement est null");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution de la requête SELECT : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'exécution de la requête SELECT", e);
        }
    }

    public int executeMaj() {
        try {
            if (pstm != null) {
                System.out.println("Exécution de la requête de mise à jour");
                ok = pstm.executeUpdate();
                return ok;
            } else {
                throw new RuntimeException("PreparedStatement est null");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution de la mise à jour : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'exécution de la mise à jour", e);
        }
    }

    public void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
