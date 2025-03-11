 package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

 public class AjoutController {

        @FXML
        private Button btnAjout;

        @FXML
        private Button btnRetour;


        @FXML
        private TextField name;

        @FXML
        private TextField username;

       @FXML
       private TextField email;

        @FXML
        void ajouter(ActionEvent event) {

            String name = this.name.getText();
            String username = this.username.getText();
            String email = this.email.getText();

            if (name.isEmpty() || username.isEmpty() || email.isEmpty()) {
                Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
                return;
            }

            String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";

            Db db = new Db(); // Initialisation de la connexion à la BD

            try {
                db.initPrepar(sql);

                PreparedStatement stm = db.getPstm();
                stm.setString(1, name);  // name devient nom
                stm.setString(2, username);  // username devient prenom
                stm.setString(3, email);


                int rowsInserted = db.executeMaj();
                if (rowsInserted > 0) {
                    Notification.NotifSuccess("Succès", "Client ajouté avec succès");

                    Outils.load(event, "Bienvenue à la Gestin des clients", "/fxml/clients.fxml");
                } else {
                    Notification.NotifError("Erreur", "Aucun client ajouté");

                }
            } catch (Exception e) {
                e.printStackTrace();
                Notification.NotifError("Erreur", "Une erreur est survenue lors de l'ajout du client");
            } finally {
                db.closeStatement();
                db.closeConnection();
            }
        }


     @FXML
        void retour(ActionEvent event) throws IOException {

            Outils.load(event, "Ajoutee un client", "/fxml/clients.fxml");

        }

    }



