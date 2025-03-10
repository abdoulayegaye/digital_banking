package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import java.util.List;
public interface ICompte {

    int save(Compte compte) throws Exception;

    int delete(String numero) throws Exception;

    List<Compte> getAll() throws Exception;

    Compte getByNumero(String numero) throws Exception;

    int updateSolde(String numero, double montant) throws Exception;
}
