package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import java.util.List;

public interface IClient {
    // Créer un client
    boolean createClient(Client client);

    // Modifier un client
    boolean updateClient(int id, String nom, String prenom, String email);

    // Supprimer un client
    boolean deleteClient(int id);

    // Récupérer tous les clients
    List<Client> getAllClients();

    // Récupérer un client par ID
    Client getClientById(int id);
}