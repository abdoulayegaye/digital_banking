package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private Db db = new Db();

    @FXML
    private Button AJOUTERTfd;

    @FXML
    private Button ConsulterBtn;

    @FXML
    private TextField EMAILTfd;
    @FXML
    private TableColumn<Client, String> EMAILCol;

    @FXML
    private TableColumn<Client, Integer> IDCol;

    @FXML
    private Button ModifierBtn;

    @FXML
    private TableColumn<Client, String> NOMCol;

    @FXML
    private TextField NOMTfd;

    @FXML
    private TableColumn<Client, String> PRENOMCol;

    @FXML
    private TextField PRENOMTfd;

    @FXML
    private Button SuprimerBtn;

    @FXML
    private VBox idNomTfd;

    @FXML
    private TableView<Client> ClientTb; // Ajout de la déclaration de la TableView

    @FXML
    public void ajouter(ActionEvent actionEvent) {
        String sql = "INSERT INTO clients(nom, prenom, email) VALUES (?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, NOMTfd.getText());
            db.getPstm().setString(2, PRENOMTfd.getText());
            db.getPstm().setString(3, EMAILTfd.getText());
            db.executeMaj();
            db.closeConnection();
            loadTableClient();
        } catch (SQLException e) {
            e.printStackTrace(); // Affiche l'erreur au lieu de la lancer en RuntimeException
        }
    }




    public void loadTableClient() {
        ObservableList<Client> listeClient = FXCollections.observableArrayList(getClient());

        ClientTb.setItems(listeClient);

        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        NOMCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        PRENOMCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        EMAILCol.setCellValueFactory(new PropertyValueFactory<>("email"));

    }


    public List<Client> getClient() {//selectionne la liste des clients pour chaque client connecte
        ObservableList<Client> pars =  FXCollections.observableArrayList();
        String sql = "SELECT *  FROM clients";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Client c=new Client();
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setEmail(rs.getString("email"));
                c.setId(rs.getInt("id"));
                pars.add(c);
            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return pars;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableClient();
    }
}
