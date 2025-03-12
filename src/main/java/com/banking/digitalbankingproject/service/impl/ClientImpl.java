package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.dao.ClientDAO;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClientImpl implements IClient {

    private ClientDAO clientDAO;

    public ClientImpl(Connection connection) {
        this.clientDAO = new ClientDAO(connection);
    }

    @Override
    public void ajouterClient(Client client) {
        try {
            clientDAO.ajouterClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierClient(Client client) {
        try {
            clientDAO.modifierClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerClient(int id) {
        try {
            clientDAO.supprimerClient(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client getClient(int id) {
        try {
            return clientDAO.getClient(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Client> getTousLesClients() {
        try {
            return clientDAO.getTousLesClients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
