package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import java.util.List;

public interface IOperation {
    boolean effectuerOperation(String numeroCompte, double montant, TypeOperation type);
    List<Operation> getAllOperations();
    List<Operation> getOperationsByCompte(String numeroCompte);
}
