package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;


public class InterfaceCompteController {
    @FXML
    private Button ajoutCompteBtn;
    @FXML
    private TableColumn<Compte, String> clientCol;
    @FXML
    private TableView<Compte> compteTbl;
    @FXML
    private TableColumn<Compte, Instant> dateCreationCol;
    @FXML
    private Button homeBtn;
    @FXML
    private TableColumn<Compte, Integer> idCol;
    @FXML
    private TableColumn<Compte, String> numeroCol;
    @FXML
    private TableColumn<Compte, String> statutCol;
    //@FXML
    //private TableColumn<Compte, String> typeCol;
    @FXML
    private Button rechargeBtn;
    @FXML
    private Button rechercheBtn;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Compte, Double> soldeCol;

    @FXML
    void home(ActionEvent event) throws IOException {
        Outils.load(event, "Bienvenue à Digital Banking", "/fxml/accueil.fxml");
    }

    @FXML
    void pageAjoutCompte(ActionEvent event) throws IOException {
        Outils.load(event, "Ajout Compte", "/FXML/comptes.fxml");
    }

    @FXML
    void recharge(ActionEvent event) {
        loadTable();
    }

    @FXML
    void searchComptes(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez entrer un nom ou un email !");
            return;
        }

        ICompte iCompte = new CompteImpl();
        List<Compte> results = iCompte.searchCompteByNumero(searchText);

        if (results.isEmpty()) {
            results = iCompte.searchCompteByNumero(searchText);
        }

        ObservableList<Compte> list = FXCollections.observableArrayList(results);
        compteTbl.setItems(list);
    }

    public void initialize() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem modifierItem = new MenuItem("Associer Client");
        modifierItem.setOnAction(event -> associerClient());

        MenuItem supprimerItem = new MenuItem("Fermer Compte");
        supprimerItem.setOnAction(event -> fermerCompte());

        MenuItem consulterItem = new MenuItem("Consulter Solde");
        consulterItem.setOnAction(event -> soldeCompte());

        MenuItem consulterHistoriqueItem = new MenuItem("Consulter Historique");
        consulterHistoriqueItem.setOnAction(event -> historiqueCompte());

        contextMenu.getItems().addAll(modifierItem, consulterItem, consulterHistoriqueItem, supprimerItem);

        compteTbl.setRowFactory(tv -> {
            TableRow<Compte> row = new TableRow<>();

            row.setContextMenu(contextMenu);

            return row;
        });
        loadTable();
    }

    public void loadTable() {
        ICompte iCompte = new CompteImpl();
        ObservableList<Compte> liste = FXCollections.observableArrayList(iCompte.getAllComptes());

        compteTbl.setItems(liste);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        soldeCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        dateCreationCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.systemDefault());

        dateCreationCol.setCellFactory(column -> new TableCell<Compte, Instant>() {
            @Override
            protected void updateItem(Instant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        clientCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getClient() != null ?
                                cellData.getValue().getClient().getPrenom() + " " + cellData.getValue().getClient().getNom(): "Non attribué"
                )
        );
        statutCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isStatut() ? "Actif" : "Fermé")
        );
        //typeCol.setCellValueFactory(new PropertyValueFactory<>("type_compte"));

    }

    public void fermerCompte() {
        Compte compte = compteTbl.getSelectionModel().getSelectedItem();

        if (compte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à fermer !");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de fermeture de compte");
        alert.setHeaderText("Fermer le compte ?");
        alert.setContentText("Voulez-vous vraiment fermer le compte : " + compte.getNumero() + " ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ICompte iCompte = new CompteImpl();
                int result = iCompte.fermerCompte(compte.getId());

                if (result > 0) {
                    compteTbl.getItems().remove(compte);
                    Notification.NotifSuccess("Succès", "Compte fermé avec succès !");
                    loadTable();
                } else {
                    Notification.NotifError("Erreur", "Échec de la fermeture du compte !");
                }
            }
        });
    }

    public void associerClient() {
        Compte compte = compteTbl.getSelectionModel().getSelectedItem();

        if (compte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à modifier !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssociationCompteClientController.fxml"));
            Parent root = loader.load();

            AssociationCompteClientController controller = loader.getController();
            controller.getData(compte);

            compteTbl.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Impossible d'ouvrir la fenêtre d'association !");
        }
    }

    public void soldeCompte() {
        Compte compte = compteTbl.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Consultation de solde");
        alert.setHeaderText("Solde actuel du compte : " + compte.getNumero());
        alert.setContentText("Solde : " + compte.getBalance() + " FCFA");
        alert.showAndWait();
    }

    public Pair<LocalDate, LocalDate> demanderIntervalleDate() {
        Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Sélectionner une période");
        dialog.setHeaderText("Veuillez choisir une plage de dates pour l'historique");

        ButtonType validerButton = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker dateDebutPicker = new DatePicker();
        DatePicker dateFinPicker = new DatePicker();

        grid.add(new Label("Date de début :"), 0, 0);
        grid.add(dateDebutPicker, 1, 0);
        grid.add(new Label("Date de fin :"), 0, 1);
        grid.add(dateFinPicker, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validerButton) {
                return new Pair<>(dateDebutPicker.getValue(), dateFinPicker.getValue());
            }
            return null;
        });

        Optional<Pair<LocalDate, LocalDate>> result = dialog.showAndWait();

        return result.orElse(null);
    }

    public void historiqueCompte() {
        Compte compte = compteTbl.getSelectionModel().getSelectedItem();

        if (compte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte !");
            return;
        }

        Pair<LocalDate, LocalDate> dates = demanderIntervalleDate();
        if (dates == null || dates.getKey() == null || dates.getValue() == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner une plage de dates !");
            return;
        }

        LocalDate dateDebut = dates.getKey();
        LocalDate dateFin = dates.getValue();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/historique.fxml"));
            Parent root = loader.load();

            HistoriqueController controller = loader.getController();
            controller.getData(compte, dateDebut.toString(), dateFin.toString());

            compteTbl.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Impossible d'ouvrir la fenêtre d'historique !");
        }
    }

}
