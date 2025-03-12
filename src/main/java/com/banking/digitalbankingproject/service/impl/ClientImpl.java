package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.DatabaseConnection;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de gestion des clients
 */
public class ClientImpl implements IClient {
    
    private Connection connection;
    
    /**
     * Constructeur initialisant la connexion à la base de données
     */
    public ClientImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Récupère tous les clients de la base de données
     * 
     * @return Liste de tous les clients
     */
    @Override
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client ORDER BY nom, prenom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Client client = mapResultSetToClient(rs);
                clients.add(client);
            }
        }
        
        return clients;
    }
    
    /**
     * Récupère un client par son identifiant
     * 
     * @param id Identifiant du client
     * @return Client trouvé ou null
     */
    @Override
    public Client getClientById(int id) throws SQLException {
        String query = "SELECT * FROM client WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Enregistre un nouveau client dans la base de données
     * 
     * @param client Client à enregistrer
     * @return Client enregistré avec son ID généré
     */
    @Override
    public Client saveClient(Client client) throws SQLException {
        String query = "INSERT INTO client (nom, prenom, email, telephone, adresse) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getTelephone());
            pstmt.setString(5, client.getAdresse());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("La création du client a échoué, aucune ligne affectée.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du client a échoué, aucun ID obtenu.");
                }
            }
        }
        
        return client;
    }
    
    /**
     * Met à jour les informations d'un client existant
     * 
     * @param client Client à mettre à jour
     * @return true si la mise à jour a réussi
     */
    @Override
    public boolean updateClient(Client client) throws SQLException {
        String query = "UPDATE client SET nom = ?, prenom = ?, email = ?, telephone = ?, adresse = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getTelephone());
            pstmt.setString(5, client.getAdresse());
            pstmt.setInt(6, client.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Supprime un client par son identifiant
     * 
     * @param id Identifiant du client à supprimer
     * @return true si la suppression a réussi
     */
    @Override
    public boolean deleteClient(int id) throws SQLException {
        // Vérifier d'abord si le client a des comptes
        if (clientHasAccounts(id)) {
            throw new SQLException("Impossible de supprimer le client car il possède des comptes actifs.");
        }
        
        String query = "DELETE FROM client WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Recherche des clients selon un critère (nom, prénom ou email)
     * 
     * @param searchTerm Terme de recherche
     * @return Liste des clients correspondant au critère
     */
    @Override
    public List<Client> searchClients(String searchTerm) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? ORDER BY nom, prenom";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String term = "%" + searchTerm + "%";
            pstmt.setString(1, term);
            pstmt.setString(2, term);
            pstmt.setString(3, term);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Client client = mapResultSetToClient(rs);
                    clients.add(client);
                }
            }
        }
        
        return clients;
    }
    
    /**
     * Vérifie si un client possède des comptes
     * 
     * @param clientId Identifiant du client
     * @return true si le client a au moins un compte
     */
    private boolean clientHasAccounts(int clientId) throws SQLException {
        String query = "SELECT COUNT(*) FROM compte WHERE client_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Convertit un ResultSet en objet Client
     * 
     * @param rs ResultSet contenant les données du client
     * @return Objet Client créé à partir du ResultSet
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        client.setTelephone(rs.getString("telephone"));
        client.setAdresse(rs.getString("adresse"));
        return client;
    }

    @Override
    public boolean add(Client client) {
        String sql = "INSERT INTO clients (nom, prenom, email, telephone, adresse) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getTelephone());
            pstmt.setString(5, client.getAdresse());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Client client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ?, telephone = ?, adresse = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getTelephone());
            pstmt.setString(5, client.getAdresse());
            pstmt.setInt(6, client.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Client getById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return clients;
    }
}
