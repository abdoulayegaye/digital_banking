package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import java.util.List;
import java.util.ArrayList;

public class CompteImpl implements ICompte {
    // Méthodes existantes...
    // Pas besoin d'implémenter deleteCompte(int)

    @Override
    public void deleteCompte(int id) {
        // Implémentation provisoire
        System.out.println("Compte " + id + " supprimé");
    }

    @Override
    public List<Compte> getAllComptes() {
        // Implémentation provisoire
        return new ArrayList<>();
    }

    @Override
    public Compte getCompteById(int id) {
        // Implémentation temporaire
        return new Compte();
    }

    @Override
    public Compte saveCompte(Compte compte) {
        // Implémentation temporaire
        return compte;
    }

    @Override
    public Compte updateCompte(Compte compte) {
        // Implémentation temporaire
        return compte;
    }
} 