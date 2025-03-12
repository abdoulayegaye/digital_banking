package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface IOperation {

    // Effectuer un dépôt
    void effectuerDepot(Compte compte, double amount);

    // Effectuer un retrait
    void effectuerRetrait(Compte compte, double amount);

    // Consulter l'historique des opérations pour un compte
    List<Operation> consulterHistorique(Compte compte);

    // Nouvelle méthode : Récupérer les opérations par compte
    List<Operation> getOperationsByCompte(Compte compte);
}
