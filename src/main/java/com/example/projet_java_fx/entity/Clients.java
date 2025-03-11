package com.example.projet_java_fx.entity;
import lombok.Data;

import java.util.List;

@Data
public class Clients {
    private int id;
    private String nom;
    private String prenom;
    private String email;
   



    public String toString(){
        return nom + " " + prenom;
    }
}
