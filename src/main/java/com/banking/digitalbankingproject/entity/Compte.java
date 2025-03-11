package com.banking.digitalbankingproject.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
public class Compte {
    private int id;
    private String numero;
    private Double balance = null;
    private Timestamp createdAt;
    private Client client;
}
