package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public  class ClientImpl implements IClient {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public int create(Client client) {
        String sql = "Insert into clients values(NULL,?,?,?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());

            ok = db.executeMaj();

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int update(Client client) {
        String sql = "Update clients set nom = ?, prenom = ? , email = ? where id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());

            ok = db.executeMaj();

            db.closeConnection();

        } catch (Exception e) {
          e.printStackTrace();
        }
        return ok;
    }

    @Override
    public Client getClientByID(int id) {
        Client client = null;
        String sql = "select * from clients where id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1,id);
            rs = db.executeSelect(sql);
            if (rs.next()) {
                client = new Client();
                client.setId(id);
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
            }
            db.closeConnection();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public int delete(int id) {
        String sql = "delete from clients where id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1,id);

            ok = db.executeMaj();

            db.closeConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public Client get(int clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        Client client = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1,clientId);
            rs = db.executeSelect(sql);
            if (rs.next()){
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));

            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }
    @Override
    public List<Client> list(String search) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR prenom LIKE ? ORDER BY nom ASC";

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + search + "%");
            db.getPstm().setString(2, "%" + search + "%");

            rs = db.executeSelect(sql);
            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

}
