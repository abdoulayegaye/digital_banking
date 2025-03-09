package org.example.javafx.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.javafx.entities.Client;
import org.example.javafx.entities.Compte;
import org.example.javafx.service.IClient;
import org.example.javafx.service.ICompte;
import org.example.javafx.service.impl.ClientImpl;
import org.example.javafx.service.impl.CompteImpl;
import org.example.javafx.tools.Notification;
import org.example.javafx.tools.Outils;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CreerCompte implements Initializable {
    private LocalDate localDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idcolum.setCellValueFactory(new PropertyValueFactory<>("id"));
        numerocolum.setCellValueFactory(new PropertyValueFactory<>("numero"));
        soldecolum.setCellValueFactory(new PropertyValueFactory<>("solde"));
        datecolum.setCellValueFactory(new PropertyValueFactory<>("date_ouverture"));
        clientcolum.setCellValueFactory(cellData -> {
            // Récupérer l'objet Client du Compte
            Client client = cellData.getValue().getClient();
            // Retourner l'ID du client comme valeur observable
            return new SimpleIntegerProperty(client != null ? client.getId() : 0).asObject();
        });
        //clientcolum.setCellValueFactory(new PropertyValueFactory<>("client"));
        loadCompte();
    }

    private IClient dao = new ClientImpl();
    private ICompte DA0= new CompteImpl();
    private int ok;
    @FXML
    private Button genererTfd;
    @FXML
    private Button consulterTfd;
    @FXML
    private Button annulertfd;

    @FXML
    private TableColumn<Compte, Integer> clientcolum;

    @FXML
    private ComboBox<Client> clienttfd;

    @FXML
    private TableColumn<?, ?> datecolum;

    @FXML
    private DatePicker datetfd;

    @FXML
    private TableColumn<Compte, Integer> idcolum;

    @FXML
    private TableColumn<Compte, String> numerocolum;

    @FXML
    private TextField numerotfd;

    @FXML
    private TableColumn<Compte, Double> soldecolum;

    @FXML
    private TextField soldetfd;
    @FXML
    private Button fermertfd;

    @FXML
    private TableView<Compte> tablecompte;

    @FXML
    private Button validertfd;

    @FXML
    void insert(ActionEvent event) {
        if (numerotfd.getText().isEmpty() || soldetfd.getText().isEmpty() || datetfd.getValue() == null || clienttfd.getValue() == null) {
            Notification.NotifError("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }
        Compte compte = new Compte();
        // Récupérer les données saisies
        compte.setNumero(numerotfd.getText());
        compte.setSolde(Double.parseDouble(soldetfd.getText()));
        LocalDate localDate = datetfd.getValue();
        Timestamp dateOuverture = Timestamp.valueOf(localDate.atStartOfDay());
        compte.setDate_ouverture(dateOuverture);
        Client client = clienttfd.getValue();
        compte.setClient(client);
        ok=DA0.create(compte);
        if (ok != 0) {
            Notification.NotifSuccess("Succès", "Compte créé avec succès");
            loadCompte();
            reset(null);
        } else {
            Notification.NotifError("Erreur", "Échec de la création du compte");
        }
    }
    public void loadCompte(){
        ObservableList<Compte> comptes = FXCollections.observableArrayList();
        List<Compte> compteList = DA0.getAllComptes(); // À implémenter dans votre DAO
        comptes.addAll(compteList);
        tablecompte.setItems(comptes);

        ObservableList<Client> clients = FXCollections.observableArrayList();
        List<Client> clientList = dao.getAllClients();
        clients.addAll(clientList);
        clienttfd.setItems(clients);
        clienttfd.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " " + client.getPrenom());
                }
            }
        });
        clienttfd.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " " + client.getPrenom()); // Afficher nom et prénom
                }
            }
        });
    }
    @FXML
    void reset(ActionEvent event) {
        numerotfd.clear();
        soldetfd.clear();
        datetfd.setValue(null);
        clienttfd.setValue(null);
    }
    @FXML
    void Fermer(ActionEvent event) throws IOException {
        Outils.loadSub(event,"Fermeture de comment","/fxml/FermetureCompte.fxml");
        loadCompte();
    }
    @FXML
    void consulterSolde(ActionEvent event) throws IOException {
        Outils.loadSub(event,"Fermeture de comment","/fxml/consulter_solde.fxml");
    }
    @FXML
    void genererPDF(ActionEvent event) throws IOException {
        Outils.loadSub(event,"Fermeture de comment","/fxml/generer_pdf.fxml");
    }

}

