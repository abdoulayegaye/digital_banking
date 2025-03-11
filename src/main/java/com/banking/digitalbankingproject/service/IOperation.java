package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;
import java.util.List;

public interface IOperation {
    boolean effectuerDepot(int compteId, double montant);
    boolean effectuerRetrait(int compteId, double montant);
    boolean effectuerVirement(int compteSourceId, int compteDestinationId, double montant);
    List<Operation> getHistoriqueOperations(int compteId);
    List<Operation> getAllOperations();
    Operation getOperationById(int id);
}
