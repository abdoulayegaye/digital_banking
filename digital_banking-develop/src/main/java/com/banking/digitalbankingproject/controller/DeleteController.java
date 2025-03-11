package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DeleteController {

    @FXML
    private TextField txtCompte;

    @FXML
    void clicksupp(ActionEvent event) {
ICompte compteDao = new CompteImpl();
String id = txtCompte.getText();
compteDao.supp(id);
txtCompte.clear();
    }

}
