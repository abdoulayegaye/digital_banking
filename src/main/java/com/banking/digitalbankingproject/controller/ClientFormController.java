package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.util.List;

public class ClientFormController {
    @FXML
    private TextField nomTFD;
    @FXML
    private TextField prenomTFD;
    @FXML
    private TextField emailTFD;
    private IClient iClient = new ClientImpl();



    public void Add_Client(ActionEvent event) throws IOException {


        if (nomTFD.getText().trim().isEmpty() || prenomTFD.getText().trim().isEmpty() || emailTFD.getText().trim().isEmpty()){
            Notification.NotifError("Error", "Tous les champs sont obligatoires");
        } else if (!EmailValidator.getInstance().isValid(emailTFD.getText().trim())) {
            Notification.NotifError("Error", "Email invalide");
        } else {
            Client client = new Client();
            client.setNom(nomTFD.getText().trim());
            client.setPrenom(prenomTFD.getText().trim());
            client.setEmail(emailTFD.getText().trim());

            boolean ok = iClient.createClient(client);
            if (ok){
                Notification.NotifSuccess("Success", "Client Créé avec succès");
                Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
            }else {
                Notification.NotifError("Error", "Erreur de création du Client");
            }
        }

    }

    public void Gestion_Clients(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
    }
}
