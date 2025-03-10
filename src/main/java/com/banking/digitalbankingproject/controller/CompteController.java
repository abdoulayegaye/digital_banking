package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import com.banking.digitalbankingproject.tools.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;

public class CompteController implements Initializable {
    @FXML private TextField numeroTfd;
    @FXML private TextField soldeTfd;
    @FXML private ComboBox<Client> clientCombo;
    @FXML private TableView<Compte> compteTable;
    @FXML private TableColumn<Compte, String> numeroCol;
    @FXML private TableColumn<Compte, Double> soldeCol;
    @FXML private TableColumn<Compte, Instant> dateCol;
    @FXML private TableColumn<Compte, String> clientCol;
    
    private final ICompte compteDao = new CompteImpl();
    private final IClient clientDao = new ClientImpl();
    private final ObservableList<Compte> comptes = FXCollections.observableArrayList();
    private final ObservableList<Client> clients = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuration des colonnes
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        soldeCol.setCellValueFactory(new PropertyValueFactory<>("formattedSolde"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        
        numeroCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 10;");
        soldeCol.setStyle("-fx-alignment: CENTER-RIGHT; -fx-padding: 0 10;");
        dateCol.setStyle("-fx-alignment: CENTER; -fx-padding: 0 10;");
        clientCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 10;");
        
        compteTable.setStyle("-fx-font-size: 14px;");
        
        compteTable.setRowFactory(tv -> {
            TableRow<Compte> row = new TableRow<>();
            row.setStyle("-fx-background-color: transparent;");
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f0f0f0;");
                }
            });
            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });
            return row;
        });
        
        numeroTfd.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; " +
                          "-fx-border-color: #dee2e6; -fx-background-color: #f8f9fa; -fx-padding: 8;");
        soldeTfd.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; " +
                         "-fx-border-color: #dee2e6; -fx-background-color: white; -fx-padding: 8;");
        
        loadComptes();
        loadClients();
        numeroTfd.setText(Utils.generateAccountNumber());
    }
    @FXML
    void addCompte(ActionEvent event) {
        try {
            String numero = numeroTfd.getText().trim();
            String soldeStr = soldeTfd.getText().trim();
            Client client = clientCombo.getValue();
            
            if (numero.isEmpty() || soldeStr.isEmpty() || client == null) {
                Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
                return;
            }
            
            double solde;
            try {
                solde = Double.parseDouble(soldeStr);
                if (solde < 0) {
                    Notification.NotifError("Erreur", "Le solde initial doit être positif");
                    return;
                }
            } catch (NumberFormatException e) {
                Notification.NotifError("Erreur", "Le solde doit être un nombre valide");
                return;
            }
            
            Compte compte = new Compte();
            compte.setNumero(numero);
            compte.setSolde(solde);
            compte.setDateCreation(Instant.now());
            compte.setClient(client);
            
            int result = compteDao.save(compte);
            
            if (result > 0) {
                clearFields();
                loadComptes();
                Notification.NotifSuccess("Succès", "Compte créé avec succès");
            } else {
                Notification.NotifError("Erreur", "Échec de la création du compte");
            }
        } catch (Exception e) {
            Notification.NotifError("Erreur", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    @FXML
    void closeCompte(ActionEvent event) {
        Compte selectedCompte = compteTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte");
            return;
        }
        
        if (!Notification.NotifConfirm("Confirmation", "Voulez-vous vraiment fermer ce compte ?")) {
            return;
        }
        
        try {
            compteDao.delete(selectedCompte.getNumero());
            clearFields();
            loadComptes();
            Notification.NotifSuccess("Succès", "Compte fermé avec succès");
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    

    @FXML
    void goToAccueil(ActionEvent event) throws IOException {
        Outils.load(event, "Digital Banking", "/fxml/accueil.fxml");
    }

    private void loadComptes() {
        try {
            comptes.clear();
            comptes.addAll(compteDao.getAll());
            compteTable.setItems(comptes);
            compteTable.refresh();
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    private void loadClients() {
        try {
            clients.clear();
            clients.addAll(clientDao.getAll());
            clientCombo.setItems(clients);
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    private void clearFields() {
        numeroTfd.setText(Utils.generateAccountNumber());
        soldeTfd.clear();
        clientCombo.setValue(null);
    }
}
