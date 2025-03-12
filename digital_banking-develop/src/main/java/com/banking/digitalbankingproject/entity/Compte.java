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

    public void setCreated_at(String createdAt) {
    }

    public void setClient_id(int clientId) {
    }
}
