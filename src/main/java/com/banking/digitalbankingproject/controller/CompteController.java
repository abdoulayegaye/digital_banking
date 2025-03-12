package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.Notifications;

import java.sql.Timestamp;
import java.util.List;

public class CompteController {
    @FXML
    private TableView<Compte> comptesTable;
    @FXML
    private TableColumn<Compte, Integer> idColumn;
    @FXML
    private TableColumn<Compte, String> numeroColumn;
    @FXML
    private TableColumn<Compte, Double> balanceColumn;
    @FXML
    private TableColumn<Compte, String> clientColumn;
    @FXML
    private TableColumn<Compte, Timestamp> dateColumn;
    @FXML
    private TableColumn<Compte, Void> actionsColumn;
    @FXML
    private TextField searchField;

    private final ICompte compteService;

    public CompteController() {
        this.compteService = new CompteImpl();
    }

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        clientColumn.setCellValueFactory(cellData -> {
            Compte compte = cellData.getValue();
            String clientInfo = compte.getClient() != null ? 
                compte.getClient().getNom() + " " + compte.getClient().getPrenom() : "";
            return javafx.beans.binding.Bindings.createStringBinding(() -> clientInfo);
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Configuration de la colonne d'actions
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Compte compte = getTableView().getItems().get(getIndex());
                    handleDeleteCompte(compte);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Chargement des données
        loadComptes();
    }

    private void loadComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        comptesTable.setItems(FXCollections.observableArrayList(comptes));
    }

    @FXML
    private void handleSearchAction() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        List<Compte> comptes = compteService.getAllComptes();
        List<Compte> filteredComptes = comptes.stream()
            .filter(compte -> 
                compte.getNumero().toLowerCase().contains(searchTerm) ||
                (compte.getClient() != null && 
                 (compte.getClient().getNom().toLowerCase().contains(searchTerm) ||
                  compte.getClient().getPrenom().toLowerCase().contains(searchTerm)))
            )
            .toList();
        comptesTable.setItems(FXCollections.observableArrayList(filteredComptes));
    }

    @FXML
    private void handleNewCompteAction(ActionEvent event) {
        try {
            Outils.load(event, "Nouveau Compte", "nouveau-compte");
        } catch (Exception e) {
            e.printStackTrace();
            Notifications.create()
                .title("Erreur")
                .text("Erreur lors de l'ouverture du formulaire: " + e.getMessage())
                .showError();
        }
    }

    @FXML
    private void handleRetourAction(ActionEvent event) {
        try {
            Outils.load(event, "Digital Banking", "accueil");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteCompte(Compte compte) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Suppression de compte");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce compte ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (compteService.deleteCompte(compte.getId())) {
                    loadComptes();
                    Notifications.create()
                        .title("Succès")
                        .text("Compte supprimé avec succès")
                        .showInformation();
                } else {
                    Notifications.create()
                        .title("Erreur")
                        .text("Erreur lors de la suppression du compte")
                        .showError();
                }
            }
        });
    }
}
