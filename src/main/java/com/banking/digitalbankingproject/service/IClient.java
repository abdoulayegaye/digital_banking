package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface IClient {
    boolean createClient(Client client);
    List<Client> getAllClients();
    Client getClientById(int id);
    List<Compte> getAllComptes();
    void modifierClient(Client client);
    void supprimerClient(Client client);
    void ajouterCompte(Compte compte);
    void modifierCompte(Compte compte);
    void supprimerCompte(Compte compte);
}