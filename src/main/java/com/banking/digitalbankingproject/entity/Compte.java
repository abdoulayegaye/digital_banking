package com.banking.digitalbankingproject.entity;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Compte {
    private int id;
    private String numero;
    private double balance;
    private Instant createdAt;
    private Client client;
}
