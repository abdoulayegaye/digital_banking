package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.time.Instant;
@Data
public class Depot {
    private int id;
    private Compte compte;
    private Client client;
    private double Solde;
    private Instant Date;

    public String getAcc_id() {
         return compte.getNumero();

    }

    // Setter pour acc_id
    public void setAcc_id(String acc_id) {
        if (compte == null) {
            compte = new Compte();
        }
        compte.setNumero(acc_id);
    }

    // Getter pour cust_id
    public int getCust_id() {
        return client.getId() ;
    }

    // Setter pour cust_id
    public void setCust_id(int cust_id) {
        if (client == null) {
            client = new Client();
        }
        client.setId(cust_id);
    }
}
