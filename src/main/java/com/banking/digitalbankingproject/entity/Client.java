package com.banking.digitalbankingproject.entity;

import javafx.beans.property.SimpleStringProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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


    public String getFullName() {
        return nom + " " + prenom;
    }

    private List<Compte> comptes = new ArrayList<>();

    public List<Compte> getComptes() {
        return comptes;
    }
}
