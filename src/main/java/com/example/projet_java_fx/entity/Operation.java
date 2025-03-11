package com.example.projet_java_fx.entity;

import com.example.projet_java_fx.enums.TypeOperation;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class Operation {
    private int id;
    private Timestamp dateOp;
    private double amount;
    private TypeOperation type;
}