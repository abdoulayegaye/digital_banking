package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import javafx.collections.ObservableList;

import java.util.List;

public interface ICompte {
    ObservableList<Compte> getAllComptes();
    void ajouterCompte(Compte compte);
    void supprimerCompte(Compte compte);
    void modifierCompte(Compte compte);
    void virement(Compte compteSource, Compte compteDestinataire, double montant);
    void updateCompte(Compte compte);
    void createCompte(Compte compte);
    List<Operation> getOperations(Compte compte);
}