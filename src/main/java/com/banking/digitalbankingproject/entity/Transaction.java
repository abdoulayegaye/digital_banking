package com.banking.digitalbankingproject.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private String type;
    private double montant;
    private LocalDateTime date; // Correction

    public Transaction(int id, String type, double montant, LocalDateTime date) {
        this.id = id;
        this.type = type;
        this.montant = montant;
        this.date = date;
    }

    public Transaction(int id, int compteId, String type, double montant, Instant instant) {
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getMontant() { return montant; }
    public LocalDateTime getDate() { return date; }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
    }
}
