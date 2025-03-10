package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface IOperation {
    boolean depot(int compteId, double montant);
    boolean retrait(int compteId, double montant);
    List<Operation> consulterHistorique();

    List<Operation> consulterHistorique(int compteId);

    List<Operation> consulterHistorique(int compteId, String dateDebut, String dateFin);

    int countTransactionsRecent();
}
