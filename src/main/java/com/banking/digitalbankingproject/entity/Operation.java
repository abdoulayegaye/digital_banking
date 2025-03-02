package com.banking.digitalbankingproject.entity;

import java.time.LocalDateTime;

public class Operation {
    private int id;
    private LocalDateTime date;
    private String description;
    private double montant;
    private double solde;
    private String type;
    private Compte compte;

    public Operation() {}

    public Operation(LocalDateTime date, String description, double montant, double solde, String type, Compte compte) {
        this.date = date;
        this.description = description;
        this.montant = montant;
        this.solde = solde;
        this.type = type;
        this.compte = compte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }
}