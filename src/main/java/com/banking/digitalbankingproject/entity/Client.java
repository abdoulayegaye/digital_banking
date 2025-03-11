package com.banking.digitalbankingproject.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;

    // Constructeur avec paramètres clientId et nom
    public Client(int clientId, String nom) {
        this.id = clientId;
        this.nom = nom;
    }
}
