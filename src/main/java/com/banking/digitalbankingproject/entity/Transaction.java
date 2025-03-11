package com.banking.digitalbankingproject.entity;

import lombok.Getter;

@Getter
public class Transaction {
    private String date;
    private double montant;
    private String type;

    public Transaction(String date, double montant, String type) {
        this.date = date;
        this.montant = montant;
        this.type = type;
    }

}
