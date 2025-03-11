package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.util.List;

public class ClientController {
    @FXML
    private TableView<Client> tableview;

    @FXML
    private Pane pane;
    @FXML
    private TextField nomTFD;
    @FXML
    private TextField prenomTFD;
    @FXML
    private TextField emailTFD;
    @FXML
    private TextField idTFD;
    @FXML
    private Text TextL;
    @FXML
    private Pane LASPan;
    @FXML
    private TextField searchTFD;

    @FXML
    private TableColumn<Client, Integer> idCol;
    @FXML
    private TableColumn<Client, Integer> nomCol;
    @FXML
    private TableColumn<Client, Integer> prenomCol;
    @FXML
    private TableColumn<Client, Integer> emailCol;
    private IClient iclient = new ClientImpl();
    private List<Client> clients;
    private FilteredList<Client> filteredList;
    @Getter
    private static Client clientCompte = null;



    public void initialize(){

        clients = iclient.getAllClients();

        if(clients == null){
            TextL.setText("Aucun Client à Afficher");
        }else {
            LASPan.setVisible(true);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

            prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

            tableview.setVisible(true);
            tableview.setPrefHeight(2*tableview.getItems().size());
            Ajouter_contextMenu();
            filteredList = new FilteredList<>(FXCollections.observableList(clients), p -> true);
            tableview.setItems(filteredList);

            searchTFD.textProperty().addListener((observable, olValue, newValue) -> {
                filtrerUtilisateurs(newValue);
            } );
        }
    }

    public void Accueil(ActionEvent event) throws IOException {
        Outils.load(event, "Acceuil", "/fxml/accueil.fxml");
    }

    public void filtrerUtilisateurs(String recherche){
        filteredList.setPredicate(client -> {
            if (recherche == null || recherche.isEmpty()){
                return true;
            }
            if (client.getNom().toLowerCase().contains(recherche.toLowerCase())){
                return true;
            } else if (client.getPrenom().toLowerCase().contains(recherche.toLowerCase())) {
                return true;
            } else if (client.getEmail().toLowerCase().contains(recherche.toLowerCase())) {
                return true;
            }
            return false;
        });
    }

    public void Ajout_Client(ActionEvent event) throws IOException {
        Outils.load(event, "Ajout du Client", "/fxml/client_form.fxml");
    }
    public void Ajouter_contextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem modifier = new MenuItem("Modifier");
        MenuItem supprimer = new MenuItem("Supprimer");
        MenuItem selectionner = new MenuItem("Selectionner");

        contextMenu.getItems().addAll(selectionner, modifier, supprimer);

        modifier.setOnAction(event ->{
            Client client = tableview.getSelectionModel().getSelectedItem();

            nomTFD.setText(client.getNom());
            prenomTFD.setText(client.getPrenom());
            emailTFD.setText(client.getEmail());
            idTFD.setText(""+client.getId());
            pane.setVisible(true);
        });

        supprimer.setOnAction(event -> {
            ClientImpl clientImpl = new ClientImpl();
            Client client = tableview.getSelectionModel().getSelectedItem();
            boolean ok = clientImpl.deleteClient(client.getId());
            if (ok) {
                Notification.NotifSuccess("Success", "le Client a bien été supprimé");
                initialize();
            }else {
                Notification.NotifError("Erreur", "Impossible de supprimer ce client!!");
            }


        });

        selectionner.setOnAction(event ->{
            clientCompte = tableview.getSelectionModel().getSelectedItem();
            Notification.NotifSuccess("Success", "Client sélectionner");
        });

        tableview.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->{
            if(event.getButton() == MouseButton.SECONDARY){
                if (tableview.getSelectionModel().getSelectedItem() != null){
                    tableview.setContextMenu(contextMenu);
                }
            }
        });

    }


    public void Modifier_Client(ActionEvent event) {

        if(nomTFD.getText().isEmpty() || prenomTFD.getText().isEmpty() || emailTFD.getText().isEmpty()){
            Notification.NotifError("Error", "Tous les champs sont obligatoires");
        }else if (!EmailValidator.getInstance().isValid(emailTFD.getText().trim())){
            Notification.NotifError("Error", "Email invalide");
        }else {
            Client client = new Client();
            client.setNom(nomTFD.getText().trim());
            client.setPrenom(prenomTFD.getText().trim());
            client.setEmail(emailTFD.getText().trim());
            client.setId(Integer.parseInt(idTFD.getText()));

            ClientImpl clientImpl = new ClientImpl();
            clientImpl.updateClient(client);
            pane.setVisible(false);
            initialize();
            Notification.NotifSuccess("Success", "Le Client a été modifier avec succès");

        }
    }
    public Client getclientCompte() {
        return this.clientCompte;
    }
}
