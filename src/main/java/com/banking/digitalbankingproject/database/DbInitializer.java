package com.banking.digitalbankingproject.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.banking.digitalbankingproject.database.Constants.*;

public class DbInitializer {
    private static final Logger logger = Logger.getLogger(DbInitializer.class.getName());
    
    public static boolean initializeDatabase() {
        try {
            logger.info("Initialisation de la base de données...");
            
            // Charger le script SQL depuis les ressources
            InputStream inputStream = DbInitializer.class.getResourceAsStream("/sql/init_database.sql");
            if (inputStream == null) {
                logger.severe("Le fichier SQL d'initialisation n'a pas été trouvé");
                return false;
            }
            
            String sqlScript = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            
            // Diviser le script en instructions SQL individuelles
            String[] sqlInstructions = sqlScript.split(";");
            
            // Établir une connexion à MySQL sans spécifier de base de données
            Class.forName("com.mysql.cj.jdbc.Driver");
            String rootUrl = "jdbc:mysql://" + HOST + ":" + PORT;
            
            try (Connection connection = DriverManager.getConnection(rootUrl, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {
                
                // Créer la base de données si elle n'existe pas
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE);
                logger.info("Base de données créée ou déjà existante: " + DATABASE);
                
                // Se connecter à la base de données spécifique
                try (Connection dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
                     Statement dbStatement = dbConnection.createStatement()) {
                    
                    // Exécuter chaque instruction SQL
                    for (String sql : sqlInstructions) {
                        sql = sql.trim();
                        if (!sql.isEmpty()) {
                            dbStatement.executeUpdate(sql);
                        }
                    }
                    
                    logger.info("Base de données initialisée avec succès");
                    return true;
                }
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 