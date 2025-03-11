package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface ICompte {
    int creerCompte(Compte compte);
    int fermerCompte(String numeroCompte);
    Compte consulterCompte(String numeroCompte);
    List<Compte> obtenirTousLesComptes();
}
