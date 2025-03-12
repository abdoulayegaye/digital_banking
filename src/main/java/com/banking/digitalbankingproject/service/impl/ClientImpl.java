package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements IClient {
    private List<Client> clients = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean createClient(Client client) {
        client.setId(nextId++);
        clients.add(client);
        return true;
    }

    @Override
    public boolean updateClient(int id, String nom, String prenom, String email) {
        for (Client client : clients) {
            if (client.getId() == id) {
                client.setNom(nom);
                client.setPrenom(prenom);
                client.setEmail(email);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteClient(int id) {
        return clients.removeIf(client -> client.getId() == id);
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    @Override
    public Client getClientById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }
}