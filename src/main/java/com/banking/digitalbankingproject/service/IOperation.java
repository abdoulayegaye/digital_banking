package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import java.util.List;

public interface IOperation {

    int depot(Compte compte, double montant) throws Exception;

    int retrait(Compte compte, double montant) throws Exception;

    int virement(Compte source, Compte destination, double montant) throws Exception;

    List<Operation> getAll() throws Exception;

    List<Operation> getOperationsCompte(Compte compte) throws Exception;
}
