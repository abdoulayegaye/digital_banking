package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;

import java.time.Instant;

public interface ICompte {
    public int Add(Compte compte,String id);
    public int get(String Id);
    public Compte getidrev(String Id);
    public Compte getidsolde(String Id);
    public  int supp(String Id);
    public Instant date();
}
