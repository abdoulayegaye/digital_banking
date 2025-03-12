package com.banking.digitalbankingproject.entity;

import javafx.beans.property.*;

import java.time.Instant;

public class Operation {

    private IntegerProperty id;
    private ObjectProperty<Instant> dateOp;  // Utilisation d'ObjectProperty pour Instant
    private DoubleProperty amount;  // Utilisation de DoubleProperty pour le montant
    private StringProperty type;  // Utilisation de StringProperty pour le type
    private ObjectProperty<Compte> compte;  // Lien avec l'objet Compte

    // Enum pour les types d'opération
    public enum TypeOperation {
        DEPOT, RETRAIT
    }

    // Constructeur
    public Operation(int id, Instant dateOp, double amount, TypeOperation type, Compte compte) {
        this.id = new SimpleIntegerProperty(id);
        this.dateOp = new SimpleObjectProperty<>(dateOp);
        this.amount = new SimpleDoubleProperty(amount);
        this.type = new SimpleStringProperty(type.name());  // Convertit l'enum en String
        this.compte = new SimpleObjectProperty<>(compte);
    }

    // Getters et setters pour les propriétés
    public IntegerProperty getIdProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public ObjectProperty<Instant> getDateOpProperty() {
        return dateOp;
    }

    public Instant getDateOp() {
        return dateOp.get();
    }

    public void setDateOp(Instant dateOp) {
        this.dateOp.set(dateOp);
    }

    public DoubleProperty getAmountProperty() {
        return amount;
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public String getType() {
        return type.get();
    }

    public void setType(TypeOperation type) {
        this.type.set(type.name());  // Sauvegarde de l'enum sous forme de String
    }

    public ObjectProperty<Compte> getCompteProperty() {
        return compte;
    }

    public Compte getCompte() {
        return compte.get();
    }

    public void setCompte(Compte compte) {
        this.compte.set(compte);
    }
}
