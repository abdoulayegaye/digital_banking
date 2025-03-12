package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

import static com.banking.digitalbankingproject.tools.Notification.NotifError;
import static com.banking.digitalbankingproject.tools.Notification.NotifSuccess;

public class AjouterClientController {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnRetour;

    private IClient clientService = new ClientImpl();

    @FXML
    private void initialize() {
        btnAjouter.setOnAction(event -> {
            try {
                ajouterClient(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnRetour.setOnAction(event -> retourGestionClients(new ActionEvent()));
    }

    private void ajouterClient(ActionEvent event) throws IOException {
        if (!Objects.equals(txtNom.getText(), "") && !Objects.equals(txtPrenom.getText(), "") && !Objects.equals(txtEmail.getText(), "")){
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();

            Client client = new Client(nom, prenom, email);
            boolean ok = clientService.createClient(client);
            if (ok){
                NotifSuccess("Succès", "Client ajouté avec succès");
                //retourGestionClients(new ActionEvent());
                Outils.load(event, "Liste Client", "/fxml/clients.fxml");
                initialize();
            }else {
                NotifError("Erreur", "Erreur d'ajout du client");
            }

        }else {
            NotifError("Erreur", "Tous les champs sont obligatoires");
        }

    }

    @FXML
    private void retourGestionClients(ActionEvent event) {
        try {
            // Charger la vue de gestion des clients
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/gestionClients.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnRetour.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Gestion des Clients");
            stage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}