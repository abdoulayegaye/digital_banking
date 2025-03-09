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

}
