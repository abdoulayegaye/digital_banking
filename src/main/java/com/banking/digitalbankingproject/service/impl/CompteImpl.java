package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private ClientImpl clientImpl = new ClientImpl();

    @Override
    public int creerCompte(Compte compte) {
        String sql = "INSERT INTO comptes VALUES(NULL, ?, ?,?,?,?)";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setString(3, "ACTIF");
            db.getPstm().setTimestamp(4, Timestamp.from(compte.getCreatedAt()));
            db.getPstm().setInt(5   , compte.getClient().getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int fermerCompte(String numeroCompte) {
        String sql = "UPDATE comptes SET etat = ? WHERE numero = ?";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, "FERMER");
            db.getPstm().setString(2,numeroCompte);
            ok=db.executeMaj();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ok;
    }

    @Override
    public Compte consulterCompte(String numeroCompte) {
        Compte compte = null;
        String sql = "SELECT * FROM comptes WHERE numero = ?";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setEtat(rs.getString("etat"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                compte.setClient(clientImpl.obtenirClient(rs.getInt("client_id")));
            }
            db.closeConnection();
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return compte;
    }

    @Override
    public List<Compte> obtenirTousLesComptes() {
        List<Compte> comptes = new ArrayList<Compte>();
        String sql ="SELECT * FROM comptes WHERE etat=? ORDER BY numero ASC";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "ACTIF");
            rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setEtat(rs.getString("etat"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                compte.setClient(clientImpl.obtenirClient(rs.getInt("client_id")));
                comptes.add(compte);
            }
            db.closeConnection();
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return comptes;
    }
}
