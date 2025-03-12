package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class OperationController {


    @FXML
    void p_depot(ActionEvent event) throws IOException {
        if (CompteController.getCompteselect() != null) {
            Outils.load(event, "Bienvenue à la page de dépot", "/fxml/depot.fxml");
            MenuItem compte = new MenuItem();
            compte.setText("Numero: " + CompteController.getCompteselect().getNumero());
        } else {
            Notification.NotifError("erreur", "aucun compte selectionner");
        }
    }
    @FXML
    void p_historique(ActionEvent event) {
        try {

            Outils.load(event, "Bienvenue sur la page d'historique", "/fxml/historique.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void retour(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void p_virement(ActionEvent event) throws IOException {
        if (CompteController.getCompteselect() != null) {
            Outils.load(event, "Bienvenue à la page de virement", "/fxml/virement.fxml");
            MenuItem compte = new MenuItem();
            compte.setText("Numero: " + CompteController.getCompteselect().getNumero());
        } else {
            Notification.NotifError("erreur", "aucun compte selectionner");
        }

    }

    @FXML
    void p_retrait(ActionEvent event) throws IOException {
        if (CompteController.getCompteselect() != null) {
            Outils.load(event, "Bienvenue à la page de retrait", "/fxml/retrait.fxml");
            MenuItem compte = new MenuItem();
            compte.setText("Numero: " + CompteController.getCompteselect().getNumero());
        } else {
            Notification.NotifError("erreur", "aucun compte selectionner");
        }
    }

}
