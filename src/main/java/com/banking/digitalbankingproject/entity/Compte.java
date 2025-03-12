package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private Timestamp createdAt;
    private Client client;
}
