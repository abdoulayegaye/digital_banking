package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.tools.genPDF;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class releverController {
    private static int ide =0;
    @FXML
    private TextField txtlab;
    IClient clientDao = new ClientImpl();
    ICompte compteDao = new CompteImpl();
    Client client = new Client();
    Compte compte = new Compte();
    @FXML
    void clickfind(ActionEvent event) {

        String id = txtlab.getText();
        clientDao.get(id);
        compteDao.get(id);



    }

    //generatePdf type autoid


    public int autoID(){
        ide ++;
        return ide;
    }


    @FXML
    void clickgen(ActionEvent event) {
        String id = txtlab.getText();
        client = clientDao.get(id);
        compte = compteDao.getidrev(id);
        int ide = autoID();
      genPDF.genpdf("output"+ide+".pdf",client,compte);
      ide++;
    }

}
