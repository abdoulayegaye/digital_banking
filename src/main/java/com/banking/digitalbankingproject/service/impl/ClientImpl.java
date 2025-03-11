package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private final Db db;

    public ClientImpl() {
        this.db = new Db();
    }

    public boolean ajouterClient(Client client) {
        String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            return db.executeMaj() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeStatement();
        }
    }

    @Override
    public boolean modifierClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());
            return db.executeMaj() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeStatement();
        }
    }

    @Override
    public boolean supprimerClient(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            return db.executeMaj() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeStatement();
        }
    }

    @Override
    public Client getClient(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return extractClientFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return null;
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                clients.add(extractClientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return clients;
    }

    private Client extractClientFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        return client;
    }
}
