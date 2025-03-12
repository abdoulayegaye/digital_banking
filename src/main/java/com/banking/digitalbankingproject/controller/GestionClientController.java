package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class GestionClientController implements Initializable {

    private static final Logger logger = Logger.getLogger(GestionClientController.class.getName());

    @FXML
    private Button btnRetour;
    @FXML
    private Button btnNouveauClient;
    @FXML
    private Button btnModifierClient;
    @FXML
    private Button btnSupprimerClient;
    @FXML
    private Button btnRechercher;
    @FXML
    private Button btnEnregistrer;
    @FXML
    private Button btnAnnuler;
    @FXML
    private TextField txtRecherche;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableView<Client> tableClients;
    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colNom;
    @FXML
    private TableColumn<Client, String> colPrenom;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private VBox formContainer;

    private IClient clientService;
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();
    private boolean isEditMode = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientService = new ClientImpl();
        
        // Configuration des colonnes de la table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Chargement des clients
        loadClients();
        
        // Ajout d'un listener pour la sélection dans la table
        tableClients.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showClientDetails(newSelection);
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
                        "Aucun client n'a été trouvé dans la base de données. Veuillez en créer un nouveau.");
            } else {
                logger.info(clients.size() + " client(s) trouvé(s)");
            }
            
            clientsList.clear();
            clientsList.addAll(clients);
            tableClients.setItems(clientsList);
            tableClients.refresh();
        } catch (Exception e) {
            logger.severe("Erreur lors du chargement des clients: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des clients", e.getMessage());
        }
    }
    
    private void showClientDetails(Client client) {
        txtId.setText(String.valueOf(client.getId()));
        txtNom.setText(client.getNom());
        txtPrenom.setText(client.getPrenom());
        txtEmail.setText(client.getEmail());
    }
    
    private void clearForm() {
        txtId.clear();
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
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
    private void handleNouveauClient(ActionEvent event) {
        clearForm();
        isEditMode = false;
    }
    
    @FXML
    private void handleModifierClient(ActionEvent event) {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            showClientDetails(selectedClient);
            isEditMode = true;
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun client sélectionné", 
                    "Veuillez sélectionner un client dans la liste.");
        }
    }
    
    @FXML
    private void handleSupprimerClient(ActionEvent event) {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation de suppression");
            confirmDialog.setHeaderText("Supprimer le client");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce client ?");
            
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    clientService.deleteClient(selectedClient.getId());
                    loadClients();
                    clearForm();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client supprimé", 
                            "Le client a été supprimé avec succès.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression", e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun client sélectionné", 
                    "Veuillez sélectionner un client dans la liste.");
        }
    }
    
    @FXML
    private void handleRechercher(ActionEvent event) {
        String searchTerm = txtRecherche.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                List<Client> searchResults = clientService.searchClients(searchTerm);
                clientsList.clear();
                clientsList.addAll(searchResults);
                tableClients.setItems(clientsList);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recherche", e.getMessage());
            }
        } else {
            loadClients();
        }
    }
    
    @FXML
    private void handleEnregistrer(ActionEvent event) {
        if (validateForm()) {
            try {
                Client client = new Client();
                if (isEditMode && !txtId.getText().isEmpty()) {
                    client.setId(Integer.parseInt(txtId.getText()));
                }
                client.setNom(txtNom.getText());
                client.setPrenom(txtPrenom.getText());
                client.setEmail(txtEmail.getText());
                
                if (isEditMode) {
                    clientService.updateClient(client);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client mis à jour", 
                            "Le client a été mis à jour avec succès.");
                } else {
                    clientService.createClient(client);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client créé", 
                            "Le client a été créé avec succès.");
                }
                
                loadClients();
                clearForm();
            } catch (Exception e) {
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
        
        if (txtNom.getText().trim().isEmpty()) {
            errorMessage.append("Le nom ne peut pas être vide.\n");
        }
        
        if (txtPrenom.getText().trim().isEmpty()) {
            errorMessage.append("Le prénom ne peut pas être vide.\n");
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            errorMessage.append("L'email ne peut pas être vide.\n");
        } else if (!isValidEmail(txtEmail.getText().trim())) {
            errorMessage.append("Format d'email invalide.\n");
        }
        
        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", "Veuillez corriger les erreurs suivantes:", 
                    errorMessage.toString());
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        // Validation simple d'email
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}");
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Méthode pour ajouter un client
    @FXML
    private void ajouterClient() {
        // Vérifier que les champs sont valides
        if (validateForm()) {
            try {
                // Créer un nouvel objet Client avec les données du formulaire
                Client nouveauClient = new Client();
                nouveauClient.setNom(txtNom.getText().trim());
                nouveauClient.setPrenom(txtPrenom.getText().trim());
                nouveauClient.setEmail(txtEmail.getText().trim());
                
                // Appeler le service pour créer le client
                boolean resultat = clientService.createClient(nouveauClient);
                
                if (resultat) {
                    // Afficher un message de succès
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client ajouté", 
                            "Le client a été ajouté avec succès.");
                    
                    // Recharger la liste des clients
                    loadClients();
                    
                    // Réinitialiser le formulaire
                    clearForm();
                } else {
                    // Afficher un message d'erreur
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout", 
                            "Impossible d'ajouter le client. Veuillez vérifier les informations saisies.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout", 
                        "Une erreur est survenue : " + e.getMessage());
            }
        }
    }

    // Méthode pour modifier un client
    @FXML
    private void modifierClient() {
        // Vérifier qu'un client est sélectionné
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun client sélectionné", 
                    "Veuillez sélectionner un client dans la liste pour le modifier.");
            return;
        }
        
        // Vérifier que les champs sont valides
        if (validateForm()) {
            try {
                // Mettre à jour l'objet Client avec les données du formulaire
                selectedClient.setNom(txtNom.getText().trim());
                selectedClient.setPrenom(txtPrenom.getText().trim());
                selectedClient.setEmail(txtEmail.getText().trim());
                
                // Appeler le service pour mettre à jour le client
                boolean resultat = clientService.updateClient(selectedClient);
                
                if (resultat) {
                    // Afficher un message de succès
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client modifié", 
                            "Le client a été modifié avec succès.");
                    
                    // Recharger la liste des clients
                    loadClients();
                    
                    // Réinitialiser le formulaire
                    clearForm();
                } else {
                    // Afficher un message d'erreur
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification", 
                            "Impossible de modifier le client. Veuillez vérifier les informations saisies.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification", 
                        "Une erreur est survenue : " + e.getMessage());
            }
        }
    }

    // Méthode pour supprimer un client
    @FXML
    private void supprimerClient() {
        // Vérifier qu'un client est sélectionné
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun client sélectionné", 
                    "Veuillez sélectionner un client dans la liste pour le supprimer.");
            return;
        }
        
        // Demander confirmation avant de supprimer
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Supprimer le client");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer le client " + 
                selectedClient.getNom() + " " + selectedClient.getPrenom() + " ?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Appeler le service pour supprimer le client
                boolean resultat = clientService.deleteClient(selectedClient.getId());
                
                if (resultat) {
                    // Afficher un message de succès
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client supprimé", 
                            "Le client a été supprimé avec succès.");
                    
                    // Recharger la liste des clients
                    loadClients();
                    
                    // Réinitialiser le formulaire
                    clearForm();
                } else {
                    // Afficher un message d'erreur
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression", 
                            "Impossible de supprimer le client. Il est peut-être associé à des comptes.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression", 
                        "Une erreur est survenue : " + e.getMessage());
            }
        }
    }
}