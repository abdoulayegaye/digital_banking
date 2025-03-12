package com.banking.digitalbankingproject.entity;

public class Compte {
    private int id;
    private double solde;
    private int clientId;

    // Constructeur par défaut
    public Compte() {
        // Initialisation par défaut (optionnel)
    }

    // Constructeur personnalisé
    public Compte(int id, double solde, int clientId) {
        this.id = id;
        this.solde = solde;
        this.clientId = clientId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "Compte ID: " + id + " - Solde: " + solde + " - Client ID: " + clientId;
    }
}