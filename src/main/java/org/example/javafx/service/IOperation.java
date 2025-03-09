package org.example.javafx.service;

import org.example.javafx.entities.Operation;

import java.util.List;

public interface IOperation {
    int effectuerDepot(String numeroCompte, double montant);
    int effectuerRetrait(String numeroCompte, double montant);
    int effectuerVirement(String numeroCompteSource,String numeroCompteDestination, double montant);
    List<Operation> consulterHistorique(String numeroCompte);
}
