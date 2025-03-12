package com.banking.digitalbankingproject.entity;

import java.time.LocalDate;

public class Compte {
    private String numeroCompte;
    private double solde;
    private LocalDate dateOuverture;
    private String client;

    public Compte(String numeroCompte, double solde, LocalDate dateOuverture, String client) {
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.dateOuverture = dateOuverture;
        this.client = client;
    }

    public Compte(String nom, String prenom, double solde) {

    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "numeroCompte='" + numeroCompte + '\'' +
                ", solde=" + solde +
                ", dateOuverture=" + dateOuverture +
                ", client='" + client + '\'' +
                '}';
    }
}