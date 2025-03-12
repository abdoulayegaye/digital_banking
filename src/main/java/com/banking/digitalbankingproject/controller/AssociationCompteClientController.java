package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class AssociationCompteClientController {
    private Client client;

    @FXML
    private Button enregistrerBtn;
    @FXML
    private ComboBox<Client> listeClientTfd;
    @FXML
    private TextField numCompteTfd;
    @FXML
    private Button retourBtn;

    public void initialize() {
        IClient iClient = new ClientImpl();
        List<Client> clients = iClient.getAllClients();
        ObservableList<Client> clientList = FXCollections.observableArrayList(clients);
        listeClientTfd.setItems(clientList);

        //StringConverter pour afficher "Nom Prénom" et récupérer l'ID
        listeClientTfd.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return (client != null) ? client.getNom() + " " + client.getPrenom() : "";
            }

            @Override
            public Client fromString(String string) {
                return listeClientTfd.getItems().stream()
                        .filter(client -> (client.getNom() + " " + client.getPrenom()).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    void save(ActionEvent event) {
        ICompte iCompte = new CompteImpl();

        Client clientSelectionne = listeClientTfd.getValue();
        if (clientSelectionne == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client !");
            return;
        }

        String numeroCompte = numCompteTfd.getText();
        if (numeroCompte.isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez entrer un numéro de compte !");
            return;
        }

        Compte compte = iCompte.getCompteByNumero(numeroCompte);
        if (compte == null) {
            Notification.NotifError("Erreur", "Compte introuvable !");
            return;
        }

        try {
            int ok = iCompte.associerCompteClient(numeroCompte, clientSelectionne.getId());

            if (ok > 0) {
                Notification.NotifSuccess("Succès", "Compte mis à jour avec succès !");
                Outils.load(event, "Gestion des Comptes", "/fxml/interfaceComptes.fxml");
            } else {
                Notification.NotifError("Erreur", "Échec de la mise à jour du compte !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Une erreur est survenue !");
        }
    }

    public void getData(Compte compte) {
        if (compte != null) {
            this.client = compte.getClient();

            numCompteTfd.setText(compte.getNumero());

            // Charger tous les clients dans la liste déroulante
            IClient iClient = new ClientImpl();
            listeClientTfd.getItems().setAll(iClient.getAllClients());

            // Sélectionner automatiquement le client actuel
            if (compte.getClient() != null) {
                listeClientTfd.setValue(compte.getClient());
            }
        }
    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Comptes", "/FXML/interfaceComptes.fxml");
    }
}
