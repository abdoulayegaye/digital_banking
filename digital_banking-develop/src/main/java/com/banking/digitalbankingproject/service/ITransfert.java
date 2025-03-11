package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Transfert;

import java.time.Instant;

public interface ITransfert {
    public Transfert getId(String id);

    public int senderMaj(Transfert transfert);

    public int recieverMaj(Transfert transfert);

    public int add(Transfert transfert);
    public Instant date();
}
