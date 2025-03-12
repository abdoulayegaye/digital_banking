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
import javafx.scene.text.Text;

import java.io.IOException;




public class RetraitController {
    @FXML
    private Text num_client;



    private final OperationImpl operationService = new OperationImpl();
    private Compte compte = CompteController.getCompteselect();

    @FXML
    private TextField montantField;

    @FXML
    private Text messageText;

    @FXML
    private Button btnRetrait;
    @FXML
    public void initialize() {
        num_client.setText("Numero de compte: " + compte.getNumero());

    }

    @FXML
    public void enregistrer_r(ActionEvent event) {
        try {
            double montant = Double.parseDouble(montantField.getText());
            if (montant <= 0) {
                messageText.setText("Le montant doit être supérieur à zéro.");
                return;
            }

            Operation retrait = new Operation();
            retrait.setType(TypeOperation.RETRAIT);
            retrait.setCompte(compte);
            retrait.setAmount(montant);
            boolean success = operationService.CreateOperation(retrait);

            if (success) {
                messageText.setText("Retrait effectué avec succès !");
                montantField.clear();
                Notification.NotifSuccess("succes","Votre retrait a ete effectué");
                Outils.load(event,"Operation","/fxml/operations.fxml");
            } else {
                messageText.setText("Fonds insuffisants.");
            }
        } catch (NumberFormatException e) {
            messageText.setText("Veuillez entrer un montant valide.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
