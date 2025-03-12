package com.banking.digitalbankingproject.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;

    public Client() {
    }

    public Client(int id, String email, String prenom, String nom) {
        this.id = id;
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
    }

    public Client(String nom) {
    }
    @Override
    public String toString() {
        return nom + " " + prenom;
    }

}