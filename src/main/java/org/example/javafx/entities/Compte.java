package org.example.javafx.entities;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Compte {
    private int id;
    private String numero;
    private double solde;
    private Timestamp date_ouverture;
    private Client client;
}
