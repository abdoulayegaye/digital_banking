package com.banking.digitalbankingproject.entity;

public class Operation {
    private int id;
    private int compteId;
    private double montant;
    private String typeOperation;

    // Constructeur par défaut
    public Operation() {
        // Initialisation par défaut (optionnel)
    }

    // Constructeur personnalisé
    public Operation(int id, int compteId, double montant, String typeOperation) {
        this.id = id;
        this.compteId = compteId;
        this.montant = montant;
        this.typeOperation = typeOperation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompteId() {
        return compteId;
    }

    public void setCompteId(int compteId) {
        this.compteId = compteId;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    @Override
    public String toString() {
        return "Operation ID: " + id + " - Compte ID: " + compteId + " - Montant: " + montant + " - Type: " + typeOperation;
    }
}