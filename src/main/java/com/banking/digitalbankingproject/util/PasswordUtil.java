package com.banking.digitalbankingproject.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
    
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur lors de la vérification du mot de passe: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
        // Utilitaire pour générer un nouveau hash
        String password = "admin";
        String hashed = hashPassword(password);
        System.out.println("Hash pour le mot de passe '" + password + "': " + hashed);
        System.out.println("Vérification: " + checkPassword(password, hashed));
    }
} 