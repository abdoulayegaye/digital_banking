package com.example.projet_java_fx.controllers;

import com.example.projet_java_fx.database.Db;
import com.example.projet_java_fx.entity.Clients;
import com.example.projet_java_fx.entity.Comptes;
import com.example.projet_java_fx.service.IClient;
import com.example.projet_java_fx.service.ICompte;
import com.example.projet_java_fx.service.impl.ClientImpl;
import com.example.projet_java_fx.service.impl.CompteImpl;
import com.example.projet_java_fx.tools.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CompteController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("num"));
        soldeCol.setCellValueFactory(new PropertyValueFactory<>("solde"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        load();
        loadCompte();

    }



    private IClient clientdao = new ClientImpl();

    public void loadCompte() {
        ObservableList<Clients> clients = FXCollections.observableArrayList();
        List<Clients> clientsList = clientdao.getAllClients();
        for (Clients c: clientsList ){
            clients.add(c);
        }
        cli.setItems(clients);

        tab.getStyleClass().add("tab-compte");
        idCol.getStyleClass().add("table-column");
        soldeCol.getStyleClass().add("table-column");
        dateCol.getStyleClass().add("table-column");
        clientCol.getStyleClass().add("table-column");


    }

    private Db db = new Db();
    private ICompte dao = new CompteImpl();
    private int ok;
    public void load()
    {
        ObservableList<Comptes> compte = FXCollections.observableArrayList();
        List<Comptes>  comptes = dao.getAllComptes();
        for (Comptes c: comptes){
            compte.add(c);
        }
        tab.setItems(compte);
    }

    @FXML
    private Button ajouterBtn;

    @FXML
    private ComboBox<Clients> cli;

    @FXML
    private TableColumn<Comptes, Clients> clientCol;

    @FXML
    private Button consulterBtn;

    @FXML
    private TableColumn<Comptes, Date> dateCol;

    @FXML
    private DatePicker date;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private TableColumn<Comptes, Integer> idCol;

    @FXML
    private TableColumn<Comptes, String> numCol;

    @FXML
    private TextField numTfd;

    @FXML
    private TableColumn<Comptes, Double> soldeCol;

    @FXML
    private TextField soldeTfd;

    @FXML
    private Button telechargerBtn;

    @FXML
    void ajout(ActionEvent event) {
        Comptes c = new Comptes();
        c.setNumero(numTfd.getText());
        c.setSolde(Integer.parseInt(soldeTfd.getText()));

        // Convert LocalDate to Timestamp
        LocalDate localDate = date.getValue();
        if (localDate != null) {
            LocalDateTime localDateTime = localDate.atStartOfDay();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            c.setDate(timestamp);
        }

        c.setIdclient(cli.getValue());

        int ok = dao.create(c);
        if (ok != 0)
        {
            Notification.NotifSuccess("Success","Reussi");
            load();
        }
    }

    @FXML
    private TableView<Comptes> tab;

    @FXML
    void consulter(ActionEvent event) {
        try {
            Comptes selectedComptes = tab.getSelectionModel().getSelectedItem();
            if (selectedComptes != null) {
                if (selectedComptes.getIdclient() != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Détails du Compte");
                    alert.setHeaderText("Informations du Compte");
                    alert.setContentText("ID: " + selectedComptes.getId() +
                            "\nNuméro: " + selectedComptes.getNumero() +
                            "\nSolde: " + selectedComptes.getSolde() +
                            "\nDate: " + selectedComptes.getDate() +
                            "\nClient ID: " + selectedComptes.getIdclient().getId());
                    alert.showAndWait();
                } else {
                    Notification.NotifError("Erreur", "Le compte sélectionné n'a pas de client associé");
                }
            } else {
                Notification.NotifError("Erreur", "Veuillez sélectionner un compte à consulter");
            }
        } catch (NullPointerException e) {
            Notification.NotifError("Erreur", "Une erreur s'est produite : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Notification.NotifError("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void delete(ActionEvent event) {
        Comptes selectedComptes = tab.getSelectionModel().getSelectedItem();
        if (selectedComptes != null) {
            int ok = dao.closeAccount(selectedComptes.getId());
            if (ok == 1) {
                Notification.NotifSuccess("Succès", "Compte supprimé avec succès");
                load(); // Recharger la table après la suppression
            } else {
                Notification.NotifError("Erreur", "Échec de la suppression du compte");
            }
        } else {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à supprimer");
        }
    }


    @FXML
    void download(ActionEvent event) {
        Comptes selectedComptes = tab.getSelectionModel().getSelectedItem();
        if (selectedComptes != null) {
            byte[] pdfData = dao.generateBankStatement(selectedComptes.getId());
            if (pdfData.length > 0) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(tab.getScene().getWindow());
                if (file != null) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(pdfData);
                        Notification.NotifSuccess("Succès", "Relevé bancaire téléchargé avec succès");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Notification.NotifError("Erreur", "Échec du téléchargement du relevé bancaire");
                    }
                }
            } else {
                Notification.NotifError("Erreur", "Aucun relevé bancaire disponible pour ce compte");
            }
        } else {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à télécharger");
        }
    }

    @FXML
    void edit(ActionEvent event) {
        Comptes selectedComptes = tab.getSelectionModel().getSelectedItem();
        if (selectedComptes != null) {
            selectedComptes.setNumero(numTfd.getText());
            try {
                double solde = Double.parseDouble(soldeTfd.getText());
                // Logique pour modifier le compte
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Le solde doit être un nombre valide.");
            }

            // Convert LocalDate to Timestamp
            LocalDate localDate = date.getValue();
            if (localDate != null) {
                LocalDateTime localDateTime = localDate.atStartOfDay();
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                selectedComptes.setDate(timestamp);
            }

            selectedComptes.setIdclient(cli.getValue());

            int ok = dao.update(selectedComptes);
            if (ok != 0) {
                Notification.NotifSuccess("Success", "Compte mis à jour avec succès");
                load();
            } else {
                Notification.NotifError("Erreur", "Échec de la mise à jour du compte");
            }
        } else {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à modifier");
        }
    }

}
