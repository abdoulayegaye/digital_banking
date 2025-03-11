package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import java.util.List;

public interface IClient {
    void ajouterClient(Client client);
    void modifierClient(Client client);
    void supprimerClient(Client client);
    List<Client> getAllClients();
}
