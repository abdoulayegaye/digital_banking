package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface ICompte {
    boolean createCompte(Compte compte);
    boolean updateCompte(Compte compte);
    boolean deleteCompte(int id);
    List<Compte> getAllComptes();
    Compte getCompteById(int id);
    boolean fermerCompte(int id);
    boolean ouvrirCompte(int id);
    boolean deposer(int compteId, double montant);
    int countComptes();

    boolean updateBalance(int compteId, double nouveauSolde);
}