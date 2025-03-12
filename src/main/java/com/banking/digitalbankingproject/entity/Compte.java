package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.enums.EtatCompte;
import com.banking.digitalbankingproject.enums.TypeCompte;
import lombok.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

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
    private Date createdAt;
    private Client client;
    private TypeCompte type;
    private EtatCompte etat;

    public enum TypeCompte { COURANT, EPARGNE }
    public enum EtatCompte { ACTIF, INACTIF }

    public String generateNumeroCompte() {
        return "CPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    public String getCreatedAtString() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .format(LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault()));
    }

}
