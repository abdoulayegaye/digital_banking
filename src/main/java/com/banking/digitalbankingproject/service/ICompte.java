package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import java.util.List;

public interface ICompte {
    void createCompte(Compte compte);
    void updateCompte(Compte compte);
    void supprimerCompte(Compte compte);
    List<Compte> getAllComptes();
    List<Operation> getOperations(Compte compte);
    void virement(Compte source, Compte destinataire, double montant);
}
