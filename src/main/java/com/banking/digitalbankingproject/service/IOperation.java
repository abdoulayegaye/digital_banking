package com.banking.digitalbankingproject.service;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import javafx.collections.ObservableList;
public interface IOperation {
    void effectuerVirement(int sourceId, int destId, double montant);
    boolean ajouterOperation(double amount, TypeOperation type, Compte compte);
    boolean effectuerVirement(double amount, Compte compteSource, Compte compteDest);
    ObservableList<Operation> getHistorique(int compteId);
    ObservableList<Operation> getToutesOperations();
}

