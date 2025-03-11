package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;

import java.util.List;

public interface IClient {
    public boolean createClient(Client client);
    public List<Client> getAllClients();
    public Client getClientbyId(int id);
    public boolean deleteClient(int id);
    public boolean updateClient(Client client);
}
