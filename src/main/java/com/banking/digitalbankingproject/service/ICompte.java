package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface ICompte {
    public boolean CreateCompte(Compte compte);
    public List<Compte>  GetAllCompte();
    public Compte GetCompteByNumCompte(String numCompte);
    public boolean UpdateCompte(Compte compte);
    public boolean DeleteCompte(Compte compte);
    public List<Compte> GetAllCompteByIdClient(int id_client);
    public Compte GetCompeById(int id);
}
