package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private int id;

    @Override
    public int addClient(Client client) {
        String sql = "INSERT INTO clients VALUES(DEFAULT,?,?,?)";
        int ok = 0;

        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            ok = db.executeMaj();

            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int updateClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());
            ok = db.executeMaj();
            db.closeConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int deleteClient(Client client) {
        String sql = "DELETE FROM clients WHERE id = ?";
        int ok = 0;

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, client.getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<Client>();
        String sql = "SELECT * FROM clients ORDER BY nom ASC";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));

                clients.add(client);
            }
        }catch (Exception e) {
            e.printStackTrace();
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
            rs = db.executeSelect();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
            }
            db.closeConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE nom LIKE ?";

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + name + "%");
            ResultSet rs = db.executeSelect();

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email")
                ));
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public List<Client> searchClientsByEmail(String email) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE email LIKE ?";

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + email + "%");
            ResultSet rs = db.executeSelect();

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email")
                ));
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public int countClients() {
        String sql = "SELECT COUNT(*) FROM clients";
        int total = 0;

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();

            if (rs.next()) {
                total = rs.getInt(1);
            }

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
