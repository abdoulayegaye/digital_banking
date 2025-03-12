package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.TypeOperation;

import java.time.LocalDateTime;

public class Operation {
    private int id;
    private LocalDateTime dateOp;
    private double amount;
    private TypeOperation type;
    private Compte compte;

    public Operation(int id, LocalDateTime dateOp, double amount, TypeOperation type, Compte compte) {
        this.id = id;
        this.dateOp = dateOp;
        this.amount = amount;
        this.type = type;
        this.compte = compte;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getDateOp() { return dateOp; }
    public void setDateOp(LocalDateTime dateOp) { this.dateOp = dateOp; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public TypeOperation getType() { return type; }
    public void setType(TypeOperation type) { this.type = type; }
    public Compte getCompte() { return compte; }
    public void setCompte(Compte compte) { this.compte = compte; }
}