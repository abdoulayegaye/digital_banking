package com.example.digital_banking.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Compte {
    private Long id;
    private StringProperty numeroCompte;
    private DoubleProperty solde;
    private LocalDate dateOuverture;
    private Client client;

    public Compte() {
        this.numeroCompte = new SimpleStringProperty();
        this.solde = new SimpleDoubleProperty(0.0);
        this.dateOuverture = LocalDate.now();
    }

    public Compte(String numeroCompte, double solde, Client client) {
        this.numeroCompte = new SimpleStringProperty(numeroCompte);
        this.solde = new SimpleDoubleProperty(solde);
        this.dateOuverture = LocalDate.now();
        this.client = client;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCompte() {
        return numeroCompte.get();
    }

    public StringProperty numeroCompteProperty() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte.set(numeroCompte);
    }

    public double getSolde() {
        return solde.get();
    }

    public DoubleProperty soldeProperty() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde.set(solde);
    }

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
} 