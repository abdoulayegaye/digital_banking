package com.example.projet_java_fx.service;

import com.example.projet_java_fx.entity.Clients;
import com.example.projet_java_fx.entity.Comptes;

import java.util.List;

public interface IClient {
    public int createClient(Clients client);
    public int updateClient(Clients client);
    public int deleteClient(int client);
    public Clients getClient(int id);
    public List<Clients> getAllClients();
    List<Comptes> getbyClient(int id);

}
