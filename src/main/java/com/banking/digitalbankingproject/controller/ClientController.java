package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.io.IOException;

public class ClientController {

    @FXML
    private TextField txtRecherche;

    @FXML
    private TableView<Client> tableViewClients;

    @FXML
    private TableColumn<Client, String> colNom;

    @FXML
    private TableColumn<Client, String> colPrenom;

    @FXML
    private TableColumn<Client, String> colEmail;

    @FXML
    private TextField e_client;

    @FXML
    private Pane form_client;

    @FXML
    private TextField n_client;

    @FXML
    private TextField p_client;

    private IClient clientService = new ClientImpl();
    @Getter
    private static Client clientselect = null;
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes du TableView
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        form_client.setVisible(false);
        clientselect = null;

        // Charger les clients dans le TableView
        chargerClients();

        // Configurer la barre de recherche
        configurerBarreRecherche();

        // Ajouter le menu contextuel
        tableViewClients.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem modifierItem = new MenuItem("Modifier");
            modifierItem.setOnAction(event -> {
                clientselect = row.getItem();
                if (clientselect != null) {
                    n_client.setText(clientselect.getNom());
                    p_client.setText(clientselect.getPrenom());
                    e_client.setText(clientselect.getEmail());
                    form_client.setVisible(true);
                }
            });

            MenuItem supprimer = new MenuItem("Supprimer");
            supprimer.setOnAction(event -> {
                Client client = row.getItem();
                boolean supprimerClient = clientService.supprimerClient(client);
               if (supprimerClient) {
                   Notification.NotifSuccess("supprimer","client supprimer");
                   initialize();
               } else{
                   Notification.NotifError("pas supprimer","client pas supprimer");
               }
            });

            MenuItem Selectionner = new MenuItem("Selectionner");
            Selectionner.setOnAction(event -> {
                clientselect = row.getItem();
                Notification.NotifSuccess("selectionner", "client selectionner");
            });



            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            contextMenu.getItems().addAll(modifierItem, supprimer,Selectionner);

            return row;
        });
    }

    private void chargerClients() {
        clientsList.clear();
        clientsList.addAll(clientService.getAllClients());
        tableViewClients.setItems(clientsList);
    }

    private void configurerBarreRecherche() {
        // Créer une FilteredList pour filtrer les clients
        FilteredList<Client> filteredList = new FilteredList<>(clientsList, p -> true);

        // Lier la barre de recherche au filtre
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(client -> {
                // Si la barre de recherche est vide, afficher tous les clients
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convertir la recherche en minuscules pour une recherche insensible à la casse
                String rechercheMinuscule = newValue.toLowerCase();

                // Vérifier si le nom ou le prénom contient la recherche
                if (client.getNom().toLowerCase().contains(rechercheMinuscule) ||
                        client.getPrenom().toLowerCase().contains(rechercheMinuscule)) {
                    return true;
                }

                // Aucun résultat trouvé
                return false;
            });
        });

        // Créer une SortedList pour trier les résultats filtrés
        SortedList<Client> sortedList = new SortedList<>(filteredList);

        // Lier le TableView à la SortedList
        sortedList.comparatorProperty().bind(tableViewClients.comparatorProperty());
        tableViewClients.setItems(sortedList);
    }

    public void Retour_Accueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouter_client(ActionEvent event) {
        form_client.setVisible(true);
        //clientselect = tableViewClients.getSelectionModel().getSelectedItem();

    }

    @FXML
    void valider_client(ActionEvent event) {
        Client client =new Client();
        boolean ok = false;
        if (clientselect != null) {
            client.setId(clientselect.getId());
        }
        client.setNom(n_client.getText());
        client.setPrenom(p_client.getText());
        client.setEmail(e_client.getText());
        if (clientselect == null) {
             ok = clientService.createClient(client);
        }else{
            ok = clientService.modifierClient(client);
        }

        if (ok) {
            Notification.NotifSuccess("succes", "Client modifier");
            form_client.setVisible(false);
            initialize();
        }else {
            Notification.NotifError("erreur", "erreur");
        }

    }


}