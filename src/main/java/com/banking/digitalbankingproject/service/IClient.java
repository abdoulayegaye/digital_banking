package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;

import java.util.List;

public interface IClient {

    Client ajouterClient(Client client);

    Client modifierClient(Client client);


    void supprimerClient(String email);

    Client consulterClient(String email);


    List<Client> listerClients();

    Client consulterClientById(int clientId);
}