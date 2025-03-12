package com.example.digital_banking.controllers;

import com.example.digital_banking.entities.Client;
import com.example.digital_banking.entities.Compte;
import com.example.digital_banking.entities.Operation;
import com.example.digital_banking.services.BanqueService;
import com.example.digital_banking.services.PDFService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.File;
import javafx.stage.FileChooser;

public class MainController implements Initializable {
    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> prenomColumn;
    @FXML private TableColumn<Client, String> emailColumn;

    @FXML private TableView<Compte> comptesTable;
    @FXML private TableColumn<Compte, String> numeroCompteColumn;
    @FXML private TableColumn<Compte, Double> soldeColumn;
    @FXML private TableColumn<Compte, LocalDate> dateOuvertureColumn;
    @FXML private TableColumn<Compte, String> clientCompteColumn;

    @FXML private TableView<Operation> operationsTable;
    @FXML private TableColumn<Operation, LocalDate> dateOperationColumn;
    @FXML private TableColumn<Operation, String> typeOperationColumn;
    @FXML private TableColumn<Operation, Double> montantOperationColumn;
    @FXML private TableColumn<Operation, String> compteOperationColumn;

    private BanqueService banqueService = new BanqueService();
    private PDFService pdfService = new PDFService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuration des colonnes pour la table des clients
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientsTable.setItems(banqueService.getClients());

