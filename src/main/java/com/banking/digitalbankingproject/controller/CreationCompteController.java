package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;

public class CreationCompteController {
    @FXML
    private ComboBox<String> comBoBox;
    @FXML
    private Button ajouterBtn;
    @FXML
    private TextField numeroTfd;
    @FXML
    private Button retourBtn;

    @FXML
    public void initialize() {
        comBoBox.getItems().addAll("Courant", "Épargne");
    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Comptes", "/FXML/interfaceComptes.fxml");
    }

    public void save(ActionEvent event) {
        ICompte iCompte = new CompteImpl();

        String numero = numeroTfd.getText();
        String type = comBoBox.getSelectionModel().getSelectedItem();

        if (numero.isEmpty() || type == null || type.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        Compte compte = new Compte(numero);
        compte.setType_compte(type);

        try {
            int ok = iCompte.addCompte(compte);

            if (ok > 0) {
                Notification.NotifSuccess("Succès", "Compte ajouté avec succès !");
                clearFields();
                Outils.load(event, "Gestion des Comptes", "/fxml/interfaceComptes.fxml");
            } else {
                Notification.NotifError("Erreur", "Échec de l'ajout du compte !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Une erreur est survenue lors de l'ajout !");
        }
    }

    public void clearFields(){
        numeroTfd.setText("");
    }

}
