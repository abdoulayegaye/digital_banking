package com.banking.digitalbankingproject.entity;

import lombok.Data;

@Data
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;
}
