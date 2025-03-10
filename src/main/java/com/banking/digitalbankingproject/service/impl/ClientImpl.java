package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class   ClientImpl implements IClient {
    
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public int save(Client client) throws Exception {
        String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'ajout du client : " + e.getMessage());
        }
        return ok;
    }

    @Override
    public int update(Client client) throws Exception {
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
            throw new Exception("Erreur lors de la modification du client : " + e.getMessage());
        }
        return ok;
    }

    @Override
    public int delete(int id) throws Exception {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            throw new Exception("Erreur lors de la suppression du client : " + e.getMessage());
        }
        return ok;
    }

    @Override
    public List<Client> getAll() throws Exception {
        String sql = "SELECT * FROM clients ORDER BY nom, prenom";
        List<Client> clients = new ArrayList<>();
        try {
            System.out.println("Exécution de la requête : " + sql);
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
                System.out.println("Client chargé : " + client.getNom() + " " + client.getPrenom());
            }
            System.out.println("Nombre total de clients chargés : " + clients.size());
            db.closeConnection();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des clients : " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur lors de la récupération des clients : " + e.getMessage());
        }
        return clients;
    }

    @Override
    public Client getById(int id) throws Exception {
        String sql = "SELECT * FROM clients WHERE id = ?";
        Client client = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
            }
            db.closeConnection();
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du client : " + e.getMessage());
        }
        return client;
    }
}
