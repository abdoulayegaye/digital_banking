package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.TypeCompte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe représentant un compte bancaire
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compte {
    /**
     * Identifiant unique du compte
     */
    private int id;
    
    /**
     * Numéro du compte
     */
    private String numero;
    
    /**
     * Solde actuel du compte
     */
    private double balance;
    
    /**
     * Date de création du compte
     */
    private Instant createdAt;
    
    /**
     * Client propriétaire du compte
     */
    private Client client;
    
    /**
     * Type de compte (Courant, Épargne, etc.)
     */
    private TypeCompte typeCompte;
    
    /**
     * Liste des opérations effectuées sur ce compte
     */
    private List<Operation> operations;
    
    /**
     * Constructeur avec les champs essentiels
     * 
     * @param numero Numéro du compte
     * @param balance Solde initial
     * @param client Propriétaire du compte
     * @param typeCompte Type de compte
     */
    public Compte(String numero, double balance, Client client, TypeCompte typeCompte) {
        this.numero = numero;
        this.balance = balance;
        this.client = client;
        this.typeCompte = typeCompte;
        this.createdAt = Instant.now();
    }
    
    /**
     * Effectue un dépôt sur le compte
     * 
     * @param montant Montant à déposer
     * @return true si l'opération est réussie
     */
    public boolean deposer(double montant) {
        if (montant <= 0) {
            return false;
        }
        this.balance += montant;
        return true;
    }
    
    /**
     * Effectue un retrait sur le compte
     * 
     * @param montant Montant à retirer
     * @return true si l'opération est réussie
     */
    public boolean retirer(double montant) {
        if (montant <= 0 || montant > this.balance) {
            return false;
        }
        this.balance -= montant;
        return true;
    }
    
    /**
     * Vérifie si le solde est suffisant pour un retrait
     * 
     * @param montant Montant à vérifier
     * @return true si le solde est suffisant
     */
    public boolean soldeDisponible(double montant) {
        return this.balance >= montant;
    }
    
    /**
     * Retourne la date de création formatée
     * 
     * @return Date formatée en chaîne de caractères
     */
    public String getDateCreation() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(this.createdAt, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
    
    /**
     * Retourne une représentation textuelle du compte
     * 
     * @return Informations du compte sous forme de chaîne
     */
    @Override
    public String toString() {
        return "Compte n°" + numero + " - Solde: " + balance + " €";
    }
}
