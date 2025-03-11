package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Depot;
import com.banking.digitalbankingproject.service.IDepot;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.time.Instant;

public class DepotImpl implements IDepot {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public Depot getId(String id) {
        Depot depot = new Depot();
        depot.setClient(new Client());  // Initialisation du client
        depot.setCompte(new Compte());  // Initialisation du compte

        String sql = "SELECT c.id, c.nom, a.acc_id , c.prenom, a.solde FROM account a JOIN customer c ON a.cust_id = c.id WHERE a.cust_id = ?";
       try{
           db.initPrepar(sql);
           db.getPstm().setString(1, id);
           rs = db.executeSelect();
           if(rs.next()){
Client client = new Client();
               depot.getClient().setId(rs.getInt("c.id"));
               depot.getClient().setNom(rs.getString("c.nom"));
               depot.getClient().setPrenom(rs.getString("prenom"));
               depot.getCompte().setSolde(rs.getDouble("solde"));
               depot.getCompte().setNumero(rs.getString("acc_id"));



           }

       }
       catch (Exception e) {
           throw new RuntimeException(e);
       }
       return depot;
    }



    public Depot get(String id) {
        Depot depot = new Depot();
        depot.setClient(new Client());  // Initialisation du client
        depot.setCompte(new Compte());  // Initialisation du compte

        String sql = "SELECT c.*,a.* FROM account a JOIN customer c ON a.cust_id = c.id";
        try{
            db.initPrepar(sql);
            rs = db.executeSelect();




            }


        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return depot;
    }



    @Override
    public int add(Depot depot) {





        String sql ="insert into depot (acc_id,cust_id,balance,date) values(?,?,?,?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1,(depot.getCompte().getNumero()));
            db.getPstm().setInt(2, depot.getClient().getId());
            db.getPstm().setDouble(3, depot.getCompte().getSolde());
            db.getPstm().setString(4, depot.getDate().toString());
            ok = db.executeMaj();
            if (ok==1)
            {
                String sqli ="update account set solde =solde + ? where acc_id =?";
                try {
                  db.initPrepar(sqli);
                  db.getPstm().setDouble(1, depot.getCompte().getSolde());
                  db.getPstm().setString(2, depot.getCompte().getNumero());
                  ok=db.executeMaj();
                  if(ok==1){ Notification.NotifSuccess("bonne nouvelle","depot effetuer avec succes");}
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
    public Instant date() {

        return Instant.now();
    }

}
