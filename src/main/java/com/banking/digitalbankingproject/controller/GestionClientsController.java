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
import java.util.Locale;

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
    @FXML
    private Button btnRetour;

    private IClient clientService = new ClientImpl();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        chargerClients();
        configurerBarreRecherche();

        // Ajout du menu contextuel
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
        FilteredList<Client> filteredList = new FilteredList<>(clientsList, p -> true);
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String rechercheMinuscule = newValue.toLowerCase(Locale.FRANCE);
                return client.getNom().toLowerCase(Locale.FRANCE).contains(rechercheMinuscule)
                        || client.getPrenom().toLowerCase(Locale.FRANCE).contains(rechercheMinuscule);
            });
        });
        SortedList<Client> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableViewClients.comparatorProperty());
        tableViewClients.setItems(sortedList);
    }

    @FXML
    private void allerAjouterClient(ActionEvent event) {
        try {
            Outils.load(event, "Ajouter un Client", "/fxml/ajouterClient.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page d'ajout de client.");
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
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page de modification de client.");
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
        clientService.supprimerClient(clientSelectionne);
        chargerClients();
    }

    @FXML
    private void rafraichirClients(ActionEvent event) {
        chargerClients();
    }

    @FXML
    private void rechercherClient(ActionEvent event) {
        // Vous pouvez laisser cette méthode vide si elle n'est pas encore implémentée.
        System.out.println("🔎 Recherche d'un client en cours...");
    }


    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page d'accueil.");
        }
    }
}
