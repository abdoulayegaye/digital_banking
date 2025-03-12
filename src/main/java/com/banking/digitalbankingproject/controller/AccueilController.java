package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class AccueilController {

    @FXML
    void accueil_cilient(ActionEvent event) {
        try {
            Outils.load(event, "Bienvenue sur la page client", "/fxml/clients.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void accueil_compte(ActionEvent event) {
        try {

            Outils.load(event, "Bienvenue sur la page compte", "/fxml/comptes.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void page_operation(ActionEvent event) {
        try {

            Outils.load(event, "Bienvenue sur la page operation", "/fxml/operations.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
