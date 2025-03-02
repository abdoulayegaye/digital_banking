package com.banking.digitalbankingproject.entity;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;

    public Client(int id, String nom, String prenom, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public Client(int clientId) {
        this.id = clientId;
    }
}

