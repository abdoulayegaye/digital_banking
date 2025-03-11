package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;

import java.util.List;

public class OperationImpl implements IOperation {
    @Override
    public Operation effectuerDepot(int compteId, double montant) {
        return null;
    }

    @Override
    public Operation effectuerRetrait(int compteId, double montant) {
        return null;
    }

    @Override
    public List<Operation> obtenirHistoriqueOperations(int compteId) {
        return List.of();
    }
}
