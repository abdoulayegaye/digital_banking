package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GestionClientsController {

    @FXML private AnchorPane gestionClientsPane;
    @FXML private Button btnRetour;
    @FXML private TextField txtRecherche;
    @FXML private Button btnRechercher;
    @FXML private TableView<Client> tableViewClients;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colPrenom;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private Button btnAjouterClient;
    @FXML private Button btnModifierClient;
    @FXML private Button btnSupprimerClient;
    @FXML private Button btnRefresh;

    private final IClient clientService; // Injection via constructeur
    private final ObservableList<Client> clientsList = FXCollections.observableArrayList();

    // Constructeur pour injection de dépendances
    public GestionClientsController(IClient clientService) {
        this.clientService = clientService != null ? clientService : new ClientImpl();
    }

    // Constructeur par défaut requis pour FXML
    public GestionClientsController() {
        this(null);
    }

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Charger les clients
        chargerClients();

        // Configurer la barre de recherche
        configurerBarreRecherche();

        // Ajouter le menu contextuel
        ajouterMenuContextuel();
    }

    private void chargerClients() {
        clientsList.clear();
        var clients = clientService.getAllClients();
        if (clients != null) {
            clientsList.addAll(clients);
        } else {
            Outils.showError("Erreur", "Impossible de charger la liste des clients.");
        }
        tableViewClients.setItems(clientsList);
    }

    private void configurerBarreRecherche() {
        FilteredList<Client> filteredList = new FilteredList<>(clientsList, p -> true);
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return client.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        client.getPrenom().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Client> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableViewClients.comparatorProperty());
        tableViewClients.setItems(sortedList);
    }

    private void ajouterMenuContextuel() {
        tableViewClients.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem modifierItem = new MenuItem("Modifier");
            modifierItem.setOnAction(event -> allerModifierClient(row.getItem()));

            MenuItem supprimerItem = new MenuItem("Supprimer");
            supprimerItem.setOnAction(event -> allerSupprimerClient(row.getItem()));

            contextMenu.getItems().addAll(modifierItem, supprimerItem);
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }

    @FXML
    private void rechercherClient() {
        // Méthode vide car la recherche est gérée dynamiquement par le listener
    }

    @FXML
    private void allerAjouterClient() {
        try {
            Outils.load(new javafx.event.ActionEvent(btnAjouterClient, null), "Ajouter Client", "/fxml/ajouterClient.fxml");
        } catch (IOException e) {
            Outils.showError("Erreur", "Impossible de charger la fenêtre d'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void allerModifierClient() {
        Client clientSelectionne = tableViewClients.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un client à modifier.");
            return;
        }
        allerModifierClient(clientSelectionne);
    }

    private void allerModifierClient(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierClient.fxml"));
            Parent root = loader.load();
            ModifierClientController controller = loader.getController();
            controller.setClient(client);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Client");
            stage.show();
        } catch (IOException e) {
            Outils.showError("Erreur", "Impossible de charger la fenêtre de modification : " + e.getMessage());
        }
    }

    @FXML
    private void allerSupprimerClient() {
        Client clientSelectionne = tableViewClients.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un client à supprimer.");
            return;
        }
        allerSupprimerClient(clientSelectionne);
    }

    private void allerSupprimerClient(Client client) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer un client");
        alert.setContentText("Voulez-vous vraiment supprimer " + client.getNom() + " " + client.getPrenom() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                clientService.supprimerClient(client);
                chargerClients();
            } catch (Exception e) {
                Outils.showError("Erreur", "Erreur lors de la suppression : " + e.getMessage());
            }
        }
    }

    @FXML
    private void rafraichirClients() {
        chargerClients();
    }

    @FXML
    private void retourAccueil() {
        try {
            Outils.load(new javafx.event.ActionEvent(btnRetour, null), "Accueil", "/fxml/accueil.fxml");
        } catch (IOException e) {
            Outils.showError("Erreur", "Impossible de retourner à l'accueil : " + e.getMessage());
        }
    }
}