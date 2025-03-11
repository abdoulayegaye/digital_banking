package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;

public interface ICompte {
    Compte createCompte(Compte compte, int clientId);
    Compte getCompte(String numeroCompte);
    void closeCompte(String numeroCompte);
    List<Compte> getComptesClient(int clientId);
    double getBalance(String numeroCompte);
    List<Compte> getAllComptes();
}
