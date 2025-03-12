package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;

import java.util.List;



public interface IClient {
    boolean createClient(Client client);
    List<Client> getAllClients();
    boolean modifierClient(Client client);
    boolean supprimerClient(Client client);
    Client getClientById(int id);


}
