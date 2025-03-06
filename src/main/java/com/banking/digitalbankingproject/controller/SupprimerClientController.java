package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Notification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.entity.Client;

public class SupprimerClientController {

    @FXML
    private Button btnOui;
    @FXML
    private Button btnNon;
    @FXML
    private Button btnRetour;

    private IClient clientService = new ClientImpl();
    private Client client;

    @FXML
    private void initialize() {
        btnOui.setOnAction(event -> supprimerClient());
        btnNon.setOnAction(event -> fermerFenetre());
        btnRetour.setOnAction(event -> retourGestionClients(new ActionEvent()));
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private void supprimerClient() {
        clientService.supprimerClient(client);
        Notification.NotifSuccess("Succès", "Client supprimé avec succès");
        fermerFenetre();
    }

    private void fermerFenetre() {
        btnNon.getScene().getWindow().hide();
    }

    @FXML
    private void retourGestionClients(ActionEvent event) {
        try {
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
