package com.example.projet_java_fx.controllers;

import com.example.projet_java_fx.controllers.AccueilController;

import com.example.projet_java_fx.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void loadCompte(ActionEvent event) {
        try {
            Outils.loadSub(event,"Gestion des Comptes","/pages/Compte.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadClient(ActionEvent event) {
        try{
            Outils.loadSub(event,"Gestion Clients","/pages/Client.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    void loadeOperation(ActionEvent event) {
        try{
            Outils.loadSub(event,"Gestion des Operations","/pages/Operation.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}

