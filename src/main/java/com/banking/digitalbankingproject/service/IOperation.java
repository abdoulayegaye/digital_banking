package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

/**
 * Interface définissant les opérations bancaires possibles
 */
public interface IOperation {
    
    /**
     * Effectue une opération bancaire (dépôt ou retrait)
     * 
     * @param compteId Identifiant du compte
     * @param montant Montant de l'opération
     * @param type Type d'opération (DEPOT ou RETRAIT)
     * @return Opération créée
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    Operation effectuerOperation(int compteId, double montant, TypeOperation type) throws SQLException;
    
    /**
     * Effectue un virement entre deux comptes
     * 
     * @param compteSourceId Identifiant du compte source
     * @param compteDestinationId Identifiant du compte destination
     * @param montant Montant du virement
     * @return Opération de virement créée
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    Operation effectuerVirement(int compteSourceId, int compteDestinationId, double montant) throws SQLException;
    
    /**
     * Récupère toutes les opérations d'un compte
     * 
     * @param compteId Identifiant du compte
     * @return Liste des opérations du compte
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    List<Operation> getOperationsByCompteId(int compteId) throws SQLException;
    
    /**
     * Récupère les opérations d'un compte pour une période donnée
     * 
     * @param compteId Identifiant du compte
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return Liste des opérations pour la période
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    List<Operation> getOperationsByCompteAndPeriod(int compteId, Instant dateDebut, Instant dateFin) throws SQLException;
}
