package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Transfert;
import com.banking.digitalbankingproject.service.ITransfert;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.time.Instant;

public class TransfertImpl implements ITransfert {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;



    public Transfert getId(String id) {
        Transfert transfert = new Transfert();
        transfert .setClient(new Client());
        transfert.setCompte(new Compte());
        String sql = "SELECT c.id, c.nom, a.acc_id , c.prenom, a.solde FROM account a JOIN customer c ON a.cust_id = c.id WHERE a.cust_id = ?";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, id);
            rs = db.executeSelect();
            if(rs.next()){
                Client client = new Client();
                transfert.getClient().setId(rs.getInt("c.id"));
                transfert.getClient().setNom(rs.getString("c.nom"));
                transfert.getClient().setPrenom(rs.getString("prenom"));
                transfert.getCompte().setSolde(rs.getDouble("solde"));
                transfert.getCompte().setNumero(rs.getString("acc_id"));



            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return transfert;
    }


    @Override
    public int senderMaj(Transfert transfert) {
       String sql ="update account set solde = solde - ? where acc_id=?";
       try {
           db.initPrepar(sql);
           db.getPstm().setDouble(1, transfert.getCompte().getSolde());
           db.getPstm().setString(2, transfert.getCompte().getNumero());
           ok = db.executeMaj();
       }
       catch (Exception e) {
           throw new RuntimeException(e);
       }
       return ok;
    }
    @Override
    public int recieverMaj(Transfert transfert) {
        String sql ="update account set solde= solde+? where acc_id=?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, transfert.getCompte().getSolde());
            db.getPstm().setString(2, transfert.getCompte().getNumero());
            ok = db.executeMaj();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ok;
    }
    @Override
    public int add(Transfert transfert) {
        String sql ="insert into transfer (F_account,T_account,balance,date) values(?,?,?,?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, transfert.getCompte().getNumero());
            db.getPstm().setString(2, transfert.getClient().getNom());
            db.getPstm().setDouble(3, transfert.getCompte().getSolde());
            db.getPstm().setString(4, transfert.getDate().toString());
            ok=db.executeMaj();
            if(ok==1){ Notification.NotifSuccess("bonne nouvelle","tranfert effetuer avec succes");}
            else{ Notification.NotifSuccess("hoho","ya erreur quelque part");}
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ok;
    }




    @Override
    public Instant date() {

        return Instant.now();
    }
}
