package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;
import java.util.ArrayList;

public class CompteImplConcret extends CompteImpl {
    
    @Override
    public List<Compte> getAllComptes() {
        // Implémentation concrète
        return new ArrayList<>();
    }
    
    @Override
    public void deleteCompte(int id) {
        // Implémentation concrète
        System.out.println("Compte supprimé: " + id);
    }
    
    // Implémentez ici toutes les autres méthodes abstraites
    // présentes dans CompteImpl ou dans l'interface ICompte
} 