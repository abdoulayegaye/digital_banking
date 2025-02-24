package com.banking.digitalbankingproject.entity;

import lombok.*;

@Data
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
}
