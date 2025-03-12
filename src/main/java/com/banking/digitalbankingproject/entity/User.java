package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.Role;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Classe représentant un utilisateur du système
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    /**
     * Identifiant unique de l'utilisateur
     */
    private Integer id;
    
    /**
     * Nom d'utilisateur pour la connexion
     */
    private String username;
    
    /**
     * Mot de passe hashé
     */
    private String password;
    
    /**
     * Nom complet de l'utilisateur
     */
    private String fullName;
    
    /**
     * Email de l'utilisateur
     */
    private String email;
    
    /**
     * Rôle de l'utilisateur dans le système
     */
    private Role role;
    
    /**
     * Date de création du compte
     */
    private LocalDateTime createdAt;
    
    /**
     * Dernière date de connexion
     */
    private Instant lastLogin;
    
    /**
     * Indique si le compte est actif
     */
    private boolean active;
    
    /**
     * Constructeur avec les champs essentiels
     * 
     * @param username Nom d'utilisateur
     * @param password Mot de passe (sera hashé)
     * @param fullName Nom complet
     * @param email Email
     * @param role Rôle de l'utilisateur
     */
    public User(String username, String password, String fullName, String email, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }
    
    /**
     * Vérifie si l'utilisateur est un administrateur
     * 
     * @return true si l'utilisateur a le rôle ADMIN
     */
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
    
    /**
     * Vérifie si l'utilisateur est un employé
     * 
     * @return true si l'utilisateur a le rôle EMPLOYEE
     */
    public boolean isEmployee() {
        return this.role == Role.EMPLOYEE;
    }
    
    /**
     * Met à jour la date de dernière connexion
     */
    public void updateLastLogin() {
        this.lastLogin = Instant.now();
    }
    
    /**
     * Active ou désactive le compte utilisateur
     * 
     * @param status Statut d'activation
     */
    public void setActiveStatus(boolean status) {
        this.active = status;
    }
    
    /**
     * Vérifie si le compte est actif
     * 
     * @return true si le compte est actif
     */
    public boolean isAccountActive() {
        return this.active;
    }
    
    /**
     * Retourne les initiales de l'utilisateur (pour l'affichage dans l'UI)
     * 
     * @return Initiales basées sur le nom complet
     */
    public String getInitials() {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        
        String[] names = fullName.split(" ");
        StringBuilder initials = new StringBuilder();
        
        for (String name : names) {
            if (!name.isEmpty()) {
                initials.append(name.charAt(0));
            }
        }
        
        return initials.toString().toUpperCase();
    }
}
