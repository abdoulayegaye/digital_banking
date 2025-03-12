package com.banking.digitalbankingproject.tools;

import com.banking.digitalbankingproject.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateAdmin {
    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        String sql = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Supprimer l'ancien admin s'il existe
            conn.createStatement().execute("DELETE FROM users WHERE username = 'admin'");
            
            // Créer le nouvel admin
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, "Administrateur");
            pstmt.setString(4, "ADMIN");
            
            pstmt.executeUpdate();
            
            System.out.println("Utilisateur admin créé avec succès!");
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 