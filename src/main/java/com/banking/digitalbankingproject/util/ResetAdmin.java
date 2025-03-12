package com.banking.digitalbankingproject.util;

import com.banking.digitalbankingproject.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ResetAdmin {
    public static void main(String[] args) {
        resetAdminPassword("admin");
    }
    
    public static void resetAdminPassword(String newPassword) {
        // Génération du hash BCrypt
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // D'abord, supprimer l'utilisateur s'il existe
            String deleteSQL = "DELETE FROM users WHERE username = 'admin'";
            conn.createStatement().executeUpdate(deleteSQL);
            
            // Ensuite, créer un nouvel utilisateur admin
            String insertSQL = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "admin");
                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, "Administrateur");
                pstmt.setString(4, "ADMIN");
                pstmt.executeUpdate();
            }
            
            System.out.println("Admin réinitialisé avec succès!");
            System.out.println("Username: admin");
            System.out.println("Password: " + newPassword);
            System.out.println("Hash: " + hashedPassword);
            
            // Vérification
            String plaintext = "admin";
            boolean matches = BCrypt.checkpw(plaintext, hashedPassword);
            System.out.println("Vérification: " + plaintext + " matches " + hashedPassword + " = " + matches);
        } catch (Exception e) {
            System.out.println("Erreur lors de la réinitialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 