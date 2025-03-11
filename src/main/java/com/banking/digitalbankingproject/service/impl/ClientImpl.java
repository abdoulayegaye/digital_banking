package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.tools.Utils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    @Override
    public boolean createClient(Client client) {
        String sql = "INSERT INTO clients VALUES(NULL, ?, ?, ?)";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            ok = db.executeMaj();
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok == 1;
    }

    @Override
    public List<Client> getAllClients() {
        String sql = "SELECT * FROM clients ORDER BY nom ASC";
        List<Client> clients = new ArrayList<Client>();
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
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client getClientbyId(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setNom(rs.getString("nom"));
            client.setPrenom(rs.getString("prenom"));
            client.setEmail(rs.getString("email"));

            db.closeConnection();
            return client;
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    @Override
    public boolean deleteClient(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ok = db.executeMaj();
            db.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }

        return ok == 1;
    }

    @Override
    public boolean updateClient(Client client) {
        String sql = "UPDATE clients set nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());
            ok = db.executeMaj();

        }catch (Exception e){
            e.printStackTrace();
        }
        return ok == 1;
    }
}
