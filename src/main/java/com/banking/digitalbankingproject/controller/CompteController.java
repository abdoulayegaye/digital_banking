package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CompteController {

    @FXML
    private Button ajouterBtn;

    @FXML
    private TextField clienidTfd;

    @FXML
    private TextField numeroCompteTfd;

    @FXML
    private Button retourBtn;

    @FXML
    private TextField soldeTfd;

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des comptes", "/FXML/interfaceComptes.fxml");
    }

    @FXML
    void save(ActionEvent event) {
        ICompte iCompte = new CompteImpl();

        String numero = numeroCompteTfd.getText();
        String solde = soldeTfd.getText();
        String client_id = clienidTfd.getText();

        if (numero.isEmpty() || solde.isEmpty() || client_id.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        try {
            // Créer un objet Compte
            Compte compte = new Compte();
            compte.setNumero(numero);
            compte.setBalance(Double.parseDouble(solde));

            // Créer un objet Client avec l'ID client
            Client client = new Client();
            client.setId(Integer.parseInt(client_id)); // Supposons que la classe Client a une méthode setId
            compte.setClient(client); // Affecter l'objet Client au compte

            int ok = iCompte.addCompte(compte);

            if (ok > 0) {
                Notification.NotifSuccess("Succès", "Compte ajouté avec succès !");
                clearFields();
                Outils.load(event, "Gestion des Comptes", "/fxml/interfaceComptes.fxml");
            } else {
                Notification.NotifError("Erreur", "Échec de l'ajout du compte !");
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le solde ou l'ID client n'est pas un nombre valide !");
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Une erreur est survenue lors de l'ajout !");
        }
    }

    public void clearFields() {
        numeroCompteTfd.setText("");
        soldeTfd.setText("");
        clienidTfd.setText("");
    }
}