
package com.banking.digitalbankingproject.entity;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Operation {
    @Getter
    private int id;
    @Getter
    private int compteId;
    @Getter
    private String type;
    @Getter
    private double montant;
    private Timestamp dateOp;
    @Getter
    private String numeroCompte;

    public Operation(int id, int compteId, String type, double montant, Timestamp dateOp, String numeroCompte) {
        this.id = id;
        this.compteId = compteId;
        this.type = type;
        this.montant = montant;
        this.dateOp = Timestamp.valueOf(dateOp.toLocalDateTime());
        this.numeroCompte = numeroCompte;
    }


    public String getFormattedDate() {
        return dateOp != null ? DateTimeFormatter.ISO_INSTANT.format((TemporalAccessor) dateOp) : "";
    }

    public Timestamp getDate() {
        return dateOp;
    }}


