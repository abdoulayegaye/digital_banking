package com.banking.digitalbankingproject.entity;

import java.time.LocalDate;

public class Compte {
    private int id;
    private String numero;
    private double balance;
    private LocalDate created_at;
    private Client client;

    public Compte(String numero, double balance, LocalDate created_at, Client client) {
        this.numero = numero;
        this.balance = balance;
        this.created_at = created_at;
        this.client = client;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numero = numeroCompte;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", balance=" + balance +
                ", createdAt=" + created_at +
                ", client=" + (client != null ? client.getPrenom() + " " + client.getNom() : "null") +
                '}';
    }
}
