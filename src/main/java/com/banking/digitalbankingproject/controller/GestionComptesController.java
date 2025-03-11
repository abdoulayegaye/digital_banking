package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class GestionComptesController {
    @FXML private AnchorPane gestionComptesPane;
    @FXML private TextField txtRecherche;
    @FXML private TableView<Compte> tableViewComptes;
    @FXML private TableColumn<Compte, String> colNumero;
    @FXML private TableColumn<Compte, String> colClient;
    @FXML private TableColumn<Compte, Double> colSolde;
    @FXML private TableColumn<Compte, String> colEtat;
    @FXML private TableColumn<Compte, String> colDate;
    @FXML
    private Button btnRetour;

    private IClient clientService = new ClientImpl();
    private ICompte compteService = new CompteImpl();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colClient.setCellValueFactory(cellData -> {
            Compte compte = cellData.getValue();
            Client client = compte.getClient();
            return client != null ? new SimpleStringProperty(client.getPrenom() + " " + client.getNom() + " (" + client.getEmail() + ")") : new SimpleStringProperty("Non attribué");
        });
        colSolde.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colEtat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isActif() ? "Actif" : "Fermé"));
        colDate.setCellValueFactory(cellData -> {
            Instant instant = cellData.getValue().getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(Locale.FRANCE)
                    .withZone(ZoneId.systemDefault());
            return new SimpleStringProperty(formatter.format(instant));
        });
        chargerComptes();
        configurerBarreRecherche();
        tableViewComptes.setRowFactory(tv -> {
            TableRow<Compte> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem modifierItem = new MenuItem("Modifier");
            modifierItem.setOnAction(event -> {
                Compte compte = row.getItem();
                allerModifierCompte(compte);
            });
            MenuItem supprimerItem = new MenuItem("Supprimer");
            supprimerItem.setOnAction(event -> {
                Compte compte = row.getItem();
                allerSupprimerCompte(compte);
            });
            MenuItem associerClientItem = new MenuItem("Associer Client");
            associerClientItem.setOnAction(event -> {
                Compte compte = row.getItem();
                associerClient(compte);
            });
            contextMenu.getItems().addAll(modifierItem, supprimerItem, associerClientItem);
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }

    private void chargerComptes() {
        comptesList.clear();
        comptesList.addAll(compteService.getAllComptes());

        if (comptesList.isEmpty()) {
            System.out.println("⚠️ Aucun compte trouvé !");
        } else {
            comptesList.forEach(c -> System.out.println("✅ Compte : " + c.getNumero() + " - Solde : " + c.getBalance() + " - Client : " + (c.getClient() != null ? c.getClient().getNom() : "Aucun")));
        }

        tableViewComptes.setItems(comptesList);
    }

    private void configurerBarreRecherche() {
        FilteredList<Compte> filteredList = new FilteredList<>(comptesList, p -> true);
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(compte -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String rechercheMinuscule = newValue.toLowerCase();
                if (compte.getNumero().toLowerCase().contains(rechercheMinuscule) ||
                        (compte.getClient() != null &&
                                (compte.getClient().getNom() + " " + compte.getClient().getPrenom() + " " + compte.getClient().getEmail()).toLowerCase().contains(rechercheMinuscule))) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Compte> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableViewComptes.comparatorProperty());
        tableViewComptes.setItems(sortedList);
    }

    @FXML
    private void rechercherCompte(ActionEvent event) {
        // La recherche est gérée par le listener.
    }

    @FXML
    private void allerAjouterCompte(ActionEvent event) {
        try {
            Outils.load(event, "Ajouter Compte", "/fxml/ajouterCompte.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page d'ajout de compte.");
        }
    }

    @FXML
    private void allerModifierCompte(ActionEvent event) {
        Compte compteSelectionne = tableViewComptes.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un compte à modifier.");
            return;
        }
        allerModifierCompte(compteSelectionne);
    }

    private void allerModifierCompte(Compte compteSelectionne) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierCompte.fxml"));
            Parent root = loader.load();
            ModifierCompteController controller = loader.getController();
            controller.setCompte(compteSelectionne);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Compte");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page de modification de compte.");
        }
    }

    @FXML
    private void allerSupprimerCompte(ActionEvent event) {
        Compte compteSelectionne = tableViewComptes.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un compte à supprimer.");
            return;
        }
        allerSupprimerCompte(compteSelectionne);
    }

    private void allerSupprimerCompte(Compte compteSelectionne) {
        compteService.supprimerCompte(compteSelectionne);
        chargerComptes();
    }

    private void associerClient(Compte compteSelectionne) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/associerClient.fxml"));
            Parent root = loader.load();
            AssocierClientController controller = loader.getController();
            controller.setCompte(compteSelectionne);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Associer Client");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page d'association du client.");
        }
    }

    @FXML
    private void rafraichirComptes(ActionEvent event) {
        chargerComptes();
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page Accueil.");
        }
    }
}
