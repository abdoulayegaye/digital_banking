package com.example.projet_java_fx.service.impl;

import com.example.projet_java_fx.database.Db;
import com.example.projet_java_fx.entity.Clients;
import com.example.projet_java_fx.entity.Comptes;
import com.example.projet_java_fx.service.IClient;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    @Override
    public int createClient(Clients client) {
        String sql = "INSERT INTO clients (nom ,prenom ,email) VALUES (?,?,?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            ok=db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int updateClient(Clients client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId()); // ID du client à mettre à jour
            ok = db.executeMaj(); // Exécuter la mise à jour
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok; // Retourne 1 si la mise à jour a réussi, sinon 0
    }

    @Override
    public int deleteClient(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id); // ID du client à supprimer
            ok = db.executeMaj(); // Exécuter la suppression
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok; // Retourne 1 si la suppression a réussi, sinon 0
    }

    @Override
    public Clients getClient(int id) {
        Clients client = null;
        String sql = "SELECT * FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                client = new Clients();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client; // Retourne le client trouvé, ou null si non trouvé
    }

    @Override
    public List<Clients> getAllClients() {
        List<Clients> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Clients client = new Clients();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients; // Retourne la liste des clients
    }

    @Override
    public List<Comptes> getbyClient(int id) {
        return List.of();
    }
}
