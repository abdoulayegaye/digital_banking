package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Depot;
import com.banking.digitalbankingproject.entity.Retrait;
import com.banking.digitalbankingproject.service.IDepot;
import com.banking.digitalbankingproject.service.IRetrait;
import com.banking.digitalbankingproject.service.impl.DepotImpl;
import com.banking.digitalbankingproject.service.impl.RetraitImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepotController {
private Depot depot = new Depot();
    private Retrait retrait = new Retrait();
private IDepot depotDao =new DepotImpl();
private RetraitImpl retraitDao =new RetraitImpl();
    @FXML
    private Label accountidfield ;

    @FXML
    private Label balancefield ;

    @FXML
    private Label nomfield;

    @FXML
    private Label prenomfield;

    @FXML
    private TextField txtdepot;

    @FXML
    private TextField txtid;


    @FXML
    void findclick(ActionEvent event) {

        Depot depot = new Depot();
        IDepot depotDao =new DepotImpl();
 String id = txtid.getText();
 depot = depotDao.getId(id);
        accountidfield.setText(String.valueOf(depot.getCompte().getNumero()));
        nomfield.setText(depot.getClient().getNom());
        prenomfield.setText( depot.getClient().getPrenom());
        balancefield.setText(String.valueOf(depot.getCompte().getSolde()));
    }

    @FXML
    void okclick(ActionEvent event) {
        IDepot depotDao =new DepotImpl();

        String id = txtid.getText();
        depot = depotDao.getId(id);

        depot.getCompte().setNumero(accountidfield.getText());
        depot.getCompte().setId(Integer.parseInt(txtid.getText()));
        depot.getCompte().setSolde(Double.parseDouble(txtdepot.getText()));
        depot.setDate(depotDao.date());
        depotDao.add(depot);
        txtid.setText("");
        accountidfield.setText("?");
        nomfield.setText("?");
        prenomfield.setText("?");
        balancefield.setText("?");
        txtdepot.setText(" ");


    }

    @FXML
    void retraitclick(ActionEvent event) {


        String id = txtid.getText();
        retrait = retraitDao.getId(id);
        retrait.getCompte().setNumero(accountidfield.getText());
        retrait.getCompte().setId(Integer.parseInt(txtid.getText()));
        retrait.getCompte().setSolde(Double.parseDouble(txtdepot.getText()));
        retrait.setDate(depotDao.date());
        retraitDao.add(retrait);
        txtid.setText("");
        accountidfield.setText("?");
        nomfield.setText("?");
        prenomfield.setText("?");
        balancefield.setText("?");
        txtdepot.setText(" ");

    }


}
