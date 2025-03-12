package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
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
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class GestionOperationController implements Initializable {

    private static final Logger logger = Logger.getLogger(GestionOperationController.class.getName());
    private static final double FRANC = 6.50; // Taux de conversion fixe Euro -> Franc

    // Composants communs
    @FXML
    private Button btnRetour;

    // Composants pour Dépôt/Retrait
    @FXML
    private ComboBox<Client> comboClientsDepotRetrait;
    @FXML
    private ComboBox<Compte> comboComptesDepotRetrait;
    @FXML
    private RadioButton radioDepot;
    @FXML
    private RadioButton radioRetrait;
    @FXML
    private ToggleGroup typeOperation;
    @FXML
    private TextField txtMontantDepotRetrait;
    @FXML
    private Label lblSoldeActuelDepotRetrait;
    @FXML
    private Button btnEffectuerDepotRetrait;
    @FXML
    private Button btnAnnulerDepotRetrait;

    // Composants pour Virement
    @FXML
    private ComboBox<Client> comboClientSource;
    @FXML
    private ComboBox<Compte> comboCompteSource;
    @FXML
    private Label lblSoldeSource;
    @FXML
    private ComboBox<Client> comboClientDestination;
    @FXML
    private ComboBox<Compte> comboCompteDestination;
    @FXML
    private TextField txtMontantVirement;
    @FXML
    private Button btnEffectuerVirement;
    @FXML
    private Button btnAnnulerVirement;

    // Composants pour Historique
    @FXML
    private ComboBox<Client> comboClientsHistorique;
    @FXML
    private ComboBox<Compte> comboComptesHistorique;
    @FXML
    private Button btnAfficherHistorique;
    @FXML
    private Button btnGenererPDF;
    @FXML
    private TableView<Operation> tableOperations;
    @FXML
    private TableColumn<Operation, Integer> colIdOperation;
    @FXML
    private TableColumn<Operation, Integer> colCompteId;
    @FXML
    private TableColumn<Operation, Double> colMontant;
    @FXML
    private TableColumn<Operation, String> colTypeOperation;
    @FXML
    private TableColumn<Operation, Date> colDate;

    // Services
    private IClient clientService;
    private ICompte compteService;
    private IOperation operationService;

    // Données
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();
    private ObservableList<Compte> comptesDepotRetraitList = FXCollections.observableArrayList();
    private ObservableList<Compte> comptesSourceList = FXCollections.observableArrayList();
    private ObservableList<Compte> comptesDestinationList = FXCollections.observableArrayList();
    private ObservableList<Compte> comptesHistoriqueList = FXCollections.observableArrayList();
    private ObservableList<Operation> operationsList = FXCollections.observableArrayList();
    private Map<Integer, Client> clientsMap = new HashMap<>();
    private Map<Integer, Compte> comptesMap = new HashMap<>();
    private NumberFormat currencyFormat;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des services
        clientService = new ClientImpl();
        compteService = new CompteImpl();
        operationService = new OperationImpl();
        
        // Configuration du format monétaire pour les francs
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        currencyFormat.setCurrency(Currency.getInstance("FRF"));
        
        // Afficher un message d'information sur la conversion en francs
        showAlert(Alert.AlertType.INFORMATION, "Information", "Affichage en Francs", 
                "Les montants  affichés en Francs ().");

        // Configuration des convertisseurs pour les ComboBox
        setupComboBoxConverters();

        // Configuration de la table des opérations
        setupOperationsTable();

        // Chargement des données
        loadClients();

        // Configuration des listeners pour les ComboBox
        setupComboBoxListeners();

        // Initialisation des formulaires
        clearDepotRetraitForm();
        clearVirementForm();
        clearHistoriqueForm();
    }

    private void setupComboBoxConverters() {
        // Convertisseur pour les clients
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

        // Convertisseur pour les comptes
        StringConverter<Compte> compteConverter = new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                if (compte == null) return "";
                Client client = clientsMap.get(compte.getClientId());
                String clientName = client != null ? client.getNom() + " " + client.getPrenom() : "Client inconnu";
                // Conversion du solde en francs
                double soldeEnFrancs = compte.getSolde() * FRANC;
                return "Compte " + compte.getId() + " - Solde: " + currencyFormat.format(soldeEnFrancs);
            }

            @Override
            public Compte fromString(String string) {
                return null; // Non utilisé pour la conversion inverse
            }
        };

        // Application des convertisseurs aux ComboBox
        comboClientsDepotRetrait.setConverter(clientConverter);
        comboComptesDepotRetrait.setConverter(compteConverter);
        comboClientSource.setConverter(clientConverter);
        comboCompteSource.setConverter(compteConverter);
        comboClientDestination.setConverter(clientConverter);
        comboCompteDestination.setConverter(compteConverter);
        comboClientsHistorique.setConverter(clientConverter);
        comboComptesHistorique.setConverter(compteConverter);
    }

    private void setupOperationsTable() {
        colIdOperation.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCompteId.setCellValueFactory(new PropertyValueFactory<>("compteId"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colTypeOperation.setCellValueFactory(new PropertyValueFactory<>("typeOperation"));
        
        // Formattage de la colonne montant pour afficher en francs
        colMontant.setCellFactory(tc -> new TableCell<Operation, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                if (empty || montant == null) {
                    setText(null);
                } else {
                    // Conversion du montant en francs
                    double montantEnFrancs = montant * FRANC;
                    setText(currencyFormat.format(montantEnFrancs));
                }
            }
        });
    }

    private void setupComboBoxListeners() {
        // Listener pour le choix du client dans l'onglet Dépôt/Retrait
        comboClientsDepotRetrait.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadComptesForClient(newVal.getId(), comptesDepotRetraitList, comboComptesDepotRetrait);
            } else {
                comptesDepotRetraitList.clear();
                comboComptesDepotRetrait.setValue(null);
            }
        });

        // Listener pour le choix du compte dans l'onglet Dépôt/Retrait
        comboComptesDepotRetrait.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Conversion du solde en francs
                double soldeEnFrancs = newVal.getSolde() *FRANC;
                lblSoldeActuelDepotRetrait.setText(currencyFormat.format(soldeEnFrancs));
            } else {
                lblSoldeActuelDepotRetrait.setText("0.00 F");
            }
        });

        // Listener pour le choix du client source dans l'onglet Virement
        comboClientSource.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadComptesForClient(newVal.getId(), comptesSourceList, comboCompteSource);
            } else {
                comptesSourceList.clear();
                comboCompteSource.setValue(null);
            }
        });

        // Listener pour le choix du compte source dans l'onglet Virement
        comboCompteSource.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Conversion du solde en francs
                double soldeEnFrancs = newVal.getSolde() * FRANC;
                lblSoldeSource.setText(currencyFormat.format(soldeEnFrancs));
            } else {
                lblSoldeSource.setText("0.00 F");
            }
        });

        // Listener pour le choix du client destination dans l'onglet Virement
        comboClientDestination.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadComptesForClient(newVal.getId(), comptesDestinationList, comboCompteDestination);
            } else {
                comptesDestinationList.clear();
                comboCompteDestination.setValue(null);
            }
        });

        // Listener pour le choix du client dans l'onglet Historique
        comboClientsHistorique.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadComptesForClient(newVal.getId(), comptesHistoriqueList, comboComptesHistorique);
            } else {
                comptesHistoriqueList.clear();
                comboComptesHistorique.setValue(null);
            }
        });
    }

    private void loadClients() {
        try {
            List<Client> clients = clientService.getAllClients();
            clientsList.clear();
            clientsMap.clear();
            
            for (Client client : clients) {
                clientsList.add(client);
                clientsMap.put(client.getId(), client);
            }
            
            comboClientsDepotRetrait.setItems(clientsList);
            comboClientSource.setItems(clientsList);
            comboClientDestination.setItems(clientsList);
            comboClientsHistorique.setItems(clientsList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des clients", e.getMessage());
        }
    }

    private void loadComptesForClient(int clientId, ObservableList<Compte> comptesList, ComboBox<Compte> comboBox) {
        try {
            List<Compte> comptes = compteService.getComptesByClientId(clientId);
            comptesList.clear();
            
            for (Compte compte : comptes) {
                comptesList.add(compte);
                comptesMap.put(compte.getId(), compte);
            }
            
            comboBox.setItems(comptesList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des comptes", e.getMessage());
        }
    }

    private void loadOperationsForCompte(int compteId) {
        try {
            List<Operation> operations = operationService.getOperationsByCompteId(compteId);
            operationsList.clear();
            operationsList.addAll(operations);
            tableOperations.setItems(operationsList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des opérations", e.getMessage());
        }
    }

    private void clearDepotRetraitForm() {
        comboClientsDepotRetrait.setValue(null);
        comboComptesDepotRetrait.setValue(null);
        radioDepot.setSelected(true);
        txtMontantDepotRetrait.clear();
        lblSoldeActuelDepotRetrait.setText("0.00 F");
    }

    private void clearVirementForm() {
        comboClientSource.setValue(null);
        comboCompteSource.setValue(null);
        comboClientDestination.setValue(null);
        comboCompteDestination.setValue(null);
        txtMontantVirement.clear();
        lblSoldeSource.setText("0.00 F");
    }

    private void clearHistoriqueForm() {
        comboClientsHistorique.setValue(null);
        comboComptesHistorique.setValue(null);
        operationsList.clear();
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
    private void handleEffectuerDepotRetrait(ActionEvent event) {
        if (validateDepotRetraitForm()) {
            try {
                Compte compte = comboComptesDepotRetrait.getValue();
                // Conversion du montant de francs en euros
                double montantEnFrancs = Double.parseDouble(txtMontantDepotRetrait.getText().trim());
                double montantEnEuros = montantEnFrancs / FRANC;
                boolean isDepot = radioDepot.isSelected();
                
                logger.info("Opération: " + (isDepot ? "Dépôt" : "Retrait") + 
                           ", Montant en francs: " + montantEnFrancs + " F" + 
                           ", Montant en euros: " + montantEnEuros + " €");
                
                if (isDepot) {
                    operationService.effectuerDepot(compte.getId(), montantEnEuros);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Dépôt effectué", 
                            "Le dépôt de " + currencyFormat.format(montantEnFrancs) + " a été effectué avec succès.");
                } else {
                    // Vérification du solde en francs
                    double soldeEnFrancs = compte.getSolde() * FRANC;
                    if (montantEnFrancs > soldeEnFrancs) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Solde insuffisant", 
                                "Le solde du compte est insuffisant pour effectuer ce retrait.");
                        return;
                    }
                    
                    operationService.effectuerRetrait(compte.getId(), montantEnEuros);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Retrait effectué", 
                            "Le retrait de " + currencyFormat.format(montantEnFrancs) + " a été effectué avec succès.");
                }
                
                // Mise à jour du compte
                Compte updatedCompte = compteService.getCompteById(compte.getId());
                comboComptesDepotRetrait.setValue(updatedCompte);
                
                // Réinitialisation du formulaire
                txtMontantDepotRetrait.clear();
            } catch (Exception e) {
                logger.severe("Erreur lors de l'opération: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'opération", e.getMessage());
            }
        }
    }

    @FXML
    private void handleAnnulerDepotRetrait(ActionEvent event) {
        clearDepotRetraitForm();
    }

    @FXML
    private void handleEffectuerVirement(ActionEvent event) {
        if (validateVirementForm()) {
            try {
                Compte compteSource = comboCompteSource.getValue();
                Compte compteDestination = comboCompteDestination.getValue();
                // Conversion du montant de francs en euros
                double montantEnFrancs = Double.parseDouble(txtMontantVirement.getText().trim());
                double montantEnEuros = montantEnFrancs / FRANC;
                
                logger.info("Virement: Compte source: " + compteSource.getId() + 
                           ", Compte destination: " + compteDestination.getId() + 
                           ", Montant en francs: " + montantEnFrancs + " F" + 
                           ", Montant en euros: " + montantEnEuros + " €");
                
                // Vérification du solde en francs
                double soldeEnFrancs = compteSource.getSolde() * FRANC;
                if (montantEnFrancs > soldeEnFrancs) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Solde insuffisant", 
                            "Le solde du compte source est insuffisant pour effectuer ce virement.");
                    return;
                }
                
                operationService.effectuerVirement(compteSource.getId(), compteDestination.getId(), montantEnEuros);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Virement effectué", 
                        "Le virement de " + currencyFormat.format(montantEnFrancs) + " a été effectué avec succès.");
                
                // Mise à jour des comptes
                Compte updatedCompteSource = compteService.getCompteById(compteSource.getId());
                comboCompteSource.setValue(updatedCompteSource);
                
                // Réinitialisation du formulaire
                txtMontantVirement.clear();
            } catch (Exception e) {
                logger.severe("Erreur lors du virement: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du virement", e.getMessage());
            }
        }
    }

    @FXML
    private void handleAnnulerVirement(ActionEvent event) {
        clearVirementForm();
    }

    @FXML
    private void handleAfficherHistorique(ActionEvent event) {
        Compte compte = comboComptesHistorique.getValue();
        if (compte != null) {
            loadOperationsForCompte(compte.getId());
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun compte sélectionné", 
                    "Veuillez sélectionner un compte pour afficher son historique.");
        }
    }

    @FXML
    private void handleGenererPDF(ActionEvent event) {
        Compte compte = comboComptesHistorique.getValue();
        if (compte != null) {
            try {
                operationService.genererReleve(compte.getId());
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Relevé généré", 
                        "Le relevé bancaire a été généré avec succès.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération du relevé", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun compte sélectionné", 
                    "Veuillez sélectionner un compte pour générer son relevé.");
        }
    }

    private boolean validateDepotRetraitForm() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (comboClientsDepotRetrait.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un client.\n");
        }
        
        if (comboComptesDepotRetrait.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un compte.\n");
        }
        
        if (txtMontantDepotRetrait.getText().trim().isEmpty()) {
            errorMessage.append("Le montant ne peut pas être vide.\n");
        } else {
            try {
                double montantEnFrancs = Double.parseDouble(txtMontantDepotRetrait.getText().trim());
                if (montantEnFrancs <= 0) {
                    errorMessage.append("Le montant en francs doit être supérieur à zéro.\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Le montant doit être un nombre valide.\n");
            }
        }
        
        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", "Veuillez corriger les erreurs suivantes:", 
                    errorMessage.toString());
            return false;
        }
        
        return true;
    }

    private boolean validateVirementForm() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (comboClientSource.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un client source.\n");
        }
        
        if (comboCompteSource.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un compte source.\n");
        }
        
        if (comboClientDestination.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un client destination.\n");
        }
        
        if (comboCompteDestination.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un compte destination.\n");
        }
        
        if (comboCompteSource.getValue() != null && comboCompteDestination.getValue() != null) {
            if (comboCompteSource.getValue().getId() == comboCompteDestination.getValue().getId()) {
                errorMessage.append("Les comptes source et destination ne peuvent pas être identiques.\n");
            }
        }
        
        if (txtMontantVirement.getText().trim().isEmpty()) {
            errorMessage.append("Le montant ne peut pas être vide.\n");
        } else {
            try {
                double montantEnFrancs = Double.parseDouble(txtMontantVirement.getText().trim());
                if (montantEnFrancs <= 0) {
                    errorMessage.append("Le montant en francs doit être supérieur à zéro.\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Le montant doit être un nombre valide.\n");
            }
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