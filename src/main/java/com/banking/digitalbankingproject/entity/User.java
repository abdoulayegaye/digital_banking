package com.banking.digitalbankingproject.entity;

public class User {
    private int id;
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String role;

    // Constructeur par défaut
    public User() {
        // Initialisation par défaut (optionnel)
    }

    // Constructeur personnalisé
    public User(int id, String username, String password, String nom, String prenom, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User ID: " + id + " - " + prenom + " " + nom + " (" + username + ")";
    }
}