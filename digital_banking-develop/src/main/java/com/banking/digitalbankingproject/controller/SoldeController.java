package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import com.banking.digitalbankingproject.entity.Compte;

import com.banking.digitalbankingproject.service.ICompte;

import java.awt.*;

public class SoldeController {

    @FXML
    private TextField txtCompte;


    @FXML
    private TextArea txtfield;


    @FXML
    void findclick(ActionEvent event) {
        ICompte compteDao= new CompteImpl();
        Compte compte = new Compte();
        Client client = new Client();
        String id = txtCompte.getText();
       compte =compteDao.getidsolde(id);
            //txtfield.setText("id :"+compte.getId());
        txtfield.setText("Numero : "+compte.getId()+"\nCompte : "+compte.getNumero()+"\nNom : "+
                compte.getCNom()+"\nSolde: "+ compte.getSolde() );
        txtfield.setEditable(false);



    }

}
