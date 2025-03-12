package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.enums.TypeCompte;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations possibles sur les comptes bancaires
 */
public interface ICompte {
    
    /**
     * Récupère tous les comptes
     * 
     * @return Liste de tous les comptes
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    List<Compte> getAllComptes() throws SQLException;
    
    /**
     * Récupère un compte par son identifiant
     * 
     * @param id Identifiant du compte
     * @return Compte trouvé ou null
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    Compte getCompteById(int id) throws SQLException;
    
    /**
     * Récupère un compte par son numéro
     * 
     * @param numero Numéro du compte
     * @return Compte trouvé ou null
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    Compte getCompteByNumero(String numero) throws SQLException;
    
    /**
     * Récupère tous les comptes d'un client
     * 
     * @param clientId Identifiant du client
     * @return Liste des comptes du client
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    List<Compte> getComptesByClientId(int clientId) throws SQLException;
    
    /**
     * Crée un nouveau compte
     * 
     * @param compte Compte à créer
     * @param typeCompte Type du compte (COURANT ou EPARGNE)
     * @return Compte créé avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    Compte saveCompte(Compte compte, TypeCompte typeCompte) throws SQLException;
    
    /**
     * Met à jour le solde d'un compte
     * 
     * @param compteId Identifiant du compte
     * @param newBalance Nouveau solde
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    boolean updateBalance(int compteId, double newBalance) throws SQLException;
    
    /**
     * Supprime un compte
     * 
     * @param id Identifiant du compte à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    boolean deleteCompte(int id) throws SQLException;
    
    /**
     * Génère un numéro de compte unique
     *
     * @return Numéro de compte généré
     */
    String generateAccountNumber();

    boolean add(Compte compte);

    Compte findByNumero(String numero);

    boolean update(Compte compte);
    boolean delete(int id);
    Compte getById(int id);
    List<Compte> getAll();
    List<Compte> getByClientId(int clientId);
    boolean updateSolde(int compteId, double nouveauSolde);
}
