package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CompteController implements Initializable {
    private Db db = new Db();

    @FXML
    private Button AJOUTERTfd;

    @FXML
    private Button AssocierCol;

    @FXML
    private TableColumn<?, ?> CLIENTTb1;

    @FXML
    private TextField ClientTfd;

    @FXML
    private Button ConsulterCol;
    @FXML
    private TableColumn<?, ?> CLIENTTb;

    @FXML
    private TableColumn<?, ?> IDTb;
    @FXML
    private TableColumn<?, ?> DATEOUVTb;

    @FXML
    private DatePicker DatedouvertureDtp;

    @FXML
    private Button FermerCol;
    @FXML
    private ComboBox<Client> ClientCb;
    @FXML
    private TableColumn<?, ?> NUMCPTTb;

    @FXML
    private TextField NumeroCompteTfd;

    @FXML
    private Text NumerodecompteTfd;

    @FXML
    private TableColumn<?, ?> SOLDETb;

    @FXML
    private TextField SoldeTfd;
    @FXML
    private TableView<Compte> CompteTb;

    @FXML
    public void ajouter(ActionEvent actionEvent) {
        String sql="INSERT INTO comptes(numero,balance,created_at,client_id) VALUES(?,?,?,?)";
        int selectedUser = ClientCb.getValue().getId();
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1,NumeroCompteTfd.getText());
            db.getPstm().setString(2,SoldeTfd.getText());
            db.getPstm().setString(3,DatedouvertureDtp.getValue().toString());
            db.getPstm().setInt(4,selectedUser);

            db.executeMaj();
            db.closeConnection();
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void loadComboBox() {
        if (ClientCb != null) {
            ObservableList<Client> clients = getClientsFromDatabase();
            ClientCb.setItems(clients);
        } else {
            System.err.println("Le ComboBox (ClientCb) est nul.");
        }
    }

    private ObservableList<Client> getClientsFromDatabase() {
        ObservableList<Client> clients = FXCollections.observableArrayList();

        String sql = "SELECT * FROM clients ";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setPrenom(rs.getString("prenom"));
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return clients;
    }

    public void loadTableCompte() {
        ObservableList<Compte> listeCompte = FXCollections.observableArrayList(getCompte());

        CompteTb.setItems(listeCompte);

        IDTb.setCellValueFactory(new PropertyValueFactory<>("id"));
        NUMCPTTb.setCellValueFactory(new PropertyValueFactory<>("numero"));
        SOLDETb.setCellValueFactory(new PropertyValueFactory<>("balance"));
        DATEOUVTb.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        CLIENTTb.setCellValueFactory(new PropertyValueFactory<>("client_id"));
    }

    public List<Compte> getCompte() {
        ObservableList<Compte> pars = FXCollections.observableArrayList();
        String sql = "SELECT c.id AS client_id, c.nom, c.prenom, c.email, co.id AS compte_id, co.numero, co.balance, co.created_at FROM clients c LEFT JOIN comptes co ON c.id = co.client_id";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Compte c = new Compte();
                c.setNumero(rs.getString("numero"));
                c.setBalance(rs.getDouble("balance"));
                c.setCreated_at(rs.getString("created_at"));
                c.setClient_id(rs.getInt("client_id"));
                c.setId(rs.getInt("compte_id"));
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
        loadComboBox();
        loadTableCompte();
    }
}
