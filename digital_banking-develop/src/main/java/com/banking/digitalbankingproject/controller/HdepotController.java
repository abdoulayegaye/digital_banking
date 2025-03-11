package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Depot;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;

import javafx.scene.control.TextField;

import java.awt.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class HdepotController {

    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private IClient clientDao = new ClientImpl();

    @FXML
    private TableColumn<Depot, String> acc_id;

    @FXML
    private TableColumn<Depot, Integer> cust_id;

    @FXML
    private TableColumn<Depot, Double> solde;

    @FXML
    private TableColumn<Depot, String> date;

    @FXML
    private TableColumn<Depot, String> id;

    @FXML
    private TableView<Depot> table;
    @FXML
    ObservableList<Depot> depotList = FXCollections.observableArrayList();
    @FXML
    private TextField txtlab;

    public void initialize() {

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        acc_id.setCellValueFactory(new PropertyValueFactory<>("acc_id"));
        cust_id.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        solde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));


       // loadClients();
    }

    public void loadClients(String id) {


        String sql = "SELECT d.*,c.id AS cust_id from depot d join customer c ON d.cust_id  = c.id where d.cust_id = ?";

        try {

            db.initPrepar(sql);
            db.getPstm().setString(1,id);
            rs = db.executeSelect();
            depotList.clear();

            while (rs.next()) {
                Depot depot = new Depot();
                depot.setClient(new Client());
                depot.setCompte(new Compte());

                depot.setId(rs.getInt("id"));
                depot.setAcc_id(rs.getString("acc_id"));
                depot.setCust_id(rs.getInt("cust_id"));
                depot.setSolde(rs.getDouble("balance"));
                depot.setDate(Instant.parse(rs.getString("date")));
                depotList.add(depot);
            }

            // Ajouter les donnees à la TableView
            table.setItems(depotList);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void loadAll() {
        String sql = "SELECT * from depot";

        try {

            db.initPrepar(sql);
            rs = db.executeSelect();
            depotList.clear();

            while (rs.next()) {
                Depot depot = new Depot();
                depot.setClient(new Client());
                depot.setCompte(new Compte());

                depot.setId(rs.getInt("id"));
                depot.getCompte().setNumero(rs.getString("acc_id"));
                depot.getClient().setId(rs.getInt("cust_id"));
                depot.setSolde(rs.getDouble("balance"));
                depot.setDate(Instant.parse(rs.getString("date")));
                depotList.add(depot);
            }

            // Ajouter les données à la TableView
            table.setItems(depotList);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void clickalldepot(ActionEvent event) {
        depotList.clear();
loadAll();
    }

    @FXML
    void clickfind(ActionEvent event) {

        String id = txtlab.getText();
        loadClients(id);
    }


}