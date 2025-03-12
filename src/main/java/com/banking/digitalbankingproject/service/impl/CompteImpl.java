package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {

    private Db db = new Db();
    private ResultSet rs;
    private int ok ;
    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<Compte>();
        String sql = "select * from comptes";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                IClient client = new ClientImpl();
                compte.setId(rs.getInt("id"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setNumero(rs.getString("numero"));
                compte.setCreatedAt(rs.getTimestamp("created_at"));
                compte.setClient(client.getClientById(rs.getInt("client_id")));
                comptes.add(compte);
            }
            db.closeConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return comptes;
    }

    @Override
    public boolean ajouterCompte(Compte compte) {
        String sql = "insert into comptes (balance,numero,client_id) values(?,?,?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(2, compte.getNumero());
            db.getPstm().setDouble(1, compte.getBalance());
            db.getPstm().setInt(3, compte.getClient().getId());
            ok = db.getPstm().executeUpdate();
            db.closeConnection();
        }catch(Exception e) {
            e.printStackTrace();

        }

        return ok==1;
    }

    @Override
    public boolean supprimerCompte(Compte compte) {
        String sql = "delete from comptes where id = ?";
        try {
            db.initPrepar(sql);

            db.getPstm().setInt(1,compte.getId());
            ok=db.getPstm().executeUpdate();
            db.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }
        return ok == 1;
    }

    @Override
    public Compte getCompte(String numero) {
        String sql = "select * from comptes where numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numero);
            rs = db.executeSelect();
            Compte compte = new Compte();
            while (rs.next()){
                compte.setId(rs.getInt("id"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setNumero(rs.getString("numero"));
                compte.setCreatedAt(rs.getTimestamp("created_at"));
                compte.setClient(new ClientImpl().getClientById(rs.getInt("client_id")));
            }

            db.closeConnection();
            return compte;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Compte getCompteById(int id) {
        String sql = "select * from comptes where id=?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            Compte compte = new Compte();
            while (rs.next()){
                compte.setId(rs.getInt("id"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setNumero(rs.getString("numero"));
                compte.setCreatedAt(rs.getTimestamp("created_at"));
                compte.setClient(new ClientImpl().getClientById(rs.getInt("client_id")));
            }

            db.closeConnection();
            return compte;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
