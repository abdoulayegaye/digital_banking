package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Compte;

public interface ITransferService {
    boolean transferer(Compte source, Compte destination, double montant);
}