        // Configuration des colonnes pour la table des comptes
        numeroCompteColumn.setCellValueFactory(new PropertyValueFactory<>("numeroCompte"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        dateOuvertureColumn.setCellValueFactory(new PropertyValueFactory<>("dateOuverture"));
        clientCompteColumn.setCellValueFactory(cellData -> 
            cellData.getValue().getClient().nomProperty().concat(" ").concat(cellData.getValue().getClient().prenomProperty()));
        comptesTable.setItems(banqueService.getComptes());

        // Configuration des colonnes pour la table des opérations
        dateOperationColumn.setCellValueFactory(new PropertyValueFactory<>("dateOperation"));
        typeOperationColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        montantOperationColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        compteOperationColumn.setCellValueFactory(cellData -> 
            cellData.getValue().getCompte().numeroCompteProperty());
        operationsTable.setItems(banqueService.getOperations());
    }

    @FXML
    private void nouveauClient() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Client");
        dialog.setHeaderText("Créer un nouveau client");

        // Boutons
        ButtonType buttonTypeOk = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        // Création du formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || emailField.getText().isEmpty()) {
                    showAlert("Erreur", "Champs incomplets", "Tous les champs sont obligatoires.");
                    return null;
                }
                return new Client(nomField.getText(), prenomField.getText(), emailField.getText());
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(client -> {
            banqueService.ajouterClient(client);
            clientsTable.refresh();
        });
    }

    @FXML
    private void modifierClient() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Erreur", "Aucun client sélectionné", "Veuillez sélectionner un client à modifier.");
            return;
        }

        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Modifier Client");
        dialog.setHeaderText("Modifier les informations du client");

        ButtonType buttonTypeOk = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField nomField = new TextField(selectedClient.getNom());
        TextField prenomField = new TextField(selectedClient.getPrenom());
        TextField emailField = new TextField(selectedClient.getEmail());

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || emailField.getText().isEmpty()) {
                    showAlert("Erreur", "Champs incomplets", "Tous les champs sont obligatoires.");
                    return null;
                }
                selectedClient.setNom(nomField.getText());
                selectedClient.setPrenom(prenomField.getText());
                selectedClient.setEmail(emailField.getText());
                return selectedClient;
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(client -> {
            banqueService.modifierClient(client);
            clientsTable.refresh();
        });
    }

    @FXML
    private void supprimerClient() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Erreur", "Aucun client sélectionné", "Veuillez sélectionner un client à supprimer.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le client");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce client ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            banqueService.supprimerClient(selectedClient);
            clientsTable.refresh();
        }
    }

    @FXML
    private void nouveauCompte() {
        Dialog<Compte> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Compte");
        dialog.setHeaderText("Créer un nouveau compte");

        ButtonType buttonTypeOk = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField numeroCompteField = new TextField();
        numeroCompteField.setPromptText("Numéro de compte");
        TextField soldeInitialField = new TextField();
        soldeInitialField.setPromptText("Solde initial");
        ComboBox<Client> clientComboBox = new ComboBox<>(banqueService.getClients());
        clientComboBox.setPromptText("Sélectionner un client");

        grid.add(new Label("Numéro de compte:"), 0, 0);
        grid.add(numeroCompteField, 1, 0);
        grid.add(new Label("Solde initial:"), 0, 1);
        grid.add(soldeInitialField, 1, 1);
        grid.add(new Label("Client:"), 0, 2);
        grid.add(clientComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (numeroCompteField.getText().isEmpty() || soldeInitialField.getText().isEmpty() || clientComboBox.getValue() == null) {
                    showAlert("Erreur", "Champs incomplets", "Tous les champs sont obligatoires.");
                    return null;
                }
                try {
                    double soldeInitial = Double.parseDouble(soldeInitialField.getText());
                    return new Compte(numeroCompteField.getText(), soldeInitial, clientComboBox.getValue());
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Format invalide", "Le solde doit être un nombre valide.");
                    return null;
                }
            }
            return null;
        });

        Optional<Compte> result = dialog.showAndWait();
        result.ifPresent(compte -> {
            banqueService.creerCompte(compte);
            comptesTable.refresh();
        });
    }

    @FXML
    private void fermerCompte() {
        Compte selectedCompte = comptesTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showAlert("Erreur", "Aucun compte sélectionné", "Veuillez sélectionner un compte à fermer.");
            return;
        }
        // TODO: Implémenter la fermeture du compte
    }

    @FXML
    private void effectuerDepot() {
        Compte selectedCompte = comptesTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showAlert("Erreur", "Aucun compte sélectionné", "Veuillez sélectionner un compte pour effectuer le dépôt.");
            return;
        }

        Dialog<Double> dialog = new Dialog<>();
        dialog.setTitle("Effectuer un dépôt");
        dialog.setHeaderText("Dépôt sur le compte " + selectedCompte.getNumeroCompte());

        ButtonType buttonTypeOk = new ButtonType("Déposer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField montantField = new TextField();
        montantField.setPromptText("Montant");

        grid.add(new Label("Montant:"), 0, 0);
        grid.add(montantField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (montantField.getText().isEmpty()) {
                    showAlert("Erreur", "Champ vide", "Veuillez saisir un montant.");
                    return null;
                }
                try {
                    return Double.parseDouble(montantField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Format invalide", "Le montant doit être un nombre valide.");
                    return null;
                }
            }
            return null;
        });

        Optional<Double> result = dialog.showAndWait();
        result.ifPresent(montant -> {
            try {
                banqueService.effectuerDepot(selectedCompte, montant);
                comptesTable.refresh();
                operationsTable.refresh();
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "Opération impossible", e.getMessage());
            }
        });
    }

    @FXML
    private void effectuerRetrait() {
        Compte selectedCompte = comptesTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showAlert("Erreur", "Aucun compte sélectionné", "Veuillez sélectionner un compte pour effectuer le retrait.");
            return;
        }

        Dialog<Double> dialog = new Dialog<>();
        dialog.setTitle("Effectuer un retrait");
        dialog.setHeaderText("Retrait sur le compte " + selectedCompte.getNumeroCompte());

        ButtonType buttonTypeOk = new ButtonType("Retirer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField montantField = new TextField();
        montantField.setPromptText("Montant");

        grid.add(new Label("Montant:"), 0, 0);
        grid.add(montantField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (montantField.getText().isEmpty()) {
                    showAlert("Erreur", "Champ vide", "Veuillez saisir un montant.");
                    return null;
                }
                try {
                    return Double.parseDouble(montantField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Format invalide", "Le montant doit être un nombre valide.");
                    return null;
                }
            }
            return null;
        });

        Optional<Double> result = dialog.showAndWait();
        result.ifPresent(montant -> {
            try {
                banqueService.effectuerRetrait(selectedCompte, montant);
                comptesTable.refresh();
                operationsTable.refresh();
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "Opération impossible", e.getMessage());
            }
        });
    }

    @FXML
    private void effectuerVirement() {
        Compte compteSource = comptesTable.getSelectionModel().getSelectedItem();
        if (compteSource == null) {
            showAlert("Erreur", "Aucun compte sélectionné", "Veuillez sélectionner un compte source pour le virement.");
            return;
        }

        Dialog<Virement> dialog = new Dialog<>();
        dialog.setTitle("Effectuer un virement");
        dialog.setHeaderText("Virement depuis le compte " + compteSource.getNumeroCompte());

        ButtonType buttonTypeOk = new ButtonType("Virer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        ComboBox<Compte> compteDestinationComboBox = new ComboBox<>(banqueService.getComptes());
        compteDestinationComboBox.setPromptText("Sélectionner le compte destinataire");
        compteDestinationComboBox.getItems().remove(compteSource);

        TextField montantField = new TextField();
        montantField.setPromptText("Montant");

        grid.add(new Label("Compte destinataire:"), 0, 0);
        grid.add(compteDestinationComboBox, 1, 0);
        grid.add(new Label("Montant:"), 0, 1);
        grid.add(montantField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (montantField.getText().isEmpty() || compteDestinationComboBox.getValue() == null) {
                    showAlert("Erreur", "Champs incomplets", "Tous les champs sont obligatoires.");
                    return null;
                }
                try {
                    double montant = Double.parseDouble(montantField.getText());
                    return new Virement(compteDestinationComboBox.getValue(), montant);
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Format invalide", "Le montant doit être un nombre valide.");
                    return null;
                }
            }
            return null;
        });

        Optional<Virement> result = dialog.showAndWait();
        result.ifPresent(virement -> {
            try {
                banqueService.effectuerVirement(compteSource, virement.compteDestination, virement.montant);
                comptesTable.refresh();
                operationsTable.refresh();
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "Opération impossible", e.getMessage());
            }
        });
    }

    @FXML
    private void genererReleve() {
        Compte selectedCompte = comptesTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showAlert("Erreur", "Aucun compte sélectionné", "Veuillez sélectionner un compte pour générer le relevé.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le relevé");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        fileChooser.setInitialFileName("releve_" + selectedCompte.getNumeroCompte() + ".pdf");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                pdfService.genererReleve(selectedCompte, banqueService.getOperationsCompte(selectedCompte), file.getAbsolutePath());
                showInformation("Succès", "Relevé généré", "Le relevé a été généré avec succès.");
            } catch (Exception e) {
                showAlert("Erreur", "Génération impossible", "Impossible de générer le relevé : " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInformation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static class Virement {
        private final Compte compteDestination;
        private final double montant;

        public Virement(Compte compteDestination, double montant) {
            this.compteDestination = compteDestination;
            this.montant = montant;
        }
    }
} 