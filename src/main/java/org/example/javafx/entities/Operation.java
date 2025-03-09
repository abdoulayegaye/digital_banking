package org.example.javafx.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.javafx.enums.TypeOperation;
import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Operation {
    private int id;
    private Timestamp dateOp;
    private double amount;
    private TypeOperation type;
    private Compte compte;
}
