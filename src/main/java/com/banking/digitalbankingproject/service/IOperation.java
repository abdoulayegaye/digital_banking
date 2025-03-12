package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface IOperation {
    int deposit(String accountNumber, double amount);

    int withdraw(String accountNumber, double amount);

    int transfer(String sourceAccount, String destAccount, double amount);

    List<Operation> getOperationsByAccount(String accountNumber);

    List<Operation> getAllOperations();
}
