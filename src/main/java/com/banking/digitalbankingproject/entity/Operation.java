package com.banking.digitalbankingproject.entity;

import java.time.LocalDateTime;

public class Operation {
    private LocalDateTime date;
    private String description;
    private double montant;
    private double solde;
    // Ajout de la relation avec Compte
    private Compte compte;

    public Operation() { }

    public Operation(LocalDateTime date, String description, double montant, double solde, Compte compte) {
        this.date = date;
        this.description = description;
        this.montant = montant;
        this.solde = solde;
        this.compte = compte;
    }

    // Getters & Setters pour date, description, montant et solde
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getMontant() {
        return montant;
    }
    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getSolde() {
        return solde;
    }
    public void setSolde(double solde) {
        this.solde = solde;
    }

    // Ajout du getter et setter pour le compte associé
    public Compte getCompte() {
        return compte;
    }
    public void setCompte(Compte compte) {
        this.compte = compte;
    }
}
