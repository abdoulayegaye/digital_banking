package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private TableColumn<Client, String> colEmailClients;

    @FXML
    private TableColumn<Client, String> colNomClients;

    @FXML
    private TableColumn<Client, String> colPrenomClients;

    @FXML
    private Button EffacerClients;

    @FXML
    private Button creationClients;

    @FXML
    private TextField emailClients;

    @FXML
    private Button modificationClients;

    @FXML
    private TextField nomClients;

    @FXML
    private TextField prenomClients;

    @FXML
    private Button supprimerClients;

    private int idClient;

    @FXML
    private TableView<Client> tableClient;

    private Db DBConnexion = new Db(); // Connexion à la BD


    @FXML
    void getEffacerClients(ActionEvent event) {
        clearchamp();
    }

    @FXML
    void getcreationClients(ActionEvent event) {
        String sql = ("INSERT INTO clients VALUES (NULL, ?, ?, ?)");
        try {
            DBConnexion.initPrepar(sql);
            //Passage de valeurs
            DBConnexion.getPstm().setString(1, nomClients.getText());
            DBConnexion.getPstm().setString(2, prenomClients.getText());
            DBConnexion.getPstm().setString(3, emailClients.getText());
            DBConnexion.executeMaj();
            DBConnexion.closeConnection();
            loadTable();
            clearchamp();
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    //C'est pour effacer les champs aprés la création d'1 nouveau client
    void clearchamp(){
        nomClients.setText("");
        prenomClients.setText("");
        emailClients.setText("");
    }

    @FXML
    void getdata(MouseEvent event) {
        Client serve = tableClient.getSelectionModel().getSelectedItem();
        idClient = serve.getId();
        nomClients.setText(serve.getNom());
        prenomClients.setText(serve.getPrenom());
        emailClients.setText(serve.getEmail());
        creationClients.setDisable(true);
    }


    @FXML
    void getemailClients(ActionEvent event) {
        // Code à ajouter ici
    }

    @FXML
    void getmodificationClients(ActionEvent event) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            DBConnexion.initPrepar(sql);
            DBConnexion.getPstm().setString(1, nomClients.getText());
            DBConnexion.getPstm().setString(2, prenomClients.getText());
            DBConnexion.getPstm().setString(3, emailClients.getText());
            DBConnexion.getPstm().setInt(4, idClient);  // Correction de l'index

            int rowsUpdated = DBConnexion.executeMaj();  // Exécuter la requête
            DBConnexion.closeConnection();

            if (rowsUpdated > 0) {
                System.out.println("Modification réussie !");
                loadTable();  // Rafraîchir la table
                clearchamp(); // Effacer les champs
                creationClients.setDisable(false);
            } else {
                System.out.println("Aucune ligne modifiée, vérifie l'ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Afficher l'erreur pour le débogage
        }
    }


    @FXML
    void getnomClients(ActionEvent event) {
        // Code à ajouter ici
    }

    @FXML
    void getprenomClients(ActionEvent event) {
        // Code à ajouter ici
    }

    @FXML
    void getsupprimerClients(ActionEvent event) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            DBConnexion.initPrepar(sql);
            System.out.println("ID du client sélectionné : " + idClient);

            DBConnexion.getPstm().setInt(1, idClient);  // Correction de l'index

            int rowsUpdated = DBConnexion.executeMaj();  // Exécuter la requête
            DBConnexion.closeConnection();

            if (rowsUpdated > 0) {
                System.out.println("Suppression réussie !");
                loadTable();  // Rafraîchir la table
                clearchamp(); // Effacer les champs
                creationClients.setDisable(false);
            } else {
                System.out.println("Aucune ligne supprimée, vérifie l'ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Afficher l'erreur pour le débogage
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialisation du contrôleur...");
        loadTable();
    }

    public ObservableList<Client> getClient() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients ORDER BY nom ASC";

        try {
            System.out.println("Connexion à la base de données...");
            Db.initPrepar(sql);
            ResultSet rs = Db.executeSelect();
            System.out.println("Requête exécutée avec succès, récupération des données...");

            while (rs.next()) {
                Client serv = new Client();
                serv.setId(rs.getInt("id")); // Récupération de l'ID
                serv.setNom(rs.getString("nom"));
                serv.setPrenom(rs.getString("prenom"));
                serv.setEmail(rs.getString("email"));
                clients.add(serv);
            }


            DBConnexion.closeConnection();
            System.out.println("Connexion fermée, clients chargés avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans getClient(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur SQL dans getClient(): " + e.getMessage(), e);
        }
        return clients;
    }

    public void loadTable() {
        if (tableClient == null) {
            System.out.println("ERREUR: tableClient est NULL !");
            return;
        }

        System.out.println("Chargement des clients dans la table...");
        ObservableList<Client> liste = getClient();

        colNomClients.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenomClients.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmailClients.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableClient.setItems(liste);
        System.out.println("Table des clients chargée avec succès !");
    }
}
