package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface IOperation {
    public boolean createOperation(Operation operation);
    public List<Operation> getAllOperations();
    public List<Operation> getOperationsByIdCompte(int id_compte);

}
