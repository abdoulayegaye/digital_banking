package org.example.javafx.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.javafx.tools.Outils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilControler implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    void afficherClient(ActionEvent event) throws IOException {
        Outils.loadSub(event,"Gestion des Client","/fxml/clients.fxml");
    }
    @FXML
    void compte(ActionEvent event) {
        try {
            Outils.loadSub(event, "Gestion des Comptes", "/fxml/creecompte.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void Operation(ActionEvent event) {
        try {
            Outils.loadSub(event, "Gestion des Operations", "/fxml/operations.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
