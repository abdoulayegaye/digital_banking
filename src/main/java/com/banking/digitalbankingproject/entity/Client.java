package com.banking.digitalbankingproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Classe représentant un client de la banque
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    /**
     * Identifiant unique du client
     */
    private Integer id;
    
    /**
     * Nom du client
     */
    private String nom;
    
    /**
     * Prénom du client
     */
    private String prenom;
    
    /**
     * Adresse email du client
     */
    private String email;
    
    /**
     * Numéro de téléphone du client
     */
    private String telephone;
    
    /**
     * Adresse postale du client
     */
    private String adresse;
    
    /**
     * Liste des comptes associés au client
     */
    private List<Compte> comptes;
    
    /**
     * Constructeur avec les champs obligatoires
     * 
     * @param nom Nom du client
     * @param prenom Prénom du client
     * @param email Email du client
     */
    public Client(Integer id, String nom, String prenom, String email, String telephone, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }
    
    /**
     * Retourne le nom complet du client
     * 
     * @return Nom et prénom du client
     */
    public String getNomComplet() {
        return this.nom + " " + this.prenom;
    }
    
    /**
     * Vérifie si le client possède des comptes
     * 
     * @return true si le client a au moins un compte, false sinon
     */
    public boolean hasComptes() {
        return comptes != null && !comptes.isEmpty();
    }
    
    /**
     * Retourne le nombre de comptes du client
     * 
     * @return Nombre de comptes
     */
    public int getNombreComptes() {
        return comptes != null ? comptes.size() : 0;
    }
}
