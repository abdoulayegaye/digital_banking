package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClientImpl implements IClient {

    private static final Logger logger = Logger.getLogger(ClientImpl.class.getName());
    private Db db = new Db();
    private int ok;

    @Override
    public boolean createClient(Client client) {
        if (client.getNom() == null || client.getNom().isEmpty() ||
                client.getPrenom() == null || client.getPrenom().isEmpty() ||
                client.getEmail() == null || client.getEmail().isEmpty()) {
            logger.warning("Données du client invalides");
            return false;
        }

        String sql = "INSERT INTO clients VALUES(NULL, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la création du client : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }

    @Override
    public List<Client> getAllClients() {
        String sql = "SELECT * FROM clients ORDER BY nom ASC";
        List<Client> clients = new ArrayList<>();
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
            logger.severe("Erreur lors de la récupération des clients : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return clients;
    }

    @Override
    public Client getClientById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        Client client = null;
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
            logger.severe("Erreur lors de la récupération du client : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return client;
    }

    @Override
    public boolean updateClient(Client client) {
        if (client.getNom() == null || client.getNom().isEmpty() ||
                client.getPrenom() == null || client.getPrenom().isEmpty() ||
                client.getEmail() == null || client.getEmail().isEmpty()) {
            logger.warning("Données du client invalides");
            return false;
        }

        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la mise à jour du client : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }

    @Override
    public boolean deleteClient(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la suppression du client : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }
    
    @Override
    public List<Client> searchClients(String searchTerm) {
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? ORDER BY nom ASC";
        List<Client> clients = new ArrayList<>();
        try {
            db.initPrepar(sql);
            String searchPattern = "%" + searchTerm + "%";
            db.getPstm().setString(1, searchPattern);
            db.getPstm().setString(2, searchPattern);
            db.getPstm().setString(3, searchPattern);
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
            logger.severe("Erreur lors de la recherche des clients : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return clients;
    }
}