package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.DatabaseConnection;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.enums.Role;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.tools.Utils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de gestion des utilisateurs
 */
public class UserImpl implements IUser {

    private Connection connection;
    
    /**
     * Constructeur initialisant la connexion à la base de données
     */
    public UserImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Crée un nouvel utilisateur
     * 
     * @param user Utilisateur à créer
     * @return true si la création a réussi
     */
    @Override
    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, password, full_name, email, role, created_at, active) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, Utils.hashPassword(user.getPassword()));
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole().name());
            pstmt.setTimestamp(6, Timestamp.from(Instant.now()));
            pstmt.setBoolean(7, user.isActive());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère tous les utilisateurs
     * 
     * @return Liste de tous les utilisateurs
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY username ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur
     * 
     * @param username Nom d'utilisateur
     * @return Utilisateur trouvé ou null
     */
    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        System.out.println("Executing SQL: " + sql + " with username: " + username);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                    User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(Role.valueOf(rs.getString("role")));
                    
                    System.out.println("User found: " + user.getUsername());
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("No user found with username: " + username);
        return null;
    }
    
    /**
     * Met à jour un utilisateur existant
     * 
     * @param user Utilisateur à mettre à jour
     * @return true si la mise à jour a réussi
     */
    @Override
    public boolean updateUser(User user) {
        String query = "UPDATE users SET full_name = ?, email = ?, role = ?, active = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getRole().name());
            pstmt.setBoolean(4, user.isActive());
            pstmt.setInt(5, user.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Met à jour le mot de passe d'un utilisateur
     * 
     * @param userId Identifiant de l'utilisateur
     * @param newPassword Nouveau mot de passe
     * @return true si la mise à jour a réussi
     */
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, Utils.hashPassword(newPassword));
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Met à jour la date de dernière connexion d'un utilisateur
     * 
     * @param userId Identifiant de l'utilisateur
     * @return true si la mise à jour a réussi
     */
    @Override
    public boolean updateLastLogin(int userId) {
        String query = "UPDATE users SET last_login = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setTimestamp(1, Timestamp.from(Instant.now()));
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprime un utilisateur
     * 
     * @param userId Identifiant de l'utilisateur à supprimer
     * @return true si la suppression a réussi
     */
    @Override
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Vérifie les identifiants d'un utilisateur
     * 
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @return Utilisateur authentifié ou null
     */
    @Override
    public User authenticate(String username, String password) {
        User user = getUserByUsername(username);
        
        if (user != null && Utils.checkPassword(password, user.getPassword())) {
            updateLastLogin(user.getId());
            return user;
        }
        
        return null;
    }
    
    /**
     * Convertit un ResultSet en objet User
     * 
     * @param rs ResultSet contenant les données de l'utilisateur
     * @return Objet User créé à partir du ResultSet
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        
        // Conversion sécurisée de la chaîne en Role
        String roleStr = rs.getString("role");
        if (roleStr != null && !roleStr.isEmpty()) {
            try {
                // Assurons-nous que la chaîne est en majuscules et sans espaces
                String formattedRole = roleStr.trim().toUpperCase();
                user.setRole(Role.valueOf(formattedRole));
            } catch (IllegalArgumentException e) {
                System.out.println("Rôle invalide trouvé: " + roleStr + ", utilisation du rôle USER par défaut");
                user.setRole(Role.USER);
            }
        } else {
            user.setRole(Role.USER);
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return user;
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        System.out.println("Executing SQL: " + sql + " with username: " + username);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return BCrypt.checkpw(password, storedPassword);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("No user found with username: " + username);
        return false;
    }
}
