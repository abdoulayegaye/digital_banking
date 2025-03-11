package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import java.util.List;

public interface IOperation {
    void ajouterOperation(Operation operation);
    List<Operation> getOperationsByCompte(Compte compte);
}
