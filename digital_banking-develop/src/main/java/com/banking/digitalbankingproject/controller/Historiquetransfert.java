package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Depot;
import com.banking.digitalbankingproject.entity.Transfert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;
import java.time.Instant;

public class Historiquetransfert {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @FXML
    private TableColumn<Transfert, String> Date;

    @FXML
    private TableColumn<Transfert, String> F_account;

    @FXML
    private TableColumn<Transfert, Integer> Solde;

    @FXML
    private TableColumn<Transfert, String> T_account;

    @FXML
    private TableColumn<Transfert, Integer> id;

    @FXML
    private TableView<Transfert> table;

    @FXML
    ObservableList<Transfert> tranfertList = FXCollections.observableArrayList();

    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        F_account.setCellValueFactory(new PropertyValueFactory<>("F_account"));
        T_account.setCellValueFactory(new PropertyValueFactory<>("T_account"));
        Solde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        Date.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    void loadtable(){

        String sql = "SELECT * FROM transfer";
        try{
            db.initPrepar(sql);
            rs=db.executeSelect();
            while(rs.next()){
                Transfert transfert =new Transfert();
                transfert.setCompte(new Compte());
                transfert.setClient(new Client());

                transfert.setId(rs.getInt("id"));
                transfert.getCompte().setNumero(rs.getString("F_account"));
                transfert.getClient().setNom(rs.getString("T_account"));
                transfert.getCompte().setSolde(rs.getDouble("balance"));
                transfert.setDate(Instant.parse(rs.getString("date")));
                tranfertList.add(transfert);
            }
            table.setItems(tranfertList);
        }

        catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    @FXML
    void clicklister(ActionEvent event) {
        tranfertList.clear();
loadtable();
    }

}
