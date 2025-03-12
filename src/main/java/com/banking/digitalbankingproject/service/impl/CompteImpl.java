package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.Utils.DatabaseConnection;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompteImpl implements ICompte {
    private final Connection connection;
    private final ClientImpl clientService;

    public CompteImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.clientService = new ClientImpl();
    }

    // Account creation
    @Override
    public Compte createCompte(Compte compte, int clientId) {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            compte.setNumero(generateAccountNumber());
            compte.setCreatedAt(Instant.now());
            compte.setClient(clientService.getClient(clientId));

            pstmt.setString(1, compte.getNumero());
            pstmt.setDouble(2, compte.getBalance());
            pstmt.setTimestamp(3, Timestamp.from(compte.getCreatedAt()));
            pstmt.setInt(4, clientId);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                compte.setId(rs.getInt(1));
            }
            return compte;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating account", e);
        }
    }

    // Account retrieval
    @Override
    public Compte getCompte(String numeroCompte) {
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.numero = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroCompte);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCompte(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving account", e);
        }
    }

    // Account closure
    @Override
    public void closeCompte(String numeroCompte) {
        String sql = "DELETE FROM comptes WHERE numero = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroCompte);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error closing account", e);
        }
    }

    // Client account retrieval
    @Override
    public List<Compte> getComptesClient(int clientId) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id " +
                "WHERE cl.id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
            return comptes;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving client accounts", e);
        }
    }

    // All account retrieval
    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
            return comptes;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all accounts", e);
        }
    }

    // Account balance retrieval
    @Override
    public double getBalance(String numeroCompte) {
        String sql = "SELECT balance FROM comptes WHERE numero = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroCompte);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
            throw new RuntimeException("Account not found");
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving balance", e);
        }
    }

    // Helper methods
    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Compte mapResultSetToCompte(ResultSet rs) throws SQLException {
        Compte compte = new Compte();
        compte.setId(rs.getInt("id"));
        compte.setNumero(rs.getString("numero"));
        compte.setBalance(rs.getDouble("balance"));
        compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());

        Client client = new Client();
        client.setId(rs.getInt("client_id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        compte.setClient(client);

        return compte;
    }
}
