package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
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

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private TextField EmailTFD;

    @FXML
    private TextField NomTFD;

    @FXML
    private TextField PrenomTFD;

    @FXML
    private Button ajouterBtn;

    @FXML
    private Button consulterBtn;

    @FXML
    private Button modifierBtn;

    @FXML
    private Button supprimerBtn;

    private int idClient;

    @FXML
    private TableView<Client> clientTb;

    @FXML
    private TableColumn<Client, String> emailCol;

    @FXML
    private TableColumn<Client, Integer> idCol;

    @FXML
    private TableColumn<Client, String> nomCol;

    @FXML
    private TableColumn<Client, String> prenomCol;

    @FXML
    void add(ActionEvent event) {
        String sql = "INSERT INTO clients VALUES(Null,?,?,?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, NomTFD.getText());
            db.getPstm().setString(2, PrenomTFD.getText());
            db.getPstm().setString(3, EmailTFD.getText());
            int ok = db.executeMaj();
            db.closeConnection();
            loadTable();
            clearField();
            Notification.NotifSuccess("Succés","Client bien ajouter");
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @FXML
    void cons(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, idClient);
            int ok = db.executeMaj();
            db.closeConnection();
            loadTable();
            clearField();
            ajouterBtn.setDisable(false);
            Notification.NotifSuccess("Succés","Client bien supprimer");
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @FXML
    void edit(ActionEvent event) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, NomTFD.getText());
            db.getPstm().setString(2, PrenomTFD.getText());
            db.getPstm().setString(3, EmailTFD.getText());
            db.getPstm().setInt(4, idClient);
            int ok = db.executeMaj();
            db.closeConnection();
            loadTable();
            clearField();
            ajouterBtn.setDisable(false);
            Notification.NotifSuccess("Succés","Client bien modifier");
        }catch (SQLException e){
            throw new RuntimeException();
        }

    }

    void clearField(){
        NomTFD.setText("");
        PrenomTFD.setText("");
        EmailTFD.setText("");
    }

    @FXML
    void getData(MouseEvent event) {
        Client clients = clientTb.getSelectionModel().getSelectedItem();
        idClient = clients.getId();
        NomTFD.setText(clients.getNom());
        PrenomTFD.setText(clients.getPrenom());
        EmailTFD.setText(clients.getEmail());
        ajouterBtn.setDisable(true);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }
    private Db db = new Db();

    public ObservableList<Client> getClient() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String sql ="SELECT * FROM clients ORDER BY nom ASC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
            db.closeConnection();
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return clients;
    }
    public void loadTable(){
        ObservableList<Client> liste = getClient();
        clientTb.setItems(liste);
        idCol.setCellValueFactory(new PropertyValueFactory<Client,Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Client,String>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Client,String>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Client,String>("email"));
    }
}
