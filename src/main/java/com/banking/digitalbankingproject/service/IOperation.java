package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.time.Instant;
import java.util.List;

public interface IOperation {
    boolean createOperation(Operation operation);
    List<Operation> getAllOperations();
    List<Operation> getOperationsByCompteId(int compteId);
    List<Operation> getOperationsByDate(Instant startDate, Instant endDate);
    int countOperations();
}