package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.TypeOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation {
    private int id;
    private Instant dateOperation;
    private double amount;
    private TypeOperation type;
    private Compte compte;
    
    private static final Locale LOCALE_SENEGAL = new Locale("fr", "SN");
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                        .withLocale(LOCALE_SENEGAL)
                        .withZone(ZoneId.systemDefault());
    
    public Operation(double amount, TypeOperation type, Compte compte) {
        this.dateOperation = Instant.now();
        this.amount = amount;
        this.type = type;
        this.compte = compte;
    }
    
    public String getCompteNumero() {
        return compte != null ? compte.getNumero() : "";
    }
    
    public String getFormattedDate() {
        return dateOperation != null ? DATE_FORMATTER.format(dateOperation) : "";
    }
    
    public String getFormattedMontant() {
        String signe = type == TypeOperation.RETRAIT ? "-" : "+";
        return String.format("%s %,.0f FCFA", signe, amount);
    }
    
    public String getFormattedType() {
        switch (type) {
            case DEPOT:
                return "Dépôt";
            case RETRAIT:
                return "Retrait";
            case VIREMENT:
                return "Virement";
            default:
                return type.toString();
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s de %s sur le compte %s", 
            getFormattedDate(), getFormattedType(), getFormattedMontant(), getCompteNumero());
    }
}
