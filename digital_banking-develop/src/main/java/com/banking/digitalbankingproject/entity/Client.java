package com.banking.digitalbankingproject.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class Client {
    private int id;
    private String Nom;
    private String Prenom;
    private String Email;



}
