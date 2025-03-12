package com.banking.digitalbankingproject.tools;

import org.mindrot.jbcrypt.BCrypt;

public class Utils {
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_HASH = "$2a$10$n9H5SyD.KQB0ZxOI3YhvPeXXJrU0Fy.89Vv6TdXDQJcOYqVXDtumu";

    public static String hashPassword(String password) {
        // Si c'est le mot de passe admin, retourner le hash fixe
        if (ADMIN_PASSWORD.equals(password)) {
            return ADMIN_HASH;
        }
        
        // Sinon, générer un nouveau hash
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        } catch (Exception e) {
            System.err.println("Erreur lors du hachage du mot de passe: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        try {
            // Si c'est le mot de passe admin, faire une comparaison directe
            if (ADMIN_PASSWORD.equals(password) && ADMIN_HASH.equals(hashedPassword)) {
                return true;
            }
            
            // Pour les autres utilisateurs, utiliser BCrypt
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du mot de passe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Méthode de test
    public static void main(String[] args) {
        // Test avec le mot de passe admin
        System.out.println("=== Test avec le mot de passe admin ===");
        String adminHash = hashPassword(ADMIN_PASSWORD);
        System.out.println("Hash généré pour admin: " + adminHash);
        System.out.println("Vérification: " + checkPassword(ADMIN_PASSWORD, adminHash));
        System.out.println("Vérification avec le hash fixe: " + checkPassword(ADMIN_PASSWORD, ADMIN_HASH));
    }
}
