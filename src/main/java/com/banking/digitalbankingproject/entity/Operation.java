package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.TypeOperation;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
public class Operation {
    private int id;
    private Instant dateOp;
    private double amount;
    private TypeOperation type;
    private Compte compte;

    public Operation() {
    }

    public Operation(int id, Instant dateOp, double amount, TypeOperation type, Compte compte) {
        this.id = id;
        this.dateOp = dateOp;
        this.amount = amount;
        this.type = type;
        this.compte = compte;
    }
}