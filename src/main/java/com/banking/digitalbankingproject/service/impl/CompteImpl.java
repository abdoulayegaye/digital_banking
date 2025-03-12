package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.DatabaseConnection;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.enums.TypeCompte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de gestion des comptes
 */
public class CompteImpl implements ICompte {
    
    private Connection connection;
    private ClientImpl clientService;
    
    /**
     * Constructeur initialisant la connexion à la base de données
     */
    public CompteImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.clientService = new ClientImpl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Récupère tous les comptes de la base de données
     * 
     * @return Liste de tous les comptes
     */
    @Override
    public List<Compte> getAllComptes() throws SQLException {
        List<Compte> comptes = new ArrayList<>();
        String query = "SELECT c.*, tc.libelle as type_compte FROM compte c " +
                       "JOIN type_compte tc ON c.type_compte_id = tc.id " +
                       "ORDER BY c.created_at DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Compte compte = mapResultSetToCompte(rs);
                comptes.add(compte);
            }
        }
        
        return comptes;
    }
    
    /**
     * Récupère un compte par son identifiant
     * 
     * @param id Identifiant du compte
     * @return Compte trouvé ou null
     */
    @Override
    public Compte getCompteById(int id) throws SQLException {
        String query = "SELECT c.*, tc.libelle as type_compte FROM compte c " +
                       "JOIN type_compte tc ON c.type_compte_id = tc.id " +
                       "WHERE c.id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCompte(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Récupère un compte par son numéro
     * 
     * @param numero Numéro du compte
     * @return Compte trouvé ou null
     */
    @Override
    public Compte getCompteByNumero(String numero) throws SQLException {
        String query = "SELECT c.*, tc.libelle as type_compte FROM compte c " +
                       "JOIN type_compte tc ON c.type_compte_id = tc.id " +
                       "WHERE c.numero = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, numero);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCompte(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Récupère tous les comptes d'un client
     * 
     * @param clientId Identifiant du client
     * @return Liste des comptes du client
     */
    @Override
    public List<Compte> getComptesByClientId(int clientId) throws SQLException {
        List<Compte> comptes = new ArrayList<>();
        String query = "SELECT c.*, tc.libelle as type_compte FROM compte c " +
                       "JOIN type_compte tc ON c.type_compte_id = tc.id " +
                       "WHERE c.client_id = ? " +
                       "ORDER BY c.created_at DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Compte compte = mapResultSetToCompte(rs);
                    comptes.add(compte);
                }
            }
        }
        
        return comptes;
    }
    
    /**
     * Enregistre un nouveau compte dans la base de données
     * 
     * @param compte Compte à enregistrer
     * @param typeCompte Type du compte à créer
     * @return Compte enregistré avec son ID généré
     */
    @Override
    public Compte saveCompte(Compte compte, TypeCompte typeCompte) throws SQLException {
        String query = "INSERT INTO compte (numero, balance, created_at, client_id, type_compte_id) " +
                       "VALUES (?, ?, ?, ?, (SELECT id FROM type_compte WHERE libelle = ?))";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, compte.getNumero());
            pstmt.setDouble(2, compte.getBalance());
            pstmt.setTimestamp(3, Timestamp.from(compte.getCreatedAt() != null ? compte.getCreatedAt() : Instant.now()));
            pstmt.setInt(4, compte.getClient().getId());
            pstmt.setString(5, typeCompte.name());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("La création du compte a échoué, aucune ligne affectée.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    compte.setId(generatedKeys.getInt(1));
                    compte.setTypeCompte(typeCompte);
                } else {
                    throw new SQLException("La création du compte a échoué, aucun ID obtenu.");
                }
            }
        }
        
        return compte;
    }
    
    /**
     * Met à jour le solde d'un compte
     * 
     * @param compteId Identifiant du compte
     * @param newBalance Nouveau solde
     * @return true si la mise à jour a réussi
     */
    @Override
    public boolean updateBalance(int compteId, double newBalance) throws SQLException {
        String query = "UPDATE compte SET balance = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, compteId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Supprime un compte par son identifiant
     * 
     * @param id Identifiant du compte à supprimer
     * @return true si la suppression a réussi
     */
    @Override
    public boolean deleteCompte(int id) throws SQLException {
        // Vérifier d'abord si le compte a des opérations
        if (compteHasOperations(id)) {
            throw new SQLException("Impossible de supprimer le compte car il possède des opérations.");
        }
        
        String query = "DELETE FROM compte WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Génère un numéro de compte unique
     * 
     * @param typeCompte Type du compte
     * @return Numéro de compte généré
     */
    @Override
    public String generateAccountNumber(TypeCompte typeCompte) {
        String prefix = typeCompte == TypeCompte.COURANT ? "CC" : "CE";
        long timestamp = System.currentTimeMillis();
        return prefix + timestamp;
    }
    
    /**
     * Vérifie si un compte possède des opérations
     * 
     * @param compteId Identifiant du compte
     * @return true si le compte a au moins une opération
     */
    private boolean compteHasOperations(int compteId) throws SQLException {
        String query = "SELECT COUNT(*) FROM operation WHERE compte_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, compteId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Convertit un ResultSet en objet Compte
     * 
     * @param rs ResultSet contenant les données du compte
     * @return Objet Compte créé à partir du ResultSet
     */
    private Compte mapResultSetToCompte(ResultSet rs) throws SQLException {
        Compte compte = new Compte();
        compte.setId(rs.getInt("id"));
        compte.setNumero(rs.getString("numero"));
        compte.setBalance(rs.getDouble("balance"));
        compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        
        // Récupérer le client associé
        int clientId = rs.getInt("client_id");
        Client client = clientService.getClientById(clientId);
        compte.setClient(client);
        
        // Récupérer le type de compte
        String typeCompteStr = rs.getString("type_compte");
        compte.setTypeCompte(TypeCompte.valueOf(typeCompteStr));
        
        return compte;
    }
}
