package com.banking.digitalbankingproject.entity;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private Instant createdAt;
    private Client client;
    private boolean statut;
    private String type_compte;

    public boolean isStatut() { return statut; }
    public void setStatut(boolean statut) { this.statut = statut; }

    public Compte(String numero) {
        this.numero = numero;
    }
    public Compte(int id) {
        this.id = id;
    }

    public double getSolde() {
        return balance;
    }

    public void setSolde(double solde) {
        this.balance = solde;
    }

    public String getTypeCompte() {
        return type_compte;
    }
}
