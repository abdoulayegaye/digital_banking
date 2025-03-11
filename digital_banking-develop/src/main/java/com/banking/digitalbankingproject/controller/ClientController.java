/*package com.banking.digitalbankingproject.controller;


import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;


import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;

import javafx.scene.control.TableColumn;

import javafx.scene.control.cell.PropertyValueFactory;



public class ClientController {

    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private IClient clientDao = new ClientImpl();
    @FXML
    private TableColumn<Client, String> idColumn;



    @FXML
    private TableColumn<Client, String> emailColumn;
    @FXML
    private TextField emailtxt;

    @FXML
    private TextField idtxt;

    @FXML
    private TableColumn<Client, String> nomColumn;

    @FXML
    private TextField nomtxt;

    @FXML
    private TableColumn<Client, String> prenomColumn;
    @FXML
    private TextField prenomtxt;

    @FXML
    private TableView<Client> table;

    @FXML
    ObservableList<Client> clientList = FXCollections.observableArrayList();

    public void initialize() {
        // Lier les colonnes aux propriétés de l'objet Client
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Charger les données depuis la base de données
        loadClients();
    }


    private void loadClients() {


        String sql = "SELECT * FROM customer";

        try {


            db.initPrepa(sql);
            rs = db.executeSelect();
            clientList.clear();

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clientList.add(client);
            }

            // Ajouter les données à la TableView
            table.setItems(clientList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void clickajouter(ActionEvent event) {

        Client client = new Client();
        String id = idtxt.getText();
        String nom = nomtxt.getText();
        String prenom = prenomtxt.getText();
        String email = emailtxt.getText();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        clientDao.Add(client);
        loadClients();


    }

    @FXML
    void clickdelete(ActionEvent event) {

    }

    @FXML
    void clicksearch(ActionEvent event) {

    }

    @FXML
    void clickupdate(ActionEvent event) {

    }
}*/

package com.banking.digitalbankingproject.controller;


import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;



import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;

import javafx.scene.control.TableColumn;

import javafx.scene.control.cell.PropertyValueFactory;

import static java.lang.Integer.parseInt;


public class ClientController {


    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private IClient clientDao = new ClientImpl();







    @FXML
    private TableColumn<Client, String> columnid;

    @FXML
    private TableColumn<Client, String> emailid;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField idtxt;

    @FXML
    private TableColumn<Client, String> nomid;

    @FXML
    private TextField nomtxt;

    @FXML
    private TableColumn<Client, String> prenomid;

    @FXML
    private TextField prenomtxt;

    @FXML
    private TableView<Client> table;

    @FXML
    ObservableList<Client> clientList = FXCollections.observableArrayList();

    public void initialize() {
        // Lier les colonnes aux propriétés de l'objet Client
        columnid.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomid.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomid.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailid.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Charger les données depuis la base de données
        loadClients();
    }

    public void loadClients() {


        String sql = "SELECT * FROM customer";

        try {


            db.initPrepa(sql);
            rs = db.executeSelect();
            clientList.clear();

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clientList.add(client);
            }

            // Ajouter les données à la TableView
            table.setItems(clientList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void clickajouter(ActionEvent event) {

        Client client = new Client();
        String nom = nomtxt.getText();
        String prenom = prenomtxt.getText();
        String email = emailtxt.getText();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        clientDao.Add(client);
        loadClients();

    }

    @FXML
    void clickdelete(ActionEvent event) {
        Client client = new Client();
        String id =idtxt.getText();

        clientDao.Delete(id);
        loadClients();
    }

    @FXML
    void clicksearch(ActionEvent event) {
        Client client = new Client();
        String id = idtxt.getText();
         client =clientDao.get(id);
         nomtxt.setText(client.getNom());
         prenomtxt.setText(client.getPrenom());
         emailtxt.setText(client.getEmail());
    }

    @FXML
    void clickupdate(ActionEvent event) {
        Client client = new Client();
int id = parseInt(idtxt.getText());
String nom = nomtxt.getText();
String prenom = prenomtxt.getText();
String email = emailtxt.getText();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setId(id);
        clientDao.Update(client);
        clientList.clear();
        loadClients();
    }



    }


