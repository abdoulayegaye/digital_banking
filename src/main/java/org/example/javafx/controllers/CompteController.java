package org.example.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.javafx.tools.Outils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CompteController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void CreeCompte(ActionEvent event) {
        try {
            Outils.loadSub(event, "Creer Compte", "/fxml/creecompte.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


