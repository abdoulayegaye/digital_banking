package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.time.Instant;
@Data
public class Retrait {
    private int id;
    private Compte compte;
    private Client client;
    private double Solde;
    private Instant Date;
//getteur account
  public String getAcc_id(){
        return compte.getNumero();
    }
    //setteur compte
    public void setAcc_id(String id){
      if (compte == null)
      {
          compte= new Compte();
      }
      compte.setNumero(id);

    }
//getteur client
    public int getCust_id(){
        return client.getId();
    }
//setteur client
    public void setCust_id(int id){
        if (client == null)
        {
            client= new Client();
        }
        client.setId(id);
    }



}
