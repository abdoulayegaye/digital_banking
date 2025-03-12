package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations possibles sur les clients
 */
public interface IClient {
    
    /**
     * Récupère tous les clients
     * 
     * @return Liste de tous les clients
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    List<Client> getAllClients() throws SQLException;
    
    /**
     * Récupère un client par son identifiant
     * 
     * @param id Identifiant du client
     * @return Client trouvé ou null
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    Client getClientById(int id) throws SQLException;
    
    /**
     * Enregistre un nouveau client
     * 
     * @param client Client à enregistrer
     * @return Client enregistré avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    Client saveClient(Client client) throws SQLException;
    
    /**
     * Met à jour les informations d'un client existant
     * 
     * @param client Client à mettre à jour
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    boolean updateClient(Client client) throws SQLException;
    
    /**
     * Supprime un client par son identifiant
     * 
     * @param id Identifiant du client à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    boolean deleteClient(int id) throws SQLException;
    
    /**
     * Recherche des clients selon un critère
     * 
     * @param searchTerm Terme de recherche (nom, prénom ou email)
     * @return Liste des clients correspondant au critère
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    List<Client> searchClients(String searchTerm) throws SQLException;

    boolean add(Client client);
    boolean update(Client client);
    boolean delete(int id);
    Client getById(int id);
    List<Client> getAll();

    Client findByEmail(String email);
}
