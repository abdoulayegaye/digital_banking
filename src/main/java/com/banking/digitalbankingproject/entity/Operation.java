package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.TypeOperation;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Operation {
    private int id;
    private Instant dateOp;
    private double amount;
    private TypeOperation type;
    private Compte compte;

    public Operation(String dépôt, double montant, String numeroCompte) {
    }

    public void setCompteId(int compteId) {
    }

    public void setType(String type) {
    }

    public void setDateOp(String dateOp) {
    }

    public Object getNumeroCompte() {
        return null;
    }
}
