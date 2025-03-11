package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ClientController {

    @FXML
    private Button btnRetour;

    @FXML
    private Button btnajout;

    @FXML
    private Button btnlisteClient;

    @FXML
    void ajout_client(ActionEvent event) throws IOException {

        Outils.load(event, "Bienvenue à la Gestin des clients", "/fxml/ajout.fxml");

    }

    @FXML
    void list_client(ActionEvent event) throws IOException {

        Outils.load(event, "Bienvenue à la Gestin des clients", "/fxml/listeClient.fxml");

    }

    @FXML
    void retour_gestion(ActionEvent event) throws IOException {

        Outils.load(event, "Bienvenue à la Gestin des clients", "/fxml/accueil.fxml");

    }

}
