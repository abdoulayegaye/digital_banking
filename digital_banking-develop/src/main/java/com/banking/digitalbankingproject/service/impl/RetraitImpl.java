package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Depot;
import com.banking.digitalbankingproject.entity.Retrait;
import com.banking.digitalbankingproject.service.IRetrait;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.time.Instant;

public class RetraitImpl implements IRetrait {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

public int add(Retrait retrait) {
    String sql ="insert into retrait (acc_id,cust_id,balance,date) values(?,?,?,?)";
        try {
        db.initPrepar(sql);
        db.getPstm().setString(1,(retrait.getCompte().getNumero()));
        db.getPstm().setInt(2, retrait.getClient().getId());
        db.getPstm().setDouble(3, retrait.getCompte().getSolde());
        db.getPstm().setString(4, retrait.getDate().toString());
        ok = db.executeMaj();
        if (ok==1)
        {
            String sqli ="update account set solde =solde - ? where acc_id =?";
            try {
                db.initPrepar(sqli);
                db.getPstm().setDouble(1, retrait.getCompte().getSolde());
                db.getPstm().setString(2, retrait.getCompte().getNumero());
                ok=db.executeMaj();
                if(ok==1){ Notification.NotifSuccess("bonne nouvelle","retrait effetuer avec succes");}
                else{ Notification.NotifSuccess("hoho","ya erreur quelque part");}
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("error");
        }

    }
        catch (Exception e) {
        throw new RuntimeException(e);
    }
        return ok;
}



    @Override
    public Retrait getId(String id) {
        Retrait retrait = new Retrait();
        retrait.setClient(new Client());  // Initialisation du client
        retrait.setCompte(new Compte());  // Initialisation du compte

        String sql = "SELECT c.id, c.nom, a.acc_id , c.prenom, a.solde FROM account a JOIN customer c ON a.cust_id = c.id WHERE a.cust_id = ?";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, id);
            rs = db.executeSelect();
            if(rs.next()){
                Client client = new Client();
                retrait.getClient().setId(rs.getInt("c.id"));
                retrait.getClient().setNom(rs.getString("c.nom"));
                retrait.getClient().setPrenom(rs.getString("prenom"));
                retrait.getCompte().setSolde(rs.getDouble("solde"));
                retrait.getCompte().setNumero(rs.getString("acc_id"));



            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return retrait;
    }



public Instant date() {

    return Instant.now();
}
}



