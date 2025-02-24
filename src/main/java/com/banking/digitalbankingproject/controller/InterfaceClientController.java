package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class InterfaceClientController {
    private int id;

    @FXML
    private Button ajoutClientBtn;
    @FXML
    private Button rechargeBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private TableView<Client> clientTbl;
    @FXML
    private TableColumn<Client, String> emailCol;
    @FXML
    private TableColumn<Client, Integer> idCol;
    @FXML
    private TableColumn<Client, String> nomCol;
    @FXML
    private TableColumn<Client, String> prenomCol;
    @FXML
    private Button rechercheBtn;
    @FXML
    private Button retourBtn;
    @FXML
    private TextField searchField;

    public void initialize() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem modifierItem = new MenuItem("Modifier");
        modifierItem.setOnAction(event -> updateClient());

        MenuItem supprimerItem = new MenuItem("Supprimer");
        supprimerItem.setOnAction(event -> deleteClient());

        contextMenu.getItems().addAll(modifierItem, supprimerItem);

        // Associer le menu au tableau (clic droit)
        clientTbl.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();

            // Associer le menu à chaque ligne
            row.setContextMenu(contextMenu);

            return row;
        });

        loadTable();
    }

    @FXML
    void pageAjoutClient(ActionEvent event) throws IOException {
        Outils.load(event, "Ajout Client", "/FXML/AjoutClients.fxml");
    }

    //Méthode pour charger les clients
    public void loadTable() {
        IClient iClient = new ClientImpl();
        ObservableList<Client> liste = FXCollections.observableArrayList(iClient.getAllClients());

        clientTbl.setItems(liste);

        idCol.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Client, String>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Client, String>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
    }

    //Méthode pour recharger la table après avoir terminé la recherche
    @FXML
    void recharge(ActionEvent event) {
        loadTable();
    }

    //Méthode retourner à l'acceuil
    @FXML
    void home(ActionEvent event) throws IOException {
        Outils.load(event, "Bienvenue à Digital Banking", "/fxml/accueil.fxml");
    }

    //Méthode pour supprimé un client
    public void deleteClient() {
        Client client = clientTbl.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le client ?");
        alert.setContentText("Voulez-vous vraiment supprimer " + client.getPrenom() + " " + client.getNom() + " ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                IClient iClient = new ClientImpl();
                int result = iClient.deleteClient(client);
                if (result > 0) {
                    clientTbl.getItems().remove(client);
                    Notification.NotifSuccess("Succès", "Client supprimé avec succès !");
                } else {
                    Notification.NotifError("Erreur", "Échec de la suppression du client !");
                }
            }
        });
    }

    //Méthode pour ouvrir la page modifier un client avec les infos du client concerné
    public void updateClient() {
        Client client = clientTbl.getSelectionModel().getSelectedItem();

        if (client == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à modifier !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ModifierClient.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre de modification
            ModifierClientController controller = loader.getController();
            controller.getData(client);

            // Ouvrir la nouvelle fenêtre
            clientTbl.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Impossible d'ouvrir la fenêtre de modification !");
        }
    }

    //Méthode pour rechercher un client via le nom ou l'email
    public void searchClients() {
        String searchText = searchField.getText();

        if (searchText.isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez entrer un nom ou un email !");
            return;
        }

        IClient iClient = new ClientImpl();
        List<Client> results = iClient.searchClientsByName(searchText);

        if (results.isEmpty()) {
            results = iClient.searchClientsByEmail(searchText);
        }

        ObservableList<Client> list = FXCollections.observableArrayList(results);
        clientTbl.setItems(list);
    }

}
