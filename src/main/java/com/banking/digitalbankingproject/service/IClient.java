package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;

import java.util.List;

public interface IClient {
    public int create(Client client);
    public int update(Client client);
    public Client getClientByID(int id);
    public int delete(int id);
    Client get(int clientId);
    List<Client> list(String search);
}
