package com.banking.digitalbankingproject.service;

import com.banking.digitalbankingproject.entity.Depot;

import java.time.Instant;

public interface IDepot {
    public Depot getId(String id);
    public Depot get(String id);
    public int add(Depot depot);

    public Instant date();
}
