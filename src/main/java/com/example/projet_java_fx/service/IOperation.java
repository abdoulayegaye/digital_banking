package com.example.projet_java_fx.service;

import com.example.projet_java_fx.entity.Operation;

import java.util.List;

public interface IOperation {
    int effectuerDepot(String numeroCompte, double montant);
    int effectuerRetrait(String numeroCompte, double montant);
    int effectuerVirement(String numeroCompteSource,String numeroCompteDestination, double montant);
    List<Operation> consulterHistorique(String numeroCompte);
}