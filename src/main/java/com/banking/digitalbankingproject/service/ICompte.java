package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;

public interface ICompte {

        boolean createCompte(Compte compte);
        List<Compte> getAllComptes();
        Compte getCompteByNumero(String numero);
        Compte getCompteById(int id);
        boolean updateCompte(Compte compte);
        boolean deleteCompte(int id);
        List<Compte> getComptesByClientId(int clientId);
        boolean updateSolde(int compteId, double nouveauSolde);
}


