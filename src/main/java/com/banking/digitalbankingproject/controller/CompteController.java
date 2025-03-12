package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import javafx.application.Platform;
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
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import java.text.NumberFormat;
import java.util.Locale;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class CompteController implements Initializable {
    private final Db db = new Db();
    @FXML
    public TableColumn statutColumn;

    @FXML private TableView<Compte> compteTable;
    @FXML private TableColumn<Compte, Integer> idColumn;
    @FXML private TableColumn<Compte, String> numeroColumn;
    @FXML private TableColumn<Compte, Double> balanceColumn;
    @FXML private TableColumn<Compte, String> createdAtColumn;
    @FXML private TableColumn<Compte, String> clientColumn;
    @FXML private TableColumn<Compte, String> typeColumn;

    @FXML private TextField numeroField, balanceField;
    @FXML private ComboBox<Client> clientComboBox;
    @FXML private ComboBox<String> typeCompteComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadClients();
        loadTable();
        typeCompteComboBox.setItems(FXCollections.observableArrayList("Courant", "Épargne"));
    }

    private void loadClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients ORDER BY nom ASC";

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();

            while (rs.next()) {
                Client c = new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"));
                clients.add(c);
            }
            rs.close();

            clientComboBox.setItems(clients);

            clientComboBox.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Client client, boolean empty) {
                    super.updateItem(client, empty);
                    setText((empty || client == null) ? null : client.getEmail() + " " + client.getPrenom());
                }
            });

            clientComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Client client, boolean empty) {
                    super.updateItem(client, empty);
                    setText((empty || client == null) ? "Sélectionner un client" : client.getNom() + " " + client.getPrenom());
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les clients : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    public ObservableList<Compte> getComptes() {
        ObservableList<Compte> comptes = FXCollections.observableArrayList();
        String sql = "SELECT c.id, c.numero, c.balance, c.type, c.statut, c.created_at,\n" +
                "                    cl.id AS client_id, cl.nom AS client_nom, cl.prenom AS client_prenom, cl.email AS client_email " +
                "FROM comptes c\n" +
                "            INNER JOIN clients cl ON c.client_id = cl.id\n" +
                "            ORDER BY c.created_at DESC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                comptes.add(new Compte(
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getDouble("balance"),
                        rs.getString("type"),
                        rs.getString("statut"),
                        createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        new Client(
                                rs.getInt("client_id"),
                                rs.getString("client_nom"),
                                rs.getString("client_prenom"),
                                rs.getString("client_email")
                        )

                ));

            }
            rs.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les comptes : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return comptes;
    }

    public void loadTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        clientColumn.setCellValueFactory(cellData -> {
            Client client = cellData.getValue().getClient();
            return new SimpleStringProperty(client != null ? client.getEmail() + " " + client.getPrenom() : "Aucun client");
        });

        compteTable.setItems(getComptes());
    }

    @FXML
    void ajouterCompte(ActionEvent event) {
        String numero = numeroField.getText().trim();
        String balanceText = balanceField.getText().trim();
        Client client = clientComboBox.getValue();
        String typeCompte = typeCompteComboBox.getValue();
        if (numero.isEmpty() || balanceText.isEmpty() || client == null || typeCompte == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }
        if (clientHasCompte(client.getId())) {
            showNotification("Compte existant", "Ce client a déjà un compte", NotificationType.WARNING);
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceText);
            if (balance < 0) {
                showAlert(Alert.AlertType.WARNING, "Solde invalide", "Le solde ne peut pas être négatif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Veuillez entrer un solde valide.");
            return;
        }
        if (numeroExiste(numero)) {
            showAlert(Alert.AlertType.WARNING, "Numéro existant", "Ce numéro de compte existe déjà.");
            return;
        }
        String sql = "INSERT INTO comptes (numero, balance, type, client_id, created_at) VALUES (?, ?, ?, ?, NOW())";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numero);
            db.getPstm().setDouble(2, balance);
            db.getPstm().setString(3, typeCompte);
            db.getPstm().setInt(4, client.getId());

            int rowsInserted = db.executeMaj();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte ajouté avec succès.");
                loadTable(); // Rafraîchir la table
                clearFields(); // Nettoyer les champs après ajout
            } else {
                showAlert(Alert.AlertType.ERROR, "Échec", "L'ajout du compte a échoué.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le compte : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    private boolean numeroExiste(String numero) {
        String sql = "SELECT COUNT(*) FROM comptes WHERE numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numero);
            ResultSet rs = db.executeSelect();
            boolean exists = rs.next() && rs.getInt(1) > 0;
            rs.close();
            return exists;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de vérifier l'existence du compte : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return false;
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearFields() {
        numeroField.clear();
        balanceField.clear();
        clientComboBox.setValue(null);
        typeCompteComboBox.setValue(null);
    }



    public void consulterSolde(ActionEvent actionEvent) {
        Compte compteSelectionne = compteTable.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un compte pour consulter le solde.");
            return;
        }

        // Utilisation de NumberFormat pour afficher un format lisible
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        String soldeFormate = format.format(compteSelectionne.getBalance());

        showAlert(Alert.AlertType.INFORMATION, "Solde du compte",
                "Le solde du compte " + compteSelectionne.getNumero() + " est de " + soldeFormate + " €.");
    }

    @FXML
    void voirHistorique(ActionEvent event) {
        Compte compteSelectionne = compteTable.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun compte sélectionné", "Veuillez sélectionner un compte.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Historique.fxml"));
            Parent root = loader.load();
            HistoriqueController controller = loader.getController();
            controller.setCompteId(compteSelectionne.getId());
            Stage stage = new Stage();
            stage.setTitle("Historique des Transactions - Compte " + compteSelectionne.getNumero());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'historique : " + e.getMessage());
            e.printStackTrace(); // Affiche la stack trace pour le débogage
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void fermerCompte(ActionEvent event) {
        Compte compteSelectionne = compteTable.getSelectionModel().getSelectedItem();

        if (compteSelectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun compte sélectionné", "Veuillez sélectionner un compte.");
            return;
        }
        if ("inactif".equals(compteSelectionne.getStatut())) {
            showAlert(Alert.AlertType.WARNING, "Compte déjà inactif", "Ce compte est déjà inactif.");
            return;
        }

        // Mise à jour du statut du compte en base de données
        String sql = "UPDATE comptes SET statut = 'inactif' WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteSelectionne.getId());

            int rowsUpdated = db.executeMaj();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le compte a été fermé avec succès.");
                loadTable(); // Rafraîchir la table
            } else {
                showAlert(Alert.AlertType.ERROR, "Échec", "La fermeture du compte a échoué.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de fermer le compte : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }
    @FXML
    void ouvrirCompte(ActionEvent event) {
        Compte compteSelectionne = compteTable.getSelectionModel().getSelectedItem();

        if (compteSelectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun compte sélectionné", "Veuillez sélectionner un compte.");
            return;
        }

        // Vérifie si le compte est déjà actif
        if ("actif".equals(compteSelectionne.getStatut())) {
            showAlert(Alert.AlertType.WARNING, "Compte déjà actif", "Ce compte est déjà actif.");
            return;
        }

        // Mise à jour du statut du compte en base de données
        String sql = "UPDATE comptes SET statut = 'actif' WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteSelectionne.getId());

            int rowsUpdated = db.executeMaj();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le compte a été réactivé avec succès.");
                loadTable(); // Rafraîchir la table
            } else {
                showAlert(Alert.AlertType.ERROR, "Échec", "La réactivation du compte a échoué.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de réactiver le compte : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }
    private boolean clientHasCompte(int clientId) {
        String sql = "SELECT COUNT(*) FROM comptes WHERE client_id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, clientId);
            ResultSet rs = db.executeSelect();
            boolean exists = rs.next() && rs.getInt(1) > 0;
            rs.close();
            return exists;
        } catch (SQLException e) {
            showNotification("Erreur", "Impossible de vérifier les comptes existants", NotificationType.ERROR);
        } finally {
            db.closeConnection();
        }
        return false;
    }
    private void showNotification(String title, String message, NotificationType type) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndDismiss(javafx.util.Duration.seconds(3));
    }
    @FXML
    private Button retourAccueilButton, retourClientsButton;

    public void retourClients(ActionEvent actionEvent) {


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) retourAccueilButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'accueil !");
            }
        }

    public void retourAccueil(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clients.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) retourClientsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des clients !");
    }
}
}
