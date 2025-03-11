package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;

import java.util.List;

public interface IClient {
    int creerClient(Client client);
    int modifierClient(Client client);
    void supprimerClient(int id);
    Client obtenirClient(int client);
    List<Client> listClient();

}
