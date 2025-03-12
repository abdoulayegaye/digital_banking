package com.banking.digitalbankingproject.entity;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Compte {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty numero = new SimpleStringProperty();
    private final DoubleProperty solde = new SimpleDoubleProperty();
    private final ObjectProperty<Timestamp> dateOuverture = new SimpleObjectProperty<>();
    private final IntegerProperty clientId = new SimpleIntegerProperty();
    private final StringProperty clientNom = new SimpleStringProperty();

    public Compte(int id, String numero, double solde, Timestamp dateOuverture, int clientId) {
        this.id.set(id);
        this.numero.set(numero);
        this.solde.set(solde);
        this.dateOuverture.set(dateOuverture);
        this.clientId.set(clientId);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public String getNumero() { return numero.get(); }
    public void setNumero(String numero) { this.numero.set(numero); }
    public double getSolde() { return solde.get(); }
    public void setSolde(double solde) { this.solde.set(solde); }
    public Timestamp getDateOuverture() { return dateOuverture.get(); }
    public void setDateOuverture(Timestamp dateOuverture) { this.dateOuverture.set(dateOuverture); }
    public int getClientId() { return clientId.get(); }
    public void setClientId(int clientId) { this.clientId.set(clientId); }
    public String getClientNom() { return clientNom.get(); }
    public void setClientNom(String clientNom) { this.clientNom.set(clientNom); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty numeroProperty() { return numero; }
    public DoubleProperty soldeProperty() { return solde; }
    public ObjectProperty<Timestamp> dateOuvertureProperty() { return dateOuverture; }
    public IntegerProperty clientIdProperty() { return clientId; }
    public StringProperty clientNomProperty() { return clientNom; }
}