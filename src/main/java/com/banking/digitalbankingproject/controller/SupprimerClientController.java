package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils; // ✅ Ajout de l'import
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

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
        if (client != null) {
            clientService.supprimerClient(client);
            Outils.showSuccess("Succès", "Client supprimé avec succès");
            fermerFenetre();
        } else {
            Outils.showError("Erreur", "Aucun client sélectionné !");
        }
    }

    private void fermerFenetre() {
        btnNon.getScene().getWindow().hide();
    }

    @FXML
    private void retourGestionClients(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionClients.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Clients");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page Gestion des Clients.");
        }
    }
}
