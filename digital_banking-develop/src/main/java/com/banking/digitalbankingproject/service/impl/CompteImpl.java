package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CompteImpl implements ICompte {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

//verifie si utilisateur a deja un account avant de push dans la base
    @Override
    public int Add(Compte compte,String id) {
        String sqli = "select * from account where cust_id = ?";
        try {
            db.initPrepar(sqli);
            db.getPstm().setString(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                Notification.NotifError("hoho","utilisateur deja indexer");
            }
            else {
                String sql = "insert into account(acc_id,solde,date,cust_id) values(?,?,?,?)";
                try{
                    db.initPrepar(sql);
                    db.getPstm().setString(1,compte.getNumero());
                    db.getPstm().setDouble(2,compte.getSolde());
                    db.getPstm().setString(3,compte.getDate().toString());
                    db.getPstm().setInt(4,compte.getClient().getId());
                    ok=db.executeMaj();

                    if (ok==1)
                    {
                        Notification.NotifSuccess("bonne nouvelle","compte ajouter avec succees");
                    }else { Notification.NotifError("hoho","ya erreur quelque part");}
                }
                catch(Exception e) {
                    System.out.println("eroor"+e.getMessage());
                }
            }
        }
        catch (Exception e) {
            System.out.println("eroor"+e.getMessage());
        }



       return ok;
    }

    @Override
    public int get(String Id) {
       String sql = "select * from customer where id=?";
       try {
           db.initPrepar(sql);
           db.getPstm().setString(1,Id);
           rs=db.executeSelect();
           if (rs.next())
           {
               Notification.NotifSuccess("bonne nouvelle","utilisateur trouver");
           }else { Notification.NotifError("Error", "utilisateur inexistant");}
       }
       catch(Exception e) {
           System.out.println("eroor"+e.getMessage());
       }
return ok;
    }
//utiliser dans le relever pour obtenir des infomations du client
    public Compte getidrev(String Id) {
        String sql = "select * from account where cust_id=?";
        Compte compte = new Compte();

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1,Id);
            rs=db.executeSelect();
            if (rs.next())
            {

                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("acc_id"));
                compte.setSolde(rs.getDouble("solde"));

                Notification.NotifSuccess("bonne nouvelle","utilisateur trouver");
            }else { Notification.NotifError("Error", "utilisateur inexistant");}
        }
        catch(Exception e) {
            System.out.println("eroor"+e.getMessage());
        }
        return compte;
    }


//utiliser dans le solde pour avoir les infos du client
    public Compte getidsolde(String Id) {
        String sql = "select a.id,a.acc_id,a.solde,c.nom from account a JOIN customer c on a.cust_id = c.id where a.id=?";
        Compte compte = new Compte();
        Client client = new Client();
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1,Id);
            rs=db.executeSelect();
            if (rs.next())
            {

                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("acc_id"));
                compte.setSolde(rs.getDouble("solde"));
                compte.setCNom(rs.getString("nom"));
                Notification.NotifSuccess("bonne nouvelle","utilisateur trouver");
            }else { Notification.NotifError("Error", "utilisateur inexistant");}
        }
        catch(Exception e) {
            System.out.println("eroor"+e.getMessage());
        }
        return compte;
    }
    //supprimer un client
    public int supp(String Id) {
        String sql = "delete  from account where id=?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1,Id);
            ok=db.executeMaj();
            if (ok==1)
            {
                Notification.NotifSuccess("bonne nouvelle","compte supprimer avec succes");
            }else { Notification.NotifError("hoho", "ya erreur quelque part");}
        }
        catch(Exception e) {
            System.out.println("eroor"+e.getMessage());
        }
        return ok;
    }


    @Override
    public Instant date() {

        return Instant.now();
    }


}
