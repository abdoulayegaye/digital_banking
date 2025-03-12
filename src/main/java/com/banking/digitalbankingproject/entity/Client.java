package com.banking.digitalbankingproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;
}
