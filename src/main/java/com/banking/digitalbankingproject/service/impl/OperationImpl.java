package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private List<Operation> operations = new ArrayList<>();

    @Override
    public boolean deposer(String numeroCompte, double montant) {
        if (montant <= 0) {
            return false; // Le montant doit être positif
        }
        Operation operation = new Operation("Dépôt", montant, numeroCompte);
        operations.add(operation);
        return true;
    }

    @Override
    public boolean retirer(String numeroCompte, double montant) {
        if (montant <= 0) {
            return false; // Le montant doit être positif
        }
        Operation operation = new Operation("Retrait", montant, numeroCompte);
        operations.add(operation);
        return true;
    }

    @Override
    public boolean virement(String compteSource, String compteDestination, double montant) {
        if (montant <= 0) {
            return false; // Le montant doit être positif
        }
        // Simuler un retrait du compte source
        Operation retrait = new Operation("Virement (Retrait)", montant, compteSource);
        operations.add(retrait);

        // Simuler un dépôt sur le compte destination
        Operation depot = new Operation("Virement (Dépôt)", montant, compteDestination);
        operations.add(depot);

        return true;
    }

    @Override
    public List<Operation> getAllOperations() {
        return new ArrayList<>(operations);
    }

    @Override
    public List<Operation> getOperationsByCompte(String numeroCompte) {
        List<Operation> operationsByCompte = new ArrayList<>();
        for (Operation operation : operations) {
            if (operation.getNumeroCompte().equals(numeroCompte)) {
                operationsByCompte.add(operation);
            }
        }
        return operationsByCompte;
    }
}