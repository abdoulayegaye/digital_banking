package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface ICompte {
    int addCompte(Compte compte);
    double consulterSolde(int compteId);
    int associerCompteClient(String numeroCompte, int clientId);
    int fermerCompte(int compteId);
    List<Compte> getAllComptes();
    Compte getCompteById(int id);
    Compte getCompteByNumero(String numeroCompte);
    int countComptesActif();
    int countComptesFerme();
    int countClientsComptes();
    List<Client> loadClients();
    List<Compte> searchCompteByNumero(String numeroCompte);
}
