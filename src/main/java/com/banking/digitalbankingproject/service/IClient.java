package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Client;
import java.util.List;


public interface IClient {

    int save(Client client) throws Exception;
    

    int update(Client client) throws Exception;
    

    int delete(int id) throws Exception;
    

    List<Client> getAll() throws Exception;
    

    Client getById(int id) throws Exception;
}
