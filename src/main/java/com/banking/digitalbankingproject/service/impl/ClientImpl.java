package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {

    private Db db = new Db();

    @Override
    public boolean createClient(Client client) {
        String sql = "INSERT INTO clients VALUES (NULL, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean updateClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean deleteClient(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return clients;
    }

    @Override
    public Client getClientById(int id) {
        Client client = null;
        String sql = "SELECT * FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return client;
    }
    @Override
    public boolean clientExists(String nom, String prenom, String email) {
        String sql = "SELECT COUNT(*) FROM clients WHERE nom = ? AND prenom = ? OR email = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, nom);
            db.getPstm().setString(2, prenom);
            db.getPstm().setString(3, email);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Retourne true si un client existe déjà
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return false;
    }

    @Override
    public boolean hasLinkedAccounts(int clientId) {
        String sql = "SELECT COUNT(*) FROM comptes WHERE client_id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, clientId);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Retourne true si le client a des comptes associés
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return false;
    }

    @Override
    public int countClients() {
        String sql = "SELECT COUNT(*) FROM clients";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return rs.getInt(1); // Retourne le nombre de clients
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return 0;
    }

}