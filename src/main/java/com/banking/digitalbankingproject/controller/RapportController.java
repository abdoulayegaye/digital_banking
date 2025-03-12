package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RapportController {

    @FXML
    private BorderPane mainContainer;

    @FXML
    public void initialize() {
        // Code d'initialisation
    }

    @FXML
    public void retourAccueil() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/accueil.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            if (stage != null) {
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Impossible de trouver la fenêtre actuelle");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de navigation");
            alert.setContentText("Impossible de retourner à l'accueil: " + e.getMessage());
            alert.showAndWait();
        }
    }
} 