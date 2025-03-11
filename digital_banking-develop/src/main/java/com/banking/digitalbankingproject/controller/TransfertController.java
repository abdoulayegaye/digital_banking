package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Depot;
import com.banking.digitalbankingproject.entity.Transfert;
import com.banking.digitalbankingproject.service.IDepot;
import com.banking.digitalbankingproject.service.ITransfert;
import com.banking.digitalbankingproject.service.impl.DepotImpl;
import com.banking.digitalbankingproject.service.impl.TransfertImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TransfertController {

    @FXML
    private TextField acctxt;

    @FXML
    private Label accid;

    @FXML
    private TextField balancetxt;

    @FXML
    private TextField idtxt;

    @FXML
    private TextField montanttxt;

    @FXML
    void clickfind(ActionEvent event) {
        Transfert transfert = new Transfert();
        ITransfert transfertDao =new TransfertImpl();
        String id = idtxt.getText();
        transfert = transfertDao.getId(id);
        balancetxt.setText(String.valueOf(transfert.getCompte().getSolde()));
        accid.setText(transfert.getCompte().getNumero());
    }

    @FXML
    void clicktransfer(ActionEvent event) {
        Transfert transfert = new Transfert();
        ITransfert transfertDao =new TransfertImpl();
        transfert.setCompte(new Compte());
        transfert.setClient(new Client());


        String id = idtxt.getText();

        transfert = transfertDao.getId(id);

        String nom =transfert.getClient().getNom();
        //double solde = transfert.getCompte().setSolde(Double.parseDouble(montanttxt.getText()));


        transfert.getCompte().setSolde(Double.parseDouble(montanttxt.getText()));
        transfert.getCompte().setNumero(accid.getText());
        transfertDao.senderMaj(transfert);



        transfert.getCompte().setSolde(Integer.parseInt(montanttxt.getText()));
        transfert.getCompte().setNumero(acctxt.getText());
        transfertDao.recieverMaj(transfert);


        transfert = transfertDao.getId(id);
        transfert.getCompte().setNumero(accid.getText());
        transfert.getClient().setNom(nom);
        transfert.getCompte().setSolde(Double.parseDouble(montanttxt.getText()));
        transfert.setDate(transfertDao.date());
        transfertDao.add(transfert);

        acctxt.setText(" ");
        balancetxt.setText(" ");
        idtxt.setText(" ");
        montanttxt.setText(" ");
        accid.setText(" ");



    }

}
