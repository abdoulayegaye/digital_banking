package com.banking.digitalbankingproject.entity;

import java.time.Instant;

public class Compte {
    private String numero;
    private double balance;
    private boolean actif;
    private Instant createdAt;
    private Client client;

    public Compte() { }

    public Compte(String numero, double balance, boolean actif, Instant createdAt, Client client) {
        this.numero = numero;
        this.balance = balance;
        this.actif = actif;
        this.createdAt = createdAt;
        this.client = client;
    }

    // Getters & Setters
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}
