package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ModifierClientController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnRetour;

    private Client client;
    private Client clientSel;
    private IClient clientService = new ClientImpl();

    public void initialize(){
        clientSel = ClientController.getClientselect();
        setClient(clientSel);
    }
    @FXML
    private void modifierClient(ActionEvent event) {
        clientSel = ClientController.getClientselect();
        if (clientSel == null) {
            Outils.showError("Erreur", "Aucun client sélectionné.");
            return;
        }

        try {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();

            // Vérifier que les champs ne sont pas vides
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                Outils.showError("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            // Mettre à jour le client
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setEmail(email);
            clientService.modifierClient(client);

            // Afficher un message de succès et fermer la fenêtre
            Outils.showSuccess("Succès", "Client modifié avec succès.");
            Stage stage = (Stage) btnModifier.getScene().getWindow();


            stage.close();
        } catch (Exception e) {
            Outils.showError("Erreur", "Erreur lors de la modification du client.");
        }
    }

    @FXML
    private void retourGestionClients(ActionEvent event) {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }

    public void setClient(Client client) {
        this.client = client;
        txtNom.setText(client.getNom());
        txtPrenom.setText(client.getPrenom());
        txtEmail.setText(client.getEmail());
    }
}