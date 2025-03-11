package com.banking.digitalbankingproject.service;


import com.banking.digitalbankingproject.entity.Retrait;

import java.time.Instant;

public interface IRetrait {
    public int add(Retrait retrait);
    public Retrait getId(String id);
    public Instant date();
}
