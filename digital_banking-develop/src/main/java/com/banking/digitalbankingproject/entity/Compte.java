package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class Compte {
    private int id;
    private String Numero;
    private double Solde;
    private Instant Date;
    private Client client;

    public void setCNom( String id){
        if (client ==null)
        {
            client = new Client();
        }
        client.setNom(id);
    }

    public String getCNom(){
        return client.getNom();
    }
}
