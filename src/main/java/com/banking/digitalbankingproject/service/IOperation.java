package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;
//import java.Utils.List;

public interface IOperation {
    /**
     * Effectue un dépôt sur un compte
     * @param numeroCompte Le numéro du compte
     * @param amount Le montant à déposer
     * @throws IllegalArgumentException si le montant est négatif ou nul
     */
    void deposit(String numeroCompte, double amount);

    /**
     * Effectue un retrait sur un compte
     * @param numeroCompte Le numéro du compte
     * @param amount Le montant à retirer
     * @throws IllegalArgumentException si le montant est négatif ou nul
     * @throws IllegalStateException si le solde est insuffisant
     */
    void withdraw(String numeroCompte, double amount);

    /**
     * Récupère l'historique des opérations d'un compte
     * @param numeroCompte Le numéro du compte
     * @return La liste des opérations triées par date décroissante
     */
    List<Operation> getAccountHistory(String numeroCompte);

    /**
     * Génère un relevé PDF des opérations d'un compte
     * @param numeroCompte Le numéro du compte
     * @param directory Le répertoire où sauvegarder le fichier PDF
     * @return Le chemin complet du fichier PDF généré
     */
    String generatePdfStatement(String numeroCompte, String directory);

    void effectuerDepot(String numero, double montant);

    void effectuerRetrait(String numero, double montant);

    String genererReleve(String numero, String absolutePath);

    Operation getOperationsCompte(String numero);
}
