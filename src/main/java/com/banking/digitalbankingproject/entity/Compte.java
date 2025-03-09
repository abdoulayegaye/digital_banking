package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private Instant createdAt;
    private Client client;

    public Compte() {
    }
    public Compte(int id, String numero, double balance, Instant createdAt, Client client) {
        this.id = id;
        this.numero = numero;
        this.balance = balance;
        this.createdAt = createdAt;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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
}
