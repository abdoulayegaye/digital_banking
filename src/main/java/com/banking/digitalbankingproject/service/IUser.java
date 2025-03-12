package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.User;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations possibles sur les utilisateurs
 */
public interface IUser {
    
    /**
     * Crée un nouvel utilisateur
     * 
     * @param user Utilisateur à créer
     * @return true si la création a réussi
     */
    boolean createUser(User user);
    
    /**
     * Récupère tous les utilisateurs
     * 
     * @return Liste de tous les utilisateurs
     */
    List<User> getAllUsers();
    
    /**
     * Récupère un utilisateur par son nom d'utilisateur
     * 
     * @param username Nom d'utilisateur
     * @return Utilisateur trouvé ou null
     */
    User getUserByUsername(String username);
    
    /**
     * Met à jour un utilisateur existant
     * 
     * @param user Utilisateur à mettre à jour
     * @return true si la mise à jour a réussi
     */
    boolean updateUser(User user);
    
    /**
     * Met à jour le mot de passe d'un utilisateur
     * 
     * @param userId Identifiant de l'utilisateur
     * @param newPassword Nouveau mot de passe
     * @return true si la mise à jour a réussi
     */
    boolean updatePassword(int userId, String newPassword);
    
    /**
     * Met à jour la date de dernière connexion d'un utilisateur
     * 
     * @param userId Identifiant de l'utilisateur
     * @return true si la mise à jour a réussi
     */
    boolean updateLastLogin(int userId);
    
    /**
     * Supprime un utilisateur
     * 
     * @param userId Identifiant de l'utilisateur à supprimer
     * @return true si la suppression a réussi
     */
    boolean deleteUser(int userId);
    
    /**
     * Authentifie un utilisateur
     * 
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @return Utilisateur authentifié ou null
     */
    User authenticate(String username, String password);
}
