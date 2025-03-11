package com.banking.digitalbankingproject.controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;

import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class AccueilController {
    CompteController compteController = new CompteController();

    private AnchorPane pane;

    @FXML
    private AnchorPane Jpane;



    @FXML
    private Label nom;


    @FXML
    void clickdelete(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/delete.fxml"));
            Stage stage = new Stage();//(Stage) ((Node) event.getS vource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            //  Outils.load(event, "Bienvenue à Digital Banking", "l/fxml/clients.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void clickhistorique(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/historique.fxml"));
            Stage stage = new Stage();//(Stage) ((Node) event.getS vource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            //  Outils.load(event, "Bienvenue à Digital Banking", "l/fxml/clients.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void compteclick(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/comptes.fxml"));
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
    void customerclick(ActionEvent event) {
        try {
            CompteController comp = new CompteController();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/clients.fxml"));
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
    void soldeclick(ActionEvent event) {
        try {
            CompteController comp = new CompteController();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/solde.fxml"));
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
    void depotclick(ActionEvent event) {

        try {
            CompteController comp = new CompteController();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/depot.fxml"));
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
    void transferclick(ActionEvent event) {
        try {
            CompteController comp = new CompteController();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/transfert.fxml"));
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
    void clickrelever(ActionEvent event) {
        try {
            CompteController comp = new CompteController();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/relever.fxml"));
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
