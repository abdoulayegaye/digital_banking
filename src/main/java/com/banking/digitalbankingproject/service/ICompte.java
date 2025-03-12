package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;

public interface ICompte {

    void creerCompte(Compte compte);

    List<Compte> listerComptes();

    Compte consulterCompte(int id);

    void fermerCompte(int id);

    void genererPdf(int id);

    void depot(int compteId, double montant);
    void retrait(int compteId, double montant);
    void virement(int compteSourceId, int compteDestId, double montant);
}