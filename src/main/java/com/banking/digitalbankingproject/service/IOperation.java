package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface IOperation {
    boolean createOperation(Operation operation);
    List<Operation> getOperationsByCompte(int compteId);
}
