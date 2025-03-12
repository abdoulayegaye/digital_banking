package com.banking.digitalbankingproject.entity;

public class Compte {
    private int id;
    private String numero;
    private double balance;
    private String createdAt; // Ou LocalDate si vous utilisez Java 8+
    private int clientId;

    // Constructeurs
    public Compte() {}

    public Compte(int id, String numero, double balance, String createdAt, int clientId) {
        this.id = id;
        this.numero = numero;
        this.balance = balance;
        this.createdAt = createdAt;
        this.clientId = clientId;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
}