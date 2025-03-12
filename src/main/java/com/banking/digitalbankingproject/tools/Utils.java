package com.banking.digitalbankingproject.tools;

import org.mindrot.jbcrypt.BCrypt;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

/**
 * Classe utilitaire pour diverses fonctionnalités communes
 */
public class Utils {

    /**
     * Hashe un mot de passe en utilisant BCrypt
     * 
     * @param plainTextPassword Mot de passe en clair
     * @return Mot de passe hashé
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Vérifie si un mot de passe correspond à sa version hashée
     * 
     * @param plainTextPassword Mot de passe en clair
     * @param hashedPassword Mot de passe hashé
     * @return true si les mots de passe correspondent
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    /**
     * Génère un identifiant unique basé sur le timestamp
     * 
     * @param prefix Préfixe pour l'identifiant
     * @return Identifiant unique
     */
    public static String generateUniqueId(String prefix) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Random random = new Random();
        String timestamp = dateFormat.format(new Date());
        int randomNum = random.nextInt(1000);
        return prefix + timestamp + String.format("%03d", randomNum);
    }

    /**
     * Formate une date pour l'affichage
     * 
     * @param instant Date à formater
     * @return Date formatée en chaîne
     */
    public static String formatDate(Instant instant) {
        if (instant == null) return "";
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    /**
     * Formate un montant pour l'affichage
     * 
     * @param amount Montant à formater
     * @return Montant formaté en chaîne
     */
    public static String formatAmount(double amount) {
        return String.format("%,.2f €", amount);
    }

    /**
     * Vérifie si une chaîne est un nombre valide
     * 
     * @param str Chaîne à vérifier
     * @return true si la chaîne est un nombre valide
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Vérifie si une adresse email est valide
     * 
     * @param email Adresse email à vérifier
     * @return true si l'email est valide
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
