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

    // ✅ Ouvrir une seule connexion au lancement
    public Db() {
        try {
            System.out.println("🔍 Tentative de connexion à la base de données...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            if (cnx != null && !cnx.isClosed()) {
                System.out.println("✅ Connexion réussie !");
            } else {
                System.err.println("❌ Connexion échouée !");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir la connexion
    public Connection getConnection() {
        return cnx;  // Retourne la connexion (cnx) déjà établie dans le constructeur
    }



    // ✅ Initialiser le PreparedStatement correctement
    public void initPrepar(String sql) {
        try {
            if (cnx == null || cnx.isClosed()) {
                System.err.println("⚠️ Connexion fermée. Tentative de reconnexion...");
                cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            pstm = cnx.prepareStatement(sql);
            System.out.println("✅ Requête préparée : " + sql);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'initialisation du PreparedStatement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Exécuter une requête SELECT et fermer les ressources après utilisation
    public ResultSet executeSelect() {
        try {
            if (pstm == null) {
                System.err.println("❌ Erreur : executeSelect() appelé sans initPrepar()");
                return null;
            }
            rs = pstm.executeQuery();
            return rs;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'exécution de la requête SELECT : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Exécuter une requête de mise à jour (INSERT, UPDATE, DELETE)
    public int executeMaj() {
        int rowsAffected = 0;
        try {
            if (pstm == null) {
                System.err.println("❌ Erreur : executeMaj() appelé sans initPrepar() !");
                return 0;
            }
            System.out.println("🔍 Exécution de la requête...");
            rowsAffected = pstm.executeUpdate();
            System.out.println("✅ Requête exécutée, " + rowsAffected + " ligne(s) affectée(s)");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'exécution de la mise à jour : " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected;
    }



    // ✅ Fermer le PreparedStatement après chaque utilisation
    public void closeStatement() {
        try {
            if (pstm != null) {
                pstm.close();
                pstm = null;
                System.out.println("✅ PreparedStatement fermé !");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Fermer la connexion proprement à la fin
    public void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (cnx != null) cnx.close();
            System.out.println("✅ Connexion fermée proprement.");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la fermeture de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        if (pstm == null) {
            System.err.println("❌ Erreur : Le PreparedStatement est nul. Assurez-vous d'appeler initPrepar() avant.");
        }
        return pstm;
    }


}

