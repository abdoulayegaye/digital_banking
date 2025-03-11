package com.banking.digitalbankingproject.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.Instant;

@Data
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private Instant createdAt;
    private Client client;
    private String typeCompte;
    private String statut;
    private LocalDate dateOuverture;
}