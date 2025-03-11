package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;

import java.util.List;

public interface IClient {

        boolean modifierClient(Client client);
        boolean supprimerClient(int id);
        Client getClient(int id);
        List<Client> getAllClients();

}
