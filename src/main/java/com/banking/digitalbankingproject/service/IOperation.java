package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;
import java.util.List;

public interface IOperation {

    void creerOperation(Operation operation);

    List<Operation> listerOperationsParCompte(int compteId);

    Operation consulterOperation(int id);
}