package com.banking.digitalbankingproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compte {
    private int id;
    private String numero;
    private double solde;
    private Instant dateCreation;
    private Client client;
    
    private static final Locale LOCALE_SENEGAL = new Locale("fr", "SN");
    
    public Compte(String numero, double solde, Instant dateCreation, Client client) {
        this.numero = numero;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.client = client;
    }
    
    public String getClientNom() {
        return client != null ? client.toString() : "";
    }
    
    public String getFormattedSolde() {
        return String.format(LOCALE_SENEGAL, "%,.0f FCFA", solde);
    }
    
    @Override
    public String toString() {
        return numero + " - " + getClientNom();
    }
}
