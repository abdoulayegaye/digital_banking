package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lombok.Setter;

import java.io.IOException;
import java.time.Instant;

public class DepotController {
    private final OperationImpl operationService = new OperationImpl();
    @Setter
    private Compte compte = CompteController.getCompteselect();

    @FXML
    private TextField montantField;
    @FXML
    private Button btnDepot;
    @FXML
    private Text messageText;
    @FXML
    private AnchorPane form_d;

    @FXML
    private Text num_client;


    public void initialize() {
        num_client.setText("Numero de compte: " + compte.getNumero());

    }


    @FXML
    private void enregistrer_d(ActionEvent event) {
        try {
            double montant = Double.parseDouble(montantField.getText());
            if (montant <= 0) {
                messageText.setText("Le montant doit être supérieur à zéro.");
                return;
            }

            Operation depot = new Operation();
            depot.setType(TypeOperation.DEPOT);
            depot.setCompte(compte);
            depot.setAmount(montant);
            boolean success = operationService.CreateOperation(depot);

            if (success) {
                messageText.setText("Dépôt effectué avec succès !");
                montantField.clear();
                Notification.NotifSuccess("succes","Votre dépot a ete effectué");
                Outils.load(event,"Operation","/fxml/operations.fxml");

            } else {
                messageText.setText("Échec du dépôt.");
            }
        } catch (NumberFormatException e) {
            messageText.setText("Veuillez entrer un montant valide.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
