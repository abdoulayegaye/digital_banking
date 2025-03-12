package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ClientImpl implements IClient {

    private static final Logger logger = Logger.getLogger(ClientImpl.class.getName());

    // Simuler une base de données avec une Map pour améliorer les performances de recherche
    private Map<String, Client> clientsMap = new HashMap<>();

    @Override
    public Client ajouterClient(Client client) {
        if (client == null) {
            logger.severe("Le client ne peut pas être null.");
            throw new IllegalArgumentException("Le client ne peut pas être null.");
        }
        if (clientsMap.containsKey(client.getEmail())) {
            logger.warning("Un client avec cet email existe déjà !");
            return null;
        }
        clientsMap.put(client.getEmail(), client);
        logger.info("Client ajouté avec succès : " + client.getEmail());
        return client;
    }

    @Override
    public Client modifierClient(Client client) {
        if (client == null) {
            logger.severe("Le client ne peut pas être null.");
            throw new IllegalArgumentException("Le client ne peut pas être null.");
        }
        if (clientsMap.containsKey(client.getEmail())) {
            Client existingClient = clientsMap.get(client.getEmail());
            existingClient.setNom(client.getNom());
            existingClient.setPrenom(client.getPrenom());
            logger.info("Client modifié avec succès : " + client.getEmail());
            return existingClient;
        }
        logger.warning("Client non trouvé pour modification : " + client.getEmail());
        return null;
    }

    @Override
    public void supprimerClient(String email) {
        if (email == null || email.isEmpty()) {
            logger.severe("L'email ne peut pas être null ou vide.");
            throw new IllegalArgumentException("L'email ne peut pas être null ou vide.");
        }
        if (clientsMap.remove(email) != null) {
            logger.info("Client supprimé avec succès : " + email);
        } else {
            logger.warning("Client non trouvé pour suppression : " + email);
        }
    }

    @Override
    public Client consulterClient(String email) {
        if (email == null || email.isEmpty()) {
            logger.severe("L'email ne peut pas être null ou vide.");
            throw new IllegalArgumentException("L'email ne peut pas être null ou vide.");
        }
        return clientsMap.get(email);
    }

    @Override
    public List<Client> listerClients() {
        return new ArrayList<>(clientsMap.values());
    }

    @Override
    public Client consulterClientById(int clientId) {
        return null;
    }
}