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
    private Integer id;
    
    /**
     * Numéro du compte
     */
    private String numero;
    
    /**
     * Solde actuel du compte
     */
    private Double solde;
    
    /**
     * Date de création du compte
     */
    private String dateCreation;
    
    /**
     * Client propriétaire du compte
     */
    private Integer clientId;
    
    /**
     * Nom du client propriétaire du compte
     */
    private String clientNom;
    
    /**
     * Type de compte (Courant, Épargne, etc.)
     */
    private String type;
    
    /**
     * Description du compte
     */
    private String description;
    
    /**
     * Constructeur avec les champs essentiels
     * 
     * @param numero Numéro du compte
     * @param solde Solde initial
     * @param clientId Identifiant du client propriétaire
     * @param type Type de compte
     */
    public Compte(String numero, Double solde, Integer clientId, String type) {
        this.numero = numero;
        this.solde = solde;
        this.clientId = clientId;
        this.type = type;
        this.dateCreation = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
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
        this.solde += montant;
        return true;
    }
    
    /**
     * Effectue un retrait sur le compte
     * 
     * @param montant Montant à retirer
     * @return true si l'opération est réussie
     */
    public boolean retirer(double montant) {
        if (montant <= 0 || montant > this.solde) {
            return false;
        }
        this.solde -= montant;
        return true;
    }
    
    /**
     * Vérifie si le solde est suffisant pour un retrait
     * 
     * @param montant Montant à vérifier
     * @return true si le solde est suffisant
     */
    public boolean soldeDisponible(double montant) {
        return this.solde >= montant;
    }
    
    /**
     * Retourne la date de création formatée
     * 
     * @return Date formatée en chaîne de caractères
     */
    public String getDateCreation() {
        return dateCreation;
    }
    
    /**
     * Retourne une représentation textuelle du compte
     * 
     * @return Informations du compte sous forme de chaîne
     */
    @Override
    public String toString() {
        return "Compte n°" + numero + " - Solde: " + solde + " €";
    }

    public Client getClient() {
        return null;
    }

    public void setClient(Client value) {
        if (value != null) {
            this.clientId = value.getId();
            this.clientNom = value.getNom() + " " + value.getPrenom();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getBalance() {
        return solde;
    }

    public void updateBalance(Double balance) {
        this.solde = balance;
    }

    public Double calculateNewBalance(Double amount) {
        return this.solde + amount;
    }

    public Instant getCreatedAt() {
            return null;
    }

    public void setTypeCompte(TypeCompte typeCompte) {

    }

    public void setBalance(double balance) {

    }
}
