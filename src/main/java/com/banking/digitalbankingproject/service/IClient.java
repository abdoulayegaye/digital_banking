package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;

import java.util.List;

public interface IClient {
    int addClient(Client client);
    int updateClient(Client client);
    int deleteClient(Client client);
    List<Client> getAllClients();
    Client getClientById(int id);
    List<Client> searchClientsByName(String name);
    List<Client> searchClientsByEmail(String email);
    int countClients();

}
