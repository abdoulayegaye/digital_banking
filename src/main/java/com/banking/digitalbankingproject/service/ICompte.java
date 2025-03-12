package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface ICompte {
    void ajouterCompte(Compte compte);
    void modifierCompte(Compte compte);
    void supprimerCompte(int id);
    Compte getCompteById(int id);
    List<Compte> getTousLesComptes();

    Compte getCompteByNumero(String numeroCompte);

    void updateCompte(Compte compte);
}