package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;

import java.util.List;

public interface ICompte {
    boolean createCompte(Compte compte);
    List<Compte> getAllComptes();
    Compte getCompteById(int id);
    void supprimerCompte(Compte compteSelectionne);
    void modifierCompte(Compte compte);
    boolean updateCompte(Compte compte);
    void retrait(Compte compteSelectionne, double montant);
    void depot(Compte compteSelectionne, double montant);
    void virement(Compte compteSource, Compte compteDestinataire, double montant);
    public boolean accountNumberExists(String numero);
    public List<Client> getAllClients();

    List<Operation> getOperations(Compte compteSelectionne);
}
