package com.example.projet_java_fx.controllers;

import com.example.projet_java_fx.database.Db;
import com.example.projet_java_fx.entity.Clients;
import com.example.projet_java_fx.service.IClient;
import com.example.projet_java_fx.service.impl.ClientImpl;
import com.example.projet_java_fx.tools.Notification;
import com.example.projet_java_fx.tools.Outils;
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

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        serverTb.getStylesheets().add(getClass().getResource("/css/client.css").toExternalForm());
    }
    public void load(){
        ObservableList<Clients> clients = FXCollections.observableArrayList();
        List<Clients> clientsList = dao.getAllClients();
        for (Clients c: clientsList ){
            clients.add(c);
        }
        serverTb .setItems(clients);
    }

    private Db db = new Db();
    private IClient dao= new ClientImpl();
    private int ok;

    public ObservableList<Clients> getClients() {
        ObservableList<Clients> clients = FXCollections.observableArrayList();
        String sql = "select * from clients order by nom";
        try
        {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()){
                Clients client = new Clients();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return clients;
    }

    //    public void loadTable(){
//        ObservableList<Clients> liste = getClients();
//        serverTb.setItems(liste);
//        idCol.setCellValueFactory(new PropertyValueFactory<Clients,Integer>("id"));
//        nomCol.setCellValueFactory(new PropertyValueFactory<Clients,String>("nom"));
//        prenomCol.setCellValueFactory(new PropertyValueFactory<Clients,String>("prenom"));
//        emailCol.setCellValueFactory(new PropertyValueFactory<Clients,String>("email "));
//    }
    public void loadTable() {
        ObservableList<Clients> liste = getClients();
        serverTb.setItems(liste);

        // Configurer les colonnes
//        idCol.setCellValueFactory(new PropertyValueFactory<Clients, Integer>("id"));
//        nomCol.setCellValueFactory(new PropertyValueFactory<Clients, String>("nom"));
//        prenomCol.setCellValueFactory(new PropertyValueFactory<Clients, String>("prenom"));
//        emailCol.setCellValueFactory(new PropertyValueFactory<Clients, String>("email"));

        // Appliquer des styles CSS
        serverTb.getStyleClass().add("table-view");
        idCol.getStyleClass().add("table-column");
        nomCol.getStyleClass().add("table-column");
        prenomCol.getStyleClass().add("table-column");
        emailCol.getStyleClass().add("table-column");
    }

    @FXML
    private Button ajoutBtn;

    @FXML
    private Button annuleBtn;

    @FXML
    private TableColumn<Clients, String> emailCol;

    @FXML
    private TextField emailTfd;

    @FXML
    private TableColumn<Clients, Integer> idCol;

    @FXML
    private Button modifieBtn;

    @FXML
    private TableColumn<Clients, String> nomCol;

    @FXML
    private TextField nomTfd;

    @FXML
    private TableColumn<Clients, String> prenomCol;

    @FXML
    private TextField prenomTfd;

    @FXML
    private TableView<Clients> serverTb;

    @FXML
    private Button supprimeBtn;

    @FXML
    void delete(ActionEvent event) {
        Clients selectedClient = serverTb.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            int ok = dao.deleteClient(selectedClient.getId());
            if (ok == 1) {
                Notification.NotifSuccess("Succès", "Client supprimé avec succès");
                loadTable(); // Recharger la table après la suppression
            } else {
                Notification.NotifError("Erreur", "Échec de la suppression du client");
            }
        } else {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à supprimer");
        }
    }

    @FXML
    void edit(ActionEvent event) {
        Clients selectedClient = serverTb.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            // Remplir les champs de texte avec les informations du client sélectionné
            nomTfd.setText(selectedClient.getNom());
            prenomTfd.setText(selectedClient.getPrenom());
            emailTfd.setText(selectedClient.getEmail());

            // Changer le texte du bouton "Ajouter" en "Modifier"
            ajoutBtn.setText("Modifier");

            // Stocker l'ID du client sélectionné pour la mise à jour
            ok = selectedClient.getId();
        } else {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à modifier");
        }
    }

    @FXML
    void save(ActionEvent event) {
        Clients client = new Clients();
        client.setNom(nomTfd.getText());
        client.setPrenom(prenomTfd.getText());
        client.setEmail(emailTfd.getText());

        if (ajoutBtn.getText().equals("Modifier")) {
            // Mettre à jour le client existant
            client.setId(ok);
            int result = dao.updateClient(client);
            if (result == 1) {
                Notification.NotifSuccess("Succès", "Client modifié avec succès");
                loadTable(); // Recharger la table après la modification
                ajoutBtn.setText("Ajouter"); // Revenir au mode "Ajouter"
            } else {
                Notification.NotifError("Erreur", "Échec de la modification du client");
            }
        } else {
            // Ajouter un nouveau client
            int result = dao.createClient(client);
            if (result == 1) {
                Notification.NotifSuccess("Succès", "Client ajouté avec succès");
                loadTable(); // Recharger la table après l'ajout
            } else {
                Notification.NotifError("Erreur", "Échec de l'ajout du client");
            }
        }

        // Réinitialiser les champs de texte
        nomTfd.clear();
        prenomTfd.clear();
        emailTfd.clear();
    }

}