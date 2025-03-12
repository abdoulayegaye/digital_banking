package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.banking.digitalbankingproject.tools.Notification.NotifSuccess;

public class AjouterClientController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private Button btnAjouter;
    @FXML private Button btnRetour;

    private final IClient clientService;

    // Constructeur pour injection
    public AjouterClientController() {
        this.clientService = new ClientImpl();
    }

    // Constructeur avec injection (optionnel)
    public AjouterClientController(IClient clientService) {
        this.clientService = clientService != null ? clientService : new ClientImpl();
    }

    @FXML
    private void initialize() {
        // Actions définies dans le FXML, mais peuvent être conservées ici si préféré
    }

    @FXML
    private void ajouterClient() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();

        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Outils.showError("Erreur", "Tous les champs sont obligatoires.");
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Outils.showError("Erreur", "L'email n'est pas valide.");
            return;
        }

        Client client = new Client(nom, prenom, email);
        if (clientService.createClient(client)) {
            NotifSuccess("Succès", "Client ajouté avec succès");
            retourGestionClients();
        } else {
            Outils.showError("Erreur", "Échec de l'ajout du client.");
        }
    }

    @FXML
    private void retourGestionClients() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/gestionClients.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnRetour.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Gestion des Clients");
            stage.show();
        } catch (java.io.IOException e) {
            Outils.showError("Erreur", "Impossible de retourner à la gestion des clients : " + e.getMessage());
        }
    }
}