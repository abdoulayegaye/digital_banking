package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;
import java.util.List;

public interface IOperation {
    boolean createOperation(Operation operation);
    List<Operation> getAllOperations();
    Operation getOperationById(int id);
    boolean updateOperation(Operation operation);
    boolean deleteOperation(int id);
    
    // Méthode pour récupérer les opérations d'un compte spécifique
    List<Operation> getOperationsByCompteId(int compteId);
    
    // Méthodes pour les opérations bancaires
    boolean effectuerDepot(int compteId, double montant);
    boolean effectuerRetrait(int compteId, double montant);
    boolean effectuerVirement(int compteSourceId, int compteDestinationId, double montant);
    boolean genererReleve(int compteId);
}