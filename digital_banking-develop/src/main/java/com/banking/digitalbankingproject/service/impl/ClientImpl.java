package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientImpl implements IClient {

    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    Client client = new Client();


    @Override
    public int Add(Client client) {
        // TODO Auto-generated method stub
        String sql="INSERT INTO customer(nom,prenom,email ) VALUES (?,?,?) ";
        try
        {
            db.initPrepa(sql);
            db.getPstm().setString(1,client.getNom());
            db.getPstm().setString(2,client.getPrenom());
            db.getPstm().setString(3,client.getEmail());
            ok=db.executeMaj();
            if (ok==1)
            {
                Notification.NotifSuccess("bonne nouvelle","client ajouter");
            }else { Notification.NotifSuccess("hoho","ya erreur quelque part");}
        }
        catch(SQLException e) {
            System.out.println("eroor"+e.getMessage());
        }

        return ok;
    }

    @Override
    public int Update(Client client) {

        String sql = "UPDATE customer set  nom = ?, prenom = ? , email= ?  WHERE id= ?";
        try {
            db.initPrepa(sql);
            //db.getPstm().setString(1,etudiant.getMatricule());
            db.getPstm().setString(1,client.getNom());
            db.getPstm().setString(2,client.getPrenom());
            db.getPstm().setString(3,client.getEmail());
            db.getPstm().setInt(4, client.getId());

            ok=db.executeMaj();

            if (ok==1)
            {
                Notification.NotifSuccess("bonne nouvelle","mise a jour effectuer");
            }else { Notification.NotifSuccess("hoho","ya erreur quelque part");}
        }
        catch(Exception e) {
            System.out.println("eroor"+e.getMessage());
        }

        return ok;
    }



    @Override
    public int Delete(String id) {
       String sql= "DELETE FROM customer WHERE id= ?";
       try {
           db.initPrepa(sql);
           db.getPstm().setString(1,id);
          int ok = db.executeMaj();
           if (ok==1)
           {
               Notification.NotifSuccess("bonne nouvelle","suppresion reussi");
           }else { Notification.NotifSuccess("hoho","ya erreur quelque part");}
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return ok;
    }


    @Override
    public Client get(String id) {
        Client client = new Client();
        String sql="SELECT * FROM customer WHERE id = ?";
        try {

            db.initPrepa(sql);
            db.getPstm().setString(1,id);
            rs=db.executeSelect();

            if (rs.next())
            {


                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));


            }
        }
        catch(Exception e)
        {
            System.out.println("eroor"+e.getMessage());
        }
        return client;
    }


    }



