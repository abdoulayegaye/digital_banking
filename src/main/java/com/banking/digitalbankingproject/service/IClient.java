package com.banking.digitalbankingproject.service;
import com.banking.digitalbankingproject.entity.Client;
import java.util.List;


public interface IClient {
    boolean createClient(Client client);
    boolean updateClient(Client client);
    boolean deleteClient(int id);
    List<Client> getAllClients();
    Client getClientById(int id);
    boolean clientExists(String nom, String prenom, String email);
    boolean hasLinkedAccounts(int clientId);
    int countClients();
}
