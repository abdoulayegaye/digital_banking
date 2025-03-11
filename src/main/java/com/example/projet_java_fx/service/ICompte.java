package com.example.projet_java_fx.service;
import com.example.projet_java_fx.entity.Comptes;
import java.util.List;

public interface ICompte {
    public int create(Comptes Compte);
    public double getSolde(int Id);

    public int closeAccount(int Id);

    // Génération de relevés bancaires en PDF
    public byte[] generateBankStatement(int Id);

    public int consultercompte(int Id);

    // Obtenir tous les Comptess d'un client
    public List<Comptes> getAllComptess();

    List<Comptes> getAllComptes();

    int update(Comptes selectedComptes);
}