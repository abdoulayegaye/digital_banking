package com.banking.digitalbankingproject.entity;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor // Ce constructeur sans argument sera généré par Lombok
@Getter
@Setter
@ToString
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private int clientId;
    private Instant createdAt;  // ✅ Changer Timestamp → Instant
    private Client client;

    // Si Lombok ne génère pas le constructeur sans argument, vous pouvez le faire manuellement
    public Compte() {
        // Le constructeur sans argument n'a rien à initialiser, donc on peut laisser vide ou initialiser les variables si nécessaire
    }

    public Compte(int i, String numero, double balance, int i1) {

    }
}
