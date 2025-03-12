package com.banking.digitalbankingproject.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {
    private SimpleIntegerProperty id;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final StringProperty email;

    // Constructeur avec id
    public Client(int id, String nom, String prenom, String email) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.email = new SimpleStringProperty(email);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    // Properties pour la liaison dans le TableView
    public SimpleIntegerProperty idProperty() { return id; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty emailProperty() { return email; }
}