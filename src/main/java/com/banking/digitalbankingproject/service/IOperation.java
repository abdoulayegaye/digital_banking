package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface IOperation {
    Operation effectuerDepot(int compteId, double montant);
    Operation effectuerRetrait(int compteId, double montant);
    List<Operation> obtenirHistoriqueOperations(int compteId);
}
