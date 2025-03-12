package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface IOperation {
    List<Operation> getAllOperations();

    boolean CreateOperation(Operation operation);
    List<Operation> getOperationByCompteId(int compte);
    boolean retraitOperation(Operation operation);
    boolean historiqueOperation(Operation operation);
    boolean virementOperation(Operation operation);
}
