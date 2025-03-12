package org.example.javafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.javafx.entities.Client;
import org.example.javafx.service.IClient;
import org.example.javafx.service.impl.ClientImpl;
import org.example.javafx.tools.Notification;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private IClient dao = new ClientImpl();
    private int ok;
    private Client selectedClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idcolum.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomcolum.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomcolum.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailcolum.setCellValueFactory(new PropertyValueFactory<>("email"));


        load();

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedClient = newSelection;
                nomTfd.setText(selectedClient.getNom());
                prenomTfd.setText(selectedClient.getPrenom());
                emailTfd.setText(selectedClient.getEmail());
            }
        });
    }

    public void load() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        List<Client> clientList = dao.getAllClients();
        clients.addAll(clientList);
        clientTable.setItems(clients);
    }

    @FXML
    private Button annulerbtn;

    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TextField emailTfd;

    @FXML
    private TableColumn<Client, String> emailcolum;

    @FXML
    private TableColumn<Client, Integer> idcolum;

    @FXML
    private Button modifierbtn;

    @FXML
    private TextField nomTfd;

    @FXML
    private TableColumn<Client, String> nomcolum;

    @FXML
    private TextField prenomTfd;

    @FXML
    private TableColumn<Client, String> prenomcolum;

    @FXML
    private Button supprimerbtn;

    @FXML
    private Button validerbtn;
    @FXML
    void delete(ActionEvent event) {
        if (selectedClient != null) {
            ok = dao.delete(selectedClient.getId());
            if (ok != 0) {
                Notification.NotifSuccess("Succès", "Client supprimé avec succès");
                load();
                reset(null);
            } else {
                Notification.NotifError("Erreur", "Échec de la suppression du client");
            }
        } else {
            Notification.NotifError("Erreur", "Aucun client sélectionné");
        }
    }
    @FXML
    void insert(ActionEvent event) {
        Client client = new Client();
        client.setNom(nomTfd.getText());
        client.setPrenom(prenomTfd.getText());
        client.setEmail(emailTfd.getText());

        ok = dao.create(client);
        if (ok != 0) {
            Notification.NotifSuccess("Succès", "Client ajouté avec succès");
            load();
            reset(null);
        } else {
            Notification.NotifError("Erreur", "Échec de l'ajout du client");
        }
    }

    @FXML
    void reset(ActionEvent event) {
        nomTfd.clear();
        prenomTfd.clear();
        emailTfd.clear();
        selectedClient = null;
        clientTable.getSelectionModel().clearSelection();
    }


    @FXML
    void update(ActionEvent event) {
        if (selectedClient != null) {
            selectedClient.setNom(nomTfd.getText());
            selectedClient.setPrenom(prenomTfd.getText());
            selectedClient.setEmail(emailTfd.getText());

            ok = dao.update(selectedClient);
            if (ok != 0) {
                Notification.NotifSuccess("Succès", "Client mis à jour avec succès");
                load();
                reset(null);
            } else {
                Notification.NotifError("Erreur", "Échec de la mise à jour du client");
            }
        } else {
            Notification.NotifError("Erreur", "Aucun client sélectionné");
        }
    }
}