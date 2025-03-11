package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;

public class CompteController {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private ICompte compteDao = new CompteImpl();

    @FXML
    public Label label9;

public void autoid(){
    String sql = "select max(acc_id) from account";
    try{

        db.initPrepa(sql);
        rs = db.executeSelect();
        rs.next();
        String ok=rs.getString("max(acc_id)");
      if (ok==null)
      {
        label9.setText("AC1");
      }
      else{

          long id = Long.parseLong(ok.substring(2));
          id++;
          label9.setText("AC"+id);
      }

    }

    catch (Exception e) {
        throw new RuntimeException(e);
    }

    }


    @FXML
    private AnchorPane jpane;

    @FXML
    private TextField soldetxt;

    @FXML
    private TextField useridtxt;

    @FXML
    void clickajouter(ActionEvent event) {
        Compte compte = new Compte();
String acc_id=label9.getText();
String cust_id=useridtxt.getText();
Double sold= Double.valueOf(soldetxt.getText());
compte.setNumero(acc_id);
compte.setSolde(sold);
compte.setDate(compteDao.date());
if(compte.getClient()==null)
{
    compte.setClient(new Client());
}
compte.getClient().setId(Integer.parseInt(cust_id));

compteDao.Add(compte,useridtxt.getText());

        useridtxt.setText(" ");
        soldetxt.setText(" ");
        autoid();
    }

    @FXML
    void clickfind(ActionEvent event) {
autoid();
String id = useridtxt.getText();
compteDao.get(id);

    }

}
