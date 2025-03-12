package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class GestionCompteController implements Initializable {

    @FXML
    private Button btnRetour;
    @FXML
    private Button btnNouveauCompte;
    @FXML
    private Button btnFermerCompte;
    @FXML
    private Button btnFiltrer;
    @FXML
    private Button btnTous;
    @FXML
    private Button btnEnregistrer;
    @FXML
    private Button btnAnnuler;
    @FXML
    private ComboBox<Client> comboClients;
    @FXML
    private ComboBox<Client> comboClientSelection;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtSolde;
    @FXML
    private TableView<Compte> tableComptes;
    @FXML
    private TableColumn<Compte, Integer> colId;
    @FXML
    private TableColumn<Compte, Double> colSolde;
    @FXML
    private TableColumn<Compte, Integer> colClientId;
    @FXML
    private TableColumn<Compte, String> colClientNom;
    @FXML
    private VBox formContainer;

    private ICompte compteService;
    private IClient clientService;
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();
    private Map<Integer, Client> clientsMap = new HashMap<>();
    private boolean isEditMode = false;
    private NumberFormat currencyFormat;
    private static final Logger logger = Logger.getLogger(GestionCompteController.class.getName());
    private static final double TAUX_CONVERSION_EURO_FRANC = 6.55957; // Taux de conversion fixe Euro -> Franc

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        compteService = new CompteImpl();
        clientService = new ClientImpl();
        
        // Configuration du format monétaire pour les francs
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        currencyFormat.setCurrency(Currency.getInstance("FRF"));
        
        // Afficher un message d'information sur la conversion en francs
        showAlert(Alert.AlertType.INFORMATION, "Information", "Affichage en Francs",
                "");

        // Configuration des colonnes de la table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        colClientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        
        // Formattage de la colonne solde pour afficher en francs
        colSolde.setCellFactory(tc -> new TableCell<Compte, Double>() {
            @Override
            protected void updateItem(Double solde, boolean empty) {
                super.updateItem(solde, empty);
                if (empty || solde == null) {
                    setText(null);
                } else {
                    // Conversion d'euros en francs
                    double soldeEnFrancs = solde * TAUX_CONVERSION_EURO_FRANC;
                    setText(currencyFormat.format(soldeEnFrancs));
                }
            }
        });
        
        // Configuration de la colonne nom du client
        colClientNom.setCellValueFactory(cellData -> {
            int clientId = cellData.getValue().getClientId();
            Client client = clientsMap.get(clientId);
            if (client != null) {
                return new SimpleStringProperty(client.getNom() + " " + client.getPrenom());
            } else {
                return new SimpleStringProperty("Client inconnu");
            }
        });
        
        // Configuration des ComboBox pour les clients
        StringConverter<Client> clientConverter = new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                if (client == null) return "";
                return client.getId() + " - " + client.getNom() + " " + client.getPrenom();
            }

            @Override
            public Client fromString(String string) {
                return null; // Non utilisé pour la conversion inverse
            }
        };
        
        comboClients.setConverter(clientConverter);
        comboClientSelection.setConverter(clientConverter);
        
        // Chargement des clients et des comptes
        loadClients();
        loadComptes();
        
        // Ajout d'un listener pour la sélection dans la table
        tableComptes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showCompteDetails(newSelection);
            }
        });
        
        // Initialisation du formulaire
        clearForm();
    }
    
    private void loadClients() {
        try {
            logger.info("Chargement des clients...");
            List<Client> clients = clientService.getAllClients();
            
            if (clients == null || clients.isEmpty()) {
                logger.warning("Aucun client trouvé dans la base de données");
                showAlert(Alert.AlertType.INFORMATION, "Information", "Aucun client", 
                        "Aucun client n'a été trouvé dans la base de données. Veuillez d'abord créer un client.");
                return;
            } else {
                logger.info(clients.size() + " client(s) trouvé(s)");
            }
            
            clientsList.clear();
            clientsMap.clear();
            
            for (Client client : clients) {
                clientsList.add(client);
                clientsMap.put(client.getId(), client);
            }
            
            comboClients.setItems(clientsList);
            comboClientSelection.setItems(clientsList);
            
            // Sélectionner le premier client par défaut s'il y en a
            if (!clientsList.isEmpty()) {
                comboClients.getSelectionModel().selectFirst();
                comboClientSelection.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            logger.severe("Erreur lors du chargement des clients: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des clients", e.getMessage());
        }
    }
    
    private void loadComptes() {
        try {
            logger.info("Chargement des comptes...");
            List<Compte> comptes = compteService.getAllComptes();
            
            if (comptes == null || comptes.isEmpty()) {
                logger.warning("Aucun compte trouvé dans la base de données");
                showAlert(Alert.AlertType.INFORMATION, "Information", "Aucun compte", 
                        "Aucun compte n'a été trouvé dans la base de données. Veuillez en créer un nouveau.");
            } else {
                logger.info(comptes.size() + " compte(s) trouvé(s)");
            }
            
            comptesList.clear();
            comptesList.addAll(comptes);
            tableComptes.setItems(comptesList);
            tableComptes.refresh();
        } catch (Exception e) {
            logger.severe("Erreur lors du chargement des comptes: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des comptes", e.getMessage());
        }
    }
    
    private void showCompteDetails(Compte compte) {
        txtId.setText(String.valueOf(compte.getId()));
        
        // Conversion du solde en francs pour l'affichage
        double soldeEnFrancs = compte.getSolde() * TAUX_CONVERSION_EURO_FRANC;
        txtSolde.setText(String.format("%.2f", soldeEnFrancs));
        
        Client client = clientsMap.get(compte.getClientId());
        if (client != null) {
            comboClientSelection.setValue(client);
        } else {
            comboClientSelection.setValue(null);
        }
        
        isEditMode = true;
    }
    
    private void clearForm() {
        txtId.clear();
        txtSolde.setText("0.0");
        comboClientSelection.setValue(null);
        isEditMode = false;
    }
    
    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/accueil.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setTitle("Accueil");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation", e.getMessage());
        }
    }
    
    @FXML
    private void handleNouveauCompte(ActionEvent event) {
        // Vérifier s'il y a des clients avant de créer un compte
        if (clientsList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun client disponible", 
                    "Vous devez d'abord créer un client avant de pouvoir créer un compte.");
            return;
        }
        
        clearForm();
        isEditMode = false;
        
        // Sélectionner le premier client par défaut
        if (!clientsList.isEmpty()) {
            comboClientSelection.getSelectionModel().selectFirst();
        }
    }
    
    @FXML
    private void handleFermerCompte(ActionEvent event) {
        Compte selectedCompte = tableComptes.getSelectionModel().getSelectedItem();
        if (selectedCompte != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation de fermeture");
            confirmDialog.setHeaderText("Fermer le compte");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir fermer ce compte ? Cette action est irréversible.");
            
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    compteService.deleteCompte(selectedCompte.getId());
                    loadComptes();
                    clearForm();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte fermé", 
                            "Le compte a été fermé avec succès.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la fermeture du compte", e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun compte sélectionné", 
                    "Veuillez sélectionner un compte dans la liste.");
        }
    }
    
    @FXML
    private void handleFiltrer(ActionEvent event) {
        Client selectedClient = comboClients.getValue();
        if (selectedClient != null) {
            try {
                List<Compte> filteredComptes = compteService.getComptesByClientId(selectedClient.getId());
                comptesList.clear();
                comptesList.addAll(filteredComptes);
                tableComptes.setItems(comptesList);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du filtrage", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun client sélectionné", 
                    "Veuillez sélectionner un client pour filtrer les comptes.");
        }
    }
    
    @FXML
    private void handleAfficherTous(ActionEvent event) {
        loadComptes();
        comboClients.setValue(null);
    }
    
    @FXML
    private void handleEnregistrer(ActionEvent event) {
        if (validateForm()) {
            try {
                Compte compte = new Compte();
                if (isEditMode && !txtId.getText().isEmpty()) {
                    compte.setId(Integer.parseInt(txtId.getText()));
                    logger.info("Mise à jour du compte ID: " + compte.getId());
                } else {
                    logger.info("Création d'un nouveau compte");
                }
                
                // Conversion du solde de francs en euros pour le stockage
                double soldeEnFrancs = Double.parseDouble(txtSolde.getText().trim());
                double soldeEnEuros = soldeEnFrancs / TAUX_CONVERSION_EURO_FRANC;
                compte.setSolde(soldeEnEuros);
                logger.info("Solde en francs: " + soldeEnFrancs + " F, converti en euros: " + soldeEnEuros + " €");
                
                Client selectedClient = comboClientSelection.getValue();
                if (selectedClient != null) {
                    compte.setClientId(selectedClient.getId());
                    logger.info("Client sélectionné: " + selectedClient.getNom() + " " + selectedClient.getPrenom() + " (ID: " + selectedClient.getId() + ")");
                } else {
                    logger.warning("Aucun client sélectionné");
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Client non sélectionné", 
                            "Veuillez sélectionner un client pour ce compte.");
                    return;
                }
                
                boolean success = false;
                if (isEditMode) {
                    success = compteService.updateCompte(compte);
                    if (success) {
                        logger.info("Compte mis à jour avec succès: " + compte.getId());
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte mis à jour", 
                                "Le compte a été mis à jour avec succès.");
                    } else {
                        logger.warning("Échec de la mise à jour du compte: " + compte.getId());
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour", 
                                "La mise à jour du compte a échoué. Veuillez réessayer.");
                        return;
                    }
                } else {
                    success = compteService.createCompte(compte);
                    if (success) {
                        logger.info("Compte créé avec succès");
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé", 
                                "Le compte a été créé avec succès.");
                    } else {
                        logger.warning("Échec de la création du compte");
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la création", 
                                "La création du compte a échoué. Veuillez réessayer.");
                        return;
                    }
                }
                
                loadComptes();
                clearForm();
            } catch (NumberFormatException e) {
                logger.severe("Erreur de format de nombre: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format invalide", 
                        "Le solde doit être un nombre valide.");
            } catch (Exception e) {
                logger.severe("Erreur lors de l'enregistrement du compte: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'enregistrement", e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAnnuler(ActionEvent event) {
        clearForm();
    }
    
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (txtSolde.getText().trim().isEmpty()) {
            errorMessage.append("Le solde ne peut pas être vide.\n");
        } else {
            try {
                double soldeEnFrancs = Double.parseDouble(txtSolde.getText().trim());
                if (soldeEnFrancs < 0 && !isEditMode) {
                    errorMessage.append("Le solde initial en francs ne peut pas être négatif.\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Le solde doit être un nombre valide.\n");
            }
        }
        
        if (comboClientSelection.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un client pour ce compte.\n");
        }
        
        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", "Veuillez corriger les erreurs suivantes:", 
                    errorMessage.toString());
            return false;
        }
        
        return true;
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 