package com.example.projet_java_fx.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Comptes {
    private int id;
    private String numero;
    private double solde; // Change to double
    private Timestamp date;
    private Clients idclient;

    @Override
    public String toString() {
        return "Comptes{" +
                "numero='" + numero + '\'' +
                ", solde=" + solde +
                ", date=" + date +
                ", idclient=" + idclient +
                '}';
    }
}