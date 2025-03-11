package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public boolean CreateCompte(Compte compte) {
        String sql;
        if(compte.getBalance() != null){
            sql = "INSERT INTO comptes(numero, balance, client_id) VALUES (?, ?, ?)";
        }else {
            sql = "INSERT INTO comptes(numero, balance, client_id) VALUES (?, DEFAULT, ?)";
        }

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            if(compte.getBalance() != null){
                db.getPstm().setDouble(2, compte.getBalance());
                db.getPstm().setInt(3, compte.getClient().getId());
            }else {
                db.getPstm().setInt(2, compte.getClient().getId());
            }
            ok = db.executeMaj();
            db.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }
        return ok == 1;
    }

    @Override
    public List<Compte> GetAllCompte() {
        String sql = "SELECT * FROM comptes JOIN clients ON comptes.client_id = clients.id";
        List<Compte> comptes = new ArrayList<>();
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                this.setCompte(rs, compte);
                comptes.add(compte);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return comptes;
    }

    @Override
    public Compte GetCompteByNumCompte(String numCompte) {
        String sql = "SELECT * FROM comptes JOIN clients ON comptes.client_id = clients.id WHERE comptes.numero = ?";
        Compte compte = new Compte();
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numCompte);
            rs = db.executeSelect();
            while (rs.next()) {
                this.setCompte(rs, compte);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return compte;
    }

    @Override
    public boolean UpdateCompte(Compte compte) {
        String sql = "UPDATE comptes SET balance = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1,compte.getBalance());
            db.getPstm().setInt(2,compte.getId());
            ok = db.executeMaj();
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok == 1;
    }

    @Override
    public boolean DeleteCompte(Compte compte) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compte.getId());
            ok = db.executeMaj();
            db.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }

        return ok == 1;
    }

    @Override
    public List<Compte> GetAllCompteByIdClient(int id_client) {
        String sql = "SELECT * FROM comptes JOIN clients ON comptes.client_id = clients.id WHERE client_id = ?";
        List<Compte> comptes = new ArrayList<>();
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1,id_client);
            rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                this.setCompte(rs, compte);
                comptes.add(compte);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return comptes;
    }

    @Override
    public Compte GetCompeById(int id) {
        String sql = "SELECT * FROM comptes JOIN clients ON comptes.client_id = clients.id WHERE comptes.id = ?";
        Compte compte = new Compte();
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            while (rs.next()) {
                this.setCompte(rs, compte);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return compte;
    }

    private void setCompte(ResultSet rs, Compte compte) throws SQLException {
        compte.setNumero(rs.getString("numero"));
        compte.setBalance(rs.getDouble("balance"));
        compte.setId(rs.getInt("id"));
        compte.setCreatedAt(rs.getTimestamp("created_at"));

        Client client = new Client();
        client.setId(rs.getInt("client_id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        compte.setClient(client);
    }
}
