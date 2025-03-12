package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;

public interface ICompte {
    boolean createCompte(Compte compte);
    boolean updateCompte(Compte compte);
    boolean deleteCompte(int id);
    Compte getCompteById(int id);
    Compte getCompteByNumero(String numero);
    List<Compte> getAllComptes();
    List<Compte> getComptesByClientId(int clientId);
}
