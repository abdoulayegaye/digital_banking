package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Depot;
import com.banking.digitalbankingproject.entity.Retrait;
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

public class HretraitController {

    private Db db = new Db();
    private ResultSet rs;
    private int ok;


    @FXML
    private TableColumn<Retrait, String> acc_id;

    @FXML
    private TableColumn<Retrait, Integer> cust_id;

    @FXML
    private TableColumn<Retrait, Double> solde;

    @FXML
    private TableColumn<Retrait, String> date;

    @FXML
    private TableColumn<Retrait, String> id;

    @FXML
    private TableView<Retrait> table;
    @FXML
    ObservableList<Retrait> retraitList = FXCollections.observableArrayList();
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


        String sql = "SELECT d.id,d.acc_id,c.id AS cust_id,d.balance,d.date from retrait d join customer c ON d.cust_id  = c.id where d.cust_id = ?";

        try {

            db.initPrepar(sql);
            db.getPstm().setString(1,id);
            rs = db.executeSelect();
            retraitList.clear();

            while (rs.next()) {
                Retrait retrait = new Retrait();
                retrait.setClient(new Client());
                retrait.setCompte(new Compte());

                retrait.setId(rs.getInt("id"));
                retrait.setAcc_id(rs.getString("acc_id"));
                retrait.setCust_id(rs.getInt("cust_id"));
                retrait.setSolde(rs.getDouble("balance"));
                retrait.setDate(Instant.parse(rs.getString("date")));
                retraitList.add(retrait);
            }

            // Ajouter les données à la TableView
            table.setItems(retraitList);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void loadAll() {
        String sql = "SELECT * from retrait";

        try {

            db.initPrepar(sql);
            rs = db.executeSelect();
            retraitList.clear();

            while (rs.next()) {
                Retrait retrait = new Retrait();
                retrait.setClient(new Client());
                retrait.setCompte(new Compte());

                retrait.setId(rs.getInt("id"));
                retrait.getCompte().setNumero(rs.getString("acc_id"));
                retrait.getClient().setId(rs.getInt("cust_id"));
                retrait.setSolde(rs.getDouble("balance"));
                retrait.setDate(Instant.parse(rs.getString("date")));
                retraitList.add(retrait);
            }

            // Ajouter les données à la TableView
            table.setItems(retraitList);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void clickalldepot(ActionEvent event) {
        loadAll();
    }

    @FXML
    void clickfind(ActionEvent event) {

        String id = txtlab.getText();
        loadClients(id);
    }


}