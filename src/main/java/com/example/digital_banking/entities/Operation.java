package com.example.digital_banking.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

public class Operation {
    private Long id;
    private LocalDateTime dateOperation;
    private DoubleProperty montant;
    private StringProperty type; // DEPOT, RETRAIT, VIREMENT
    private Compte compte;
    private Compte compteDestination; // Pour les virements

    public Operation() {
        this.montant = new SimpleDoubleProperty();
        this.type = new SimpleStringProperty();
        this.dateOperation = LocalDateTime.now();
    }

    public Operation(double montant, String type, Compte compte) {
        this.montant = new SimpleDoubleProperty(montant);
        this.type = new SimpleStringProperty(type);
        this.dateOperation = LocalDateTime.now();
        this.compte = compte;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public double getMontant() {
        return montant.get();
    }

    public DoubleProperty montantProperty() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant.set(montant);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Compte getCompteDestination() {
        return compteDestination;
    }

    public void setCompteDestination(Compte compteDestination) {
        this.compteDestination = compteDestination;
    }
} 