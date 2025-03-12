package com.banking.digitalbankingproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class AccueilController {

        @FXML
        private Button clientsBtn, compteBtn, operationsBtn, seDeconnecterBtn;

        private Stage stage;
        private Scene scene;

        // Méthode pour changer de scène
        private void changerDeScene(ActionEvent event, String fxml, String titre) throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle(titre);
                stage.show();
        }

        @FXML
        private void goToClients(ActionEvent event) throws IOException {
                changerDeScene(event, "/fxml/clients.fxml", "Gestion des Clients");
        }

        @FXML
        private void goToComptes(ActionEvent event) throws IOException {
                changerDeScene(event, "/fxml/comptes.fxml", "Gestion des Comptes");
        }

        @FXML
        private void goToOperations(ActionEvent event) throws IOException {
                changerDeScene(event, "/fxml/interfaceOperations.fxml", "Opérations Bancaires");
        }

        @FXML
        private void seDeconnecter(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Déconnexion");
                alert.setHeaderText("Se déconnecter");
                alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                                changerDeScene(event, "/fxml/login.fxml", "Connexion");
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
}
