package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;

public interface IClient {
    public int Add(Client client);
    public int Update( Client client);
    public int Delete(String id);
    public Client get(String Id);


}
