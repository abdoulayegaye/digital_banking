package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {

    private ObservableList<Compte> comptes = FXCollections.observableArrayList();

    @Override
    public ObservableList<Compte> getAllComptes() {
        return comptes;
    }

    @Override
    public void ajouterCompte(Compte compte) {
        comptes.add(compte);
    }

    @Override
    public void supprimerCompte(Compte compte) {
        comptes.remove(compte);
    }

    @Override
    public void modifierCompte(Compte compte) {
        int index = comptes.indexOf(compte);
        if (index >= 0) {
            comptes.set(index, compte);
        }
    }

    @Override
    public void createCompte(Compte compte) {
        ajouterCompte(compte);
    }

    @Override
    public void updateCompte(Compte compte) {
        modifierCompte(compte);
    }

    @Override
    public List<Operation> getOperations(Compte compte) {
        // Retourner la liste des opérations pour un compte donné (données fictives pour l'exemple)
        return new ArrayList<>();
    }

    @Override
    public void virement(Compte compteSource, Compte compteDestinataire, double montant) {
        if (compteSource.getSolde() < montant) {
            throw new IllegalArgumentException("Solde insuffisant sur le compte source.");
        }
        compteSource.setSolde(compteSource.getSolde() - montant);
        compteDestinataire.setSolde(compteDestinataire.getSolde() + montant);
    }
}