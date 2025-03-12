package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface ICompte {
    public List<Compte> listCompte(String client);
    public int createCompte(Compte compte);
    public void associateCompte(Compte compte, Client client);
    public double consultSolde(String numero);
    public int closeCompte(String numero);
    Compte getCompteByNumero(String numero);
    Compte getCompteById(int id);
    public int updateCompte(Compte compte);
    int updateComptes(Compte compteExiste);
    public int deleteCompte(String numero);

    void generateRelevePDF(Compte compte);
}
