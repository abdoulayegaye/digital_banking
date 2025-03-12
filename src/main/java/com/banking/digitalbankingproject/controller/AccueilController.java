package com.banking.digitalbankingproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AccueilController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void showClient(ActionEvent event) throws IOException {
        loadPage(event, "/fxml/clients.fxml");
    }

    @FXML
    private void showOperation(ActionEvent event) throws IOException {
        loadPage(event, "/fxml/operations.fxml");
    }

    @FXML
    private void showCompte(ActionEvent event) throws IOException {
        loadPage(event, "/fxml/comptes.fxml");
    }

    private void loadPage(ActionEvent event, String fxmlPath) {
        try {
            URL fxmlURL = getClass().getResource(fxmlPath);
            if (fxmlURL == null) {
                System.out.println("Fichier FXML non trouvé : " + fxmlPath);
                return;
            }

            Parent root = FXMLLoader.load(fxmlURL);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
