package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionClientsController {

    @FXML
    private AnchorPane gestionClientsPane;

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

    private IClient clientService = new ClientImpl();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes du TableView
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

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
                Client client = row.getItem();
                allerModifierClient(client);
            });

            MenuItem supprimerItem = new MenuItem("Supprimer");
            supprimerItem.setOnAction(event -> {
                Client client = row.getItem();
                allerSupprimerClient(client);
            });

            contextMenu.getItems().addAll(modifierItem, supprimerItem);

            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

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

    @FXML
    private void rechercherClient(ActionEvent event) {
        // La recherche est déjà gérée par le Listener, cette méthode peut rester vide
    }

    @FXML
    private void allerAjouterClient(ActionEvent event) {
        try {
            Outils.load(event, "Ajouter Client", "/fxml/ajouterClient.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerModifierClient(ActionEvent event) {
        Client clientSelectionne = tableViewClients.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un client à modifier.");
            return;
        }

        allerModifierClient(clientSelectionne);
    }

    private void allerModifierClient(Client clientSelectionne) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierClient.fxml"));
            Parent root = loader.load();

            // Passer le client sélectionné au contrôleur
            ModifierClientController controller = loader.getController();
            controller.setClient(clientSelectionne);

            // Afficher la vue
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Client");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerSupprimerClient(ActionEvent event) {
        Client clientSelectionne = tableViewClients.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un client à supprimer.");
            return;
        }

        allerSupprimerClient(clientSelectionne);
    }

    private void allerSupprimerClient(Client clientSelectionne) {
        // Supprimer le client
        clientService.supprimerClient(clientSelectionne);

        // Rafraîchir la liste des clients
        chargerClients();
    }

    @FXML
    private void rafraichirClients(ActionEvent event) {
        chargerClients();
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}