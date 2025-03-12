package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientController {

    @FXML
    private Button btnValider;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    private Db db = new Db(); // Instance de la classe Db pour gérer la base

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/FXML/interfaceClients.fxml");
    }

    @FXML
    public void enregistrerUtilisateur() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();

        // Vérifier que tous les champs sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            afficherMessage("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }

        // Requête SQL pour insérer un client
        String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";

        try {
            db.initPrepar(sql);
            PreparedStatement stmt = db.getPstm();
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);

            int result = db.executeMaj();
            db.closeConnection();

            if (result > 0) {
                afficherMessage("Succès", "Inscription réussie ! Veuillez clicker sur Retour", Alert.AlertType.INFORMATION);
                viderChamps();
            } else {
                afficherMessage("Erreur", "Échec de l'inscription", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            afficherMessage("Erreur", "Problème lors de l'inscription : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void afficherMessage(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.show();
    }

    private void viderChamps() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
    }
}
