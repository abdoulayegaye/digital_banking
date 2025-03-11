package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.time.Instant;
@Data
public class Transfert {
    private int id;
    private Compte compte;
    private Client client;
    private Instant Date;

    public String getF_account() {
        return compte.getNumero();
    }
    public String getT_account() {
        return client.getNom();
    }
    public Double getSolde() {
        return compte.getSolde();
    }


    public void setNumero(String numero) {
        if (compte == null) {
            compte = new Compte();
        }
        compte.setNumero(numero);
    }

    public void setSolde(Double Solde) {
        if (compte == null) {
            compte = new Compte();
        }
        compte.setSolde(Solde);
    }


    public void setNom(String Nom) {
        if (client == null) {
            client = new Client();
        }
        client.setNom(Nom);
    }




}

