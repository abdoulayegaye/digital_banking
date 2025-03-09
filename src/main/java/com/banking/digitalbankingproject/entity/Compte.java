package com.banking.digitalbankingproject.entity;

import javafx.beans.property.*;

import java.time.Instant;

public class Compte {
    private final StringProperty numero;
    private final DoubleProperty solde;
    private final StringProperty proprietaire;
    private boolean actif;
    private Instant createdAt;
    private Client client;
    private int id;

    public Compte(String numero, double solde, boolean actif, Instant createdAt, Client client) {
        this.numero = new SimpleStringProperty(numero);
        this.solde = new SimpleDoubleProperty(solde);
        this.actif = actif;
        this.createdAt = createdAt;
        this.client = client;
        this.proprietaire = new SimpleStringProperty(client != null ? client.getNom() + " " + client.getPrenom() : "");
    }

    // Constructor for operations
    public Compte(String numero, double solde, String proprietaire) {
        this.numero = new SimpleStringProperty(numero);
        this.solde = new SimpleDoubleProperty(solde);
        this.proprietaire = new SimpleStringProperty(proprietaire);
        this.actif = true;
        this.createdAt = Instant.now();
    }

    // Default constructor
    public Compte() {
        this.numero = new SimpleStringProperty();
        this.solde = new SimpleDoubleProperty();
        this.proprietaire = new SimpleStringProperty();
        this.actif = true;
        this.createdAt = Instant.now();
    }

    // Getters and Setters
    public String getNumero() {
        return numero.get();
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public StringProperty numeroProperty() {
        return numero;
    }

    public double getSolde() {
        return solde.get();
    }

    public void setSolde(double solde) {
        this.solde.set(solde);
    }

    public DoubleProperty soldeProperty() {
        return solde;
    }

    public String getProprietaire() {
        return proprietaire.get();
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire.set(proprietaire);
    }

    public StringProperty proprietaireProperty() {
        return proprietaire;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Additional methods for balance
    public double getBalance() {
        return getSolde();
    }

    public void setBalance(double balance) {
        setSolde(balance);
    }
}