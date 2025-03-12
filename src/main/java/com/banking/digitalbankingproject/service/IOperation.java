package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;
import java.util.List;

public interface IOperation {
    // Effectuer un dépôt
    boolean deposer(String numeroCompte, double montant);

    // Effectuer un retrait
    boolean retirer(String numeroCompte, double montant);

    // Effectuer un virement
    boolean virement(String compteSource, String compteDestination, double montant);

    // Récupérer toutes les opérations
    List<Operation> getAllOperations();

    // Récupérer les opérations d'un compte
    List<Operation> getOperationsByCompte(String numeroCompte);
}