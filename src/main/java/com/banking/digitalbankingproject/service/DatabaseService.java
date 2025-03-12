package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    public void addClient(Client client) throws SQLException {
        String query = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }
        }
    }

    public void updateClient(Client client) throws SQLException {
        String query = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setInt(4, client.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteClient(int clientId) throws SQLException {
        String query = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            stmt.executeUpdate();
        }
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email")
                ));
            }
        }
        return clients;
    }

    public void addCompte(Compte compte) throws SQLException {
        String query = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getBalance());
            stmt.setTimestamp(3, Timestamp.valueOf(compte.getCreatedAt()));
            stmt.setInt(4, compte.getClient().getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                compte.setId(rs.getInt(1));
            }
        }
    }

    public List<Compte> getComptesByClient(Client client) throws SQLException {
        List<Compte> comptes = new ArrayList<>();
        String query = "SELECT * FROM comptes WHERE client_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, client.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comptes.add(new Compte(
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getDouble("balance"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        client
                ));
            }
        }
        return comptes;
    }

    public List<Compte> getAllComptes() throws SQLException {
        List<Compte> comptes = new ArrayList<>();
        String query = "SELECT c.id, c.numero, c.balance, c.created_at, c.client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c JOIN clients cl ON c.client_id = cl.id";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("client_id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email")
                );
                comptes.add(new Compte(
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getDouble("balance"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        client
                ));
            }
        }
        return comptes;
    }

    public void updateCompte(Compte compte) throws SQLException {
        String query = "UPDATE comptes SET numero = ?, balance = ?, client_id = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getBalance());
            stmt.setInt(3, compte.getClient().getId());
            stmt.setInt(4, compte.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteCompte(int compteId) throws SQLException {
        String query = "DELETE FROM comptes WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, compteId);
            stmt.executeUpdate();
        }
    }

    public void addOperation(Operation operation) throws SQLException {
        String query = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(operation.getDateOp()));
            stmt.setDouble(2, operation.getAmount());
            stmt.setString(3, operation.getType().toString());
            stmt.setInt(4, operation.getCompte().getId());
            stmt.executeUpdate();

            // Mettre à jour le solde du compte
            String updateQuery = "UPDATE comptes SET balance = balance + ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                double amount = operation.getType() == TypeOperation.RETRAIT ? -operation.getAmount() : operation.getAmount();
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, operation.getCompte().getId());
                updateStmt.executeUpdate();
            }
        }
    }

    public List<Operation> getOperationsByCompte(Compte compte) throws SQLException {
        List<Operation> operations = new ArrayList<>();
        String query = "SELECT * FROM operations WHERE compte_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, compte.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                operations.add(new Operation(
                        rs.getInt("id"),
                        rs.getTimestamp("date_op").toLocalDateTime(),
                        rs.getDouble("amount"),
                        TypeOperation.valueOf(rs.getString("type")),
                        compte
                ));
            }
        }
        return operations;
    }

    public List<Operation> getAllOperations() throws SQLException {
        List<Operation> operations = new ArrayList<>();
        String query = "SELECT o.id, o.date_op, o.amount, o.type, o.compte_id, c.numero, c.balance, c.created_at, c.client_id, cl.nom, cl.prenom, cl.email " +
                "FROM operations o " +
                "JOIN comptes c ON o.compte_id = c.id " +
                "JOIN clients cl ON c.client_id = cl.id";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("client_id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email")
                );
                Compte compte = new Compte(
                        rs.getInt("compte_id"),
                        rs.getString("numero"),
                        rs.getDouble("balance"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        client
                );
                operations.add(new Operation(
                        rs.getInt("id"),
                        rs.getTimestamp("date_op").toLocalDateTime(),
                        rs.getDouble("amount"),
                        TypeOperation.valueOf(rs.getString("type")),
                        compte
                ));
            }
        }
        return operations;
    }

    public void updateOperation(Operation operation) throws SQLException {
        String query = "UPDATE operations SET date_op = ?, amount = ?, type = ?, compte_id = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(operation.getDateOp()));
            stmt.setDouble(2, operation.getAmount());
            stmt.setString(3, operation.getType().toString());
            stmt.setInt(4, operation.getCompte().getId());
            stmt.setInt(5, operation.getId());
            stmt.executeUpdate();

            // Mettre à jour le solde du compte
            String updateQuery = "UPDATE comptes SET balance = balance + ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                double amount = operation.getType() == TypeOperation.RETRAIT ? -operation.getAmount() : operation.getAmount();
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, operation.getCompte().getId());
                updateStmt.executeUpdate();
            }
        }
    }

    public void deleteOperation(int operationId) throws SQLException {
        String query = "DELETE FROM operations WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, operationId);
            stmt.executeUpdate();
        }
    }
}