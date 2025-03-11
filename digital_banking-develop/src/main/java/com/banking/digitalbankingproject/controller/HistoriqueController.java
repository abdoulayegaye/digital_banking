package com.banking.digitalbankingproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HistoriqueController {

    @FXML
    void depotclick(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/historiquedepot.fxml"));
            Stage stage = new Stage();//(Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            //  Outils.load(event, "Bienvenue à Digital Banking", "l/fxml/clients.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void retraitclick(ActionEvent event) {


        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/historiqueretrait.fxml"));
            Stage stage = new Stage();//(Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            //  Outils.load(event, "Bienvenue à Digital Banking", "l/fxml/clients.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void transfertclick(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/historiquetransfert.fxml"));
            Stage stage = new Stage();//(Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            //  Outils.load(event, "Bienvenue à Digital Banking", "l/fxml/clients.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
