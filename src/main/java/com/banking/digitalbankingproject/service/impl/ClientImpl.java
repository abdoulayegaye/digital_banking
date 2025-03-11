package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private final Db db = new Db();

    @Override
    public int addClient(Client client) {
        // Validation des champs
        if (client.getNom() == null || client.getNom().trim().isEmpty() ||
                client.getPrenom() == null || client.getPrenom().trim().isEmpty() ||
                client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            System.err.println("Erreur : Tous les champs sont obligatoires !");
            return 0;
        }

        String sql = "INSERT INTO clients(nom, prenom, email) VALUES(?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            return db.executeMaj();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public int updateClient(Client client) {
        // Validation des champs
        if (client.getNom() == null || client.getNom().trim().isEmpty() ||
                client.getPrenom() == null || client.getPrenom().trim().isEmpty() ||
                client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            System.err.println("Erreur : Tous les champs sont obligatoires !");
            return 0;
        }

        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());
            return db.executeMaj();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public int deleteClient(Client client) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, client.getId());
            return db.executeMaj();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY nom ASC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            return clients;
        } catch (SQLException e) {
            handleException(e);
            return List.of();
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public Client getClientById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
            return null;
        } catch (SQLException e) {
            handleException(e);
            return null;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        return searchClients("nom", name);
    }

    @Override
    public List<Client> searchClientsByEmail(String email) {
        return searchClients("email", email);
    }

    @Override
    public int countClients() {
        String sql = "SELECT COUNT(*) FROM clients";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    private List<Client> searchClients(String field, String value) {
        List<Client> clients = new ArrayList<>();
        String sql = String.format("SELECT * FROM clients WHERE %s LIKE ?", field);
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + value + "%");
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            return clients;
        } catch (SQLException e) {
            handleException(e);
            return List.of();
        } finally {
            db.closeConnection();
        }
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email")
        );
    }

    private void handleException(Exception e) {
        // À remplacer par un vrai logger en production
        System.err.println("Erreur de base de données : " + e.getMessage());
        e.printStackTrace();
    }
}