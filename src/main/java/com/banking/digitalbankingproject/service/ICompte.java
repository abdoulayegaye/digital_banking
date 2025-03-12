package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;

public interface ICompte {
    // Créer un compte
    boolean createCompte(Compte compte);

    // Modifier un compte
    boolean updateCompte(int id, String numero, double balance, String createdAt, int clientId);

    // Supprimer un compte
    boolean deleteCompte(int id);

    // Récupérer tous les comptes
    List<Compte> getAllComptes();

    // Récupérer un compte par ID
    Compte getCompteById(int id);
}