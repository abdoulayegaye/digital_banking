package org.example.javafx.service.impl;
import org.example.javafx.dao.DBConnexion;
import org.example.javafx.entities.Client;
import org.example.javafx.service.IClient;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private DBConnexion db = new DBConnexion();
    private ResultSet rs;
    private int ok;

    @Override
    public int create(Client client) {
        String sql = "INSERT INTO Clients (nom, prenom, email) VALUES (?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            ok = db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }
    @Override
    public int update(Client client) {
        String sql = "UPDATE Clients SET nom = ?, prenom = ?, email = ? WHERE id_client = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, client.getNom());
            db.getPstm().setString(2, client.getPrenom());
            db.getPstm().setString(3, client.getEmail());
            db.getPstm().setInt(4, client.getId());
            ok = db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM Clients WHERE id_client = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ok = db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }
}