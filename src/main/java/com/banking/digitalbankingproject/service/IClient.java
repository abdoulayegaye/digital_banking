package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import java.util.List;

public interface IClient {
    boolean createClient(Client client);
    List<Client> getAllClients();
    Client getClientById(int id);
    boolean updateClient(Client client);
    boolean deleteClient(int id);

    List<Client> searchClients(String searchTerm);
}