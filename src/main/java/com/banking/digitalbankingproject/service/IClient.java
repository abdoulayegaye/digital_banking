package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import java.util.List;

public interface IClient {
    Client createClient(Client client);
    Client updateClient(Client client);
    void deleteClient(int clientId);
    Client getClient(int clientId);
    List<Client> getAllClients();
}
