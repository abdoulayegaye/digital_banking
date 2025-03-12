package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private List<Compte> comptes = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean createCompte(Compte compte) {
        compte.setId(nextId++);
        comptes.add(compte);
        return true;
    }

    @Override
    public boolean updateCompte(int id, String numero, double balance, String createdAt, int clientId) {
        for (Compte compte : comptes) {
            if (compte.getId() == id) {
                compte.setNumero(numero);
                compte.setBalance(balance);
                compte.setCreatedAt(createdAt);
                compte.setClientId(clientId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteCompte(int id) {
        return comptes.removeIf(compte -> compte.getId() == id);
    }

    @Override
    public List<Compte> getAllComptes() {
        return new ArrayList<>(comptes);
    }

    @Override
    public Compte getCompteById(int id) {
        for (Compte compte : comptes) {
            if (compte.getId() == id) {
                return compte;
            }
        }
        return null;
    }
}