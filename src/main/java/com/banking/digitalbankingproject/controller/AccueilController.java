package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccueilController {

    public void Gestion_clients(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");

    }

    public void Gestion_Comptes(ActionEvent event) throws IOException{
        Outils.load(event, "Gestion des Comptes", "/fxml/comptes.fxml");
    }
    public void Gestion_Operations(ActionEvent event) throws IOException{
        Outils.load(event, "Gestion des Opérations", "/fxml/operations.fxml");
    }
}
