package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class CompteController implements Initializable {

    @FXML
    private Button btnAjouterCompte, btnModifierCompte, btnRefresh, btnSupprimerCompte;

    @FXML
    private TableColumn<Compte, String> colClient, colNumero;

    @FXML
    private TableColumn<Compte, Date> colDate;

    @FXML
    private TableColumn<Compte, Double> colSolde;

    @FXML
    private TextField soldeComptes, clientComptes, numeroComptes;

    @FXML
    private DatePicker dateComptes;

    @FXML
    private Button cleanComptes;

    @FXML
    private TableView<Compte> tableViewComptes;

    private final Db db = new Db(); // Connexion DB

    @FXML
    void allerAjouterCompte(ActionEvent event) {
        String sql = "INSERT INTO comptes (numero, balance, client_id, created_at) VALUES (?, ?, ?, ?)";
        try {
            db.initPrepar(sql);

            db.getPstm().setString(1, numeroComptes.getText());
            db.getPstm().setDouble(2, Double.parseDouble(soldeComptes.getText()));
            db.getPstm().setInt(3, Integer.parseInt(clientComptes.getText()));
            db.getPstm().setDate(4, java.sql.Date.valueOf(dateComptes.getValue()));

            db.executeMaj();
            db.closeConnection();
            loaderTable();
            clearFields();
            System.out.println("Compte ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erreur : Vérifiez les champs numériques !");
        }
    }

    @FXML
    void allerNumeroComptes(ActionEvent event) {

    }

    @FXML
    void allerClientComptes(ActionEvent event) {

    }


    @FXML
    void allerModifierCompte(ActionEvent event) {
        String sql = "UPDATE comptes SET numero = ?, balance = ?, created_at = ?, client_id = ? WHERE id = ?";

        // Vérifier qu'un compte est bien sélectionné
        Compte compteSelectionne = tableViewComptes.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            System.out.println("Aucun compte sélectionné !");
            return;
        }

        try {
            db.initPrepar(sql);

            // Affecter les nouvelles valeurs
            db.getPstm().setString(1, numeroComptes.getText());  // Numéro du compte
            db.getPstm().setDouble(2, Double.parseDouble(soldeComptes.getText()));  // Solde
            db.getPstm().setDate(3, java.sql.Date.valueOf(dateComptes.getValue())); // Date d'ouverture
            db.getPstm().setInt(4, Integer.parseInt(clientComptes.getText()));  // ID du client
            db.getPstm().setInt(5, compteSelectionne.getId());  // ID du compte pour WHERE

            // Exécuter la requête
            int rowsUpdated = db.executeMaj();
            db.closeConnection();

            if (rowsUpdated > 0) {
                System.out.println("Modification réussie !");
                loaderTable(); // Rafraîchir la table
                clearFields(); // Effacer les champs
                btnAjouterCompte.setDisable(true);
            } else {
                System.out.println("Aucune ligne modifiée, vérifie l'ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion : Vérifie les champs numériques !");
        }
    }

    @FXML
    void allezCleanComptes(ActionEvent event) {
        clearFields();
    }

    @FXML
    void allerSupprimerCompte(ActionEvent event) {
        String sql = "DELETE FROM comptes WHERE id = ?";

        // Vérifier qu'un compte est bien sélectionné
        Compte compteSelectionne = tableViewComptes.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            System.out.println("Aucun compte sélectionné !");
            return;
        }

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteSelectionne.getId());  // Passer l'ID du compte sélectionné

            int rowsUpdated = db.executeMaj();  // Exécuter la requête
            db.closeConnection();

            if (rowsUpdated > 0) {
                System.out.println("Suppression réussie !");
                loaderTable();  // Rafraîchir la table
                clearFields();  // Effacer les champs
                btnAjouterCompte.setDisable(false);  // Réactiver le bouton d'ajout
            } else {
                System.out.println("Aucune ligne supprimée, vérifie l'ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Afficher l'erreur pour le débogage
        }
    }


    @FXML
    void rafraichirComptes(ActionEvent event) {

        loaderTable();
    }

    @FXML
    void allerSoldeComptes(ActionEvent event) {
    }

    @FXML
    void allerDateComptes(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loaderTable();
    }

    public ObservableList<Compte> getComptes() {
        ObservableList<Compte> comptes = FXCollections.observableArrayList();
        String sql = "SELECT * FROM comptes ORDER BY numero ASC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setClient_id(rs.getInt("client_id"));
                compte.setCreated_at(rs.getDate("created_at"));
                comptes.add(compte);
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }
        return comptes;
    }

    public void loaderTable() {
        if (tableViewComptes == null) {
            System.out.println("ERREUR: tableViewComptes est NULL !");
            return;
        }

        System.out.println("Chargement des comptes dans la table...");
        ObservableList<Compte> liste = getComptes();

        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("created_at"));

        tableViewComptes.setItems(liste);
        System.out.println("Table des comptes chargée avec succès !");
    }


    void clearFields() {
        numeroComptes.clear();
        soldeComptes.clear();
        clientComptes.clear();
        dateComptes.setValue(null);
    }

    @FXML
    void getData(MouseEvent event) {
        // Vérifie si une ligne est bien sélectionnée
        Compte compteSelectionne = tableViewComptes.getSelectionModel().getSelectedItem();
        if (compteSelectionne != null) {
            // Remplissage des champs avec les données sélectionnées
            numeroComptes.setText(compteSelectionne.getNumero());
            clientComptes.setText(String.valueOf(compteSelectionne.getClient_id()));
            soldeComptes.setText(String.valueOf(compteSelectionne.getBalance()));
            dateComptes.setValue(LocalDate.parse(compteSelectionne.getCreated_at().toLocaleString()));
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }


}
