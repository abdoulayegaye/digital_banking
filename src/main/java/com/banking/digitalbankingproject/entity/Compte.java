package com.banking.digitalbankingproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private Timestamp createdAt;
    private int clientId;
    private Client client; // Pour stocker les informations du client associé

    @Override
    public String toString() {
        return "Compte N°" + numero + " - Solde: " + balance;
    }
}
