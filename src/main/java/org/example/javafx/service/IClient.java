package org.example.javafx.service;
import org.example.javafx.entities.Client;
import java.util.List;

public interface IClient {
    public int create(Client client);
    public List<Client> getAllClients();
    public int update(Client client);
    public int delete(int id);
}
