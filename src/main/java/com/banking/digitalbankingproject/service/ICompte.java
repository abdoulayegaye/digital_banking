package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface ICompte {
    List<Compte> getAllComptes();

    boolean ajouterCompte(Compte compte);
    boolean supprimerCompte(Compte compte);

    Compte getCompte(String numero);
    Compte getCompteById(int compteId);
}
