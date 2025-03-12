package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.TypeOperation;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Classe représentant une opération bancaire
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Operation {
    /**
     * Identifiant unique de l'opération
     */
    private int id;
    
    /**
     * Date et heure de l'opération
     */
    private Instant dateOp;
    
    /**
     * Montant de l'opération
     */
    private double amount;
    
    /**
     * Type d'opération (DEPOT, RETRAIT, VIREMENT)
     */
    private TypeOperation type;
    
    /**
     * Compte sur lequel l'opération est effectuée
     */
    private Compte compte;
    
    /**
     * Description de l'opération
     */
    private String description;
    
    /**
     * Compte destinataire en cas de virement
     */
    private Compte compteDestination;
    
    /**
     * Constructeur pour les opérations simples (dépôt, retrait)
     * 
     * @param amount Montant de l'opération
     * @param type Type d'opération
     * @param compte Compte concerné
     * @param description Description de l'opération
     */
    public Operation(double amount, TypeOperation type, Compte compte, String description) {
        this.dateOp = Instant.now();
        this.amount = amount;
        this.type = type;
        this.compte = compte;
        this.description = description;
    }
    
    /**
     * Constructeur pour les virements
     * 
     * @param amount Montant du virement
     * @param compteSource Compte source
     * @param compteDestination Compte destinataire
     * @param description Description du virement
     */
    public Operation(double amount, Compte compteSource, Compte compteDestination, String description) {
        this.dateOp = Instant.now();
        this.amount = amount;
        this.type = TypeOperation.VIREMENT;
        this.compte = compteSource;
        this.compteDestination = compteDestination;
        this.description = description;
    }
    
    /**
     * Retourne la date de l'opération formatée
     * 
     * @return Date formatée en chaîne de caractères
     */
    public String getDateFormatee() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(this.dateOp, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
    
    /**
     * Retourne le signe de l'opération (+ pour dépôt, - pour retrait ou virement)
     * 
     * @return Signe de l'opération
     */
    public String getSigne() {
        return this.type == TypeOperation.DEPOT ? "+" : "-";
    }
    
    /**
     * Retourne le libellé de l'opération
     * 
     * @return Libellé formaté
     */
    public String getLibelle() {
        switch (this.type) {
            case DEPOT:
                return "Dépôt - " + this.description;
            case RETRAIT:
                return "Retrait - " + this.description;
            case VIREMENT:
                String destinataire = compteDestination != null ? 
                    "vers compte " + compteDestination.getNumero() : "";
                return "Virement " + destinataire + " - " + this.description;
            default:
                return this.description;
        }
    }
    
    /**
     * Vérifie si l'opération est un débit (retrait ou virement)
     * 
     * @return true si c'est un débit
     */
    public boolean isDebit() {
        return this.type == TypeOperation.RETRAIT || this.type == TypeOperation.VIREMENT;
    }
    
    /**
     * Vérifie si l'opération est un crédit (dépôt)
     * 
     * @return true si c'est un crédit
     */
    public boolean isCredit() {
        return this.type == TypeOperation.DEPOT;
    }
}
