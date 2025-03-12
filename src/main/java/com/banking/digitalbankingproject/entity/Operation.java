package com.banking.digitalbankingproject.entity;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Operation {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final ObjectProperty<Timestamp> date_op = new SimpleObjectProperty<>();
    private final IntegerProperty compte_id = new SimpleIntegerProperty();

    public Operation(int id, String type, double amount, Timestamp date_op, int compte_id) {
        this.id.set(id);
        this.type.set(type);
        this.amount.set(amount);
        this.date_op.set(date_op);
        this.compte_id.set(compte_id);
    }

    // Getters, setters, and property methods
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }
    public double getMontant() { return amount.get(); }
    public void setMontant(double montant) { this.amount.set(montant); }
    public Timestamp getDateOperation() { return date_op.get(); }
    public void setDateOperation(Timestamp dateOperation) { this.date_op.set(dateOperation); }
    public int getCompteId() { return compte_id.get(); }
    public void setCompteId(int compteId) { this.compte_id.set(compteId); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty typeProperty() { return type; }
    public DoubleProperty montantProperty() { return amount; }
    public ObjectProperty<Timestamp> dateOperationProperty() { return date_op; }
    public IntegerProperty compteIdProperty() { return compte_id; }
}