package com.banking.digitalbankingproject.entity;

import java.time.LocalDateTime;

public class Compte {
    private int id;
    private String numero;
    private double balance;
    private LocalDateTime createdAt;
    private Client client;

    public Compte(int id, String numero, double balance, LocalDateTime createdAt, Client client) {
        this.id = id;
        this.numero = numero;
        this.balance = balance;
        this.createdAt = createdAt;
        this.client = client;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}