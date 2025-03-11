package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private final Connection connection;

    public ClientImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Client createClient(Client client) {
        String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }
            return client;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating client", e);
        }
    }

    @Override
    public Client updateClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setInt(4, client.getId());
            pstmt.executeUpdate();
            return client;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating client", e);
        }
    }

    @Override
    public void deleteClient(int clientId) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting client", e);
        }
    }

    @Override
    public Client getClient(int clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                return client;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving client", e);
        }
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all clients", e);
        }
    }
}
