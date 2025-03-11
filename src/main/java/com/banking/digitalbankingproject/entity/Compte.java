package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private Date created_at;
    private int client_id;

    public Compte() {
    }

    public Compte(int id, String numero, double balance, Date created_at, int client_id) {
        this.id = id;
        this.numero = numero;
        this.balance = balance;
        this.created_at = created_at;
        this.client_id = client_id;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }
}