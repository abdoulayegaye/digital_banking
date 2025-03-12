package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;

public interface ICompte {
    boolean createCompte(Compte compte);
    List<Compte> getAllComptes();
    Compte getCompteById(int id);
    boolean updateCompte(Compte compte);
    boolean deleteCompte(int id);
    
    // Méthode pour récupérer les comptes d'un client spécifique
    List<Compte> getComptesByClientId(int clientId);
}