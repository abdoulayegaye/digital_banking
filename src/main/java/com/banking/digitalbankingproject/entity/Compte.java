package com.banking.digitalbankingproject.entity;

import lombok.Getter;

public class Compte {
    @Getter
    private int id;
    // Vérifie que ce getter est présent
    @Getter
    private String numero;
    // Assure-toi que le getter est bien défini
    @Getter
    private double balance;
    @Getter
    private String type;
    private String created_at;
    @Getter
    private Client client;
    private String statut;

    
    public Compte(int id, String numero, double balance, String type, String statut, String created_at, Client client) {
        this.id = id;
        this.numero = numero;
        this.balance = balance;
        this.type = type;
        this.created_at = created_at;
        this.client = client;
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Compte N°" + numero + " - " + (client != null ? client.getNom() + " " + client.getPrenom() : "Aucun client");
    }

    public String getCreatedAt() { return created_at; }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
