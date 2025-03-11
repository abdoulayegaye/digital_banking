package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class CompteController implements Initializable {
    private ClientImpl clientDao = new ClientImpl();
    private CompteImpl compteDao = new CompteImpl();

    @FXML
    private TableColumn<Compte, String> numCol;

    @FXML
    private TableColumn<Compte, String> etatCol;
    @FXML
    private TableColumn<Compte, Double> balanceCol;
    @FXML
    private TableColumn<Compte, Client> clientCol;

    @FXML
    private TableView<Compte> compteTb;

    @FXML
    private Button ajouterBtn;

    @FXML
    private ComboBox<Client> clientCbb;

    @FXML
    private Button consulterBtn;

    @FXML
    private DatePicker dateOuvertureDp;

    @FXML
    private Button fermerBtn;

    @FXML
    private TextField numeroTfd;

    @FXML
    private TextField soldeTfd;

    @FXML
    void add(ActionEvent event) {
        try
        {
            Compte compte = new Compte();
            compte.setNumero(numeroTfd.getText());
            compte.setClient(clientCbb.getValue());
            compte.setBalance(Double.parseDouble(soldeTfd.getText()));
            compte.setCreatedAt(dateOuvertureDp.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            if (compteDao.creerCompte(compte)==1)
                Notification.NotifSuccess("Succes","Compte creer avec succes!");
            loadTable();
            clearField();
        }catch (Exception e)
        {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }

    @FXML
    void close(ActionEvent event) {
        try{
            if (compteDao.fermerCompte(numeroTfd.getText())==1)
                Notification.NotifSuccess("Succes","Compte fermer");
            clearField();
            loadTable();
            loadClient();
        }catch (Exception e)
        {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }

    @FXML
    void getcons(ActionEvent event) {

    }

    private void loadClient() {
        try {
            clientCbb.setItems(FXCollections.observableArrayList(clientDao.getClient()));
        } catch (Exception e) {
            Notification.NotifError("Erreur lors du chargement des categories", e.getMessage());
        }
    }
    private String generateCodeCompte() {
        LocalDate date = LocalDate.now();
        return  "CMP-"+((date.getDayOfMonth()<10)?"0"+date.getDayOfMonth():date.getDayOfMonth())
                +((date.getMonthValue()<10)?"0"+date.getMonthValue():date.getMonthValue())+date.getYear()
                +genererNumeroString(compteDao.obtenirTousLesComptes().size()+1);

    }

    void clearField(){
        soldeTfd.setText("");
        numeroTfd.setText(generateCodeCompte());
        dateOuvertureDp.setValue(null);
        clientCbb.setValue(null);
        soldeTfd.setDisable(false);
        clientCbb.setDisable(false);
        dateOuvertureDp.setDisable(false);
        ajouterBtn.setDisable(false);
    }
    @FXML
    void getData(MouseEvent event) {
        Compte compte = compteTb.getSelectionModel().getSelectedItem();
        clientCbb.setValue(compte.getClient());
        numeroTfd.setText(compte.getNumero());
        soldeTfd.setText(compte.getBalance()+"");
        dateOuvertureDp.setValue(compte.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate());
        ajouterBtn.setDisable(true);
        clientCbb.setDisable(true);
        numeroTfd.setDisable(true);
        soldeTfd.setDisable(true);
        dateOuvertureDp.setDisable(true);
    }

    private String genererNumeroString(int numero){
        if (numero<10)
            return "00"+numero;
        else if (numero<100)
            return "0"+numero;
        else
            return ""+numero;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadClient();
        numeroTfd.setText(generateCodeCompte());
        loadTable();
    }
    public void loadTable(){
        ObservableList<Compte> liste = FXCollections.observableArrayList();
        liste.addAll(compteDao.obtenirTousLesComptes());
        compteTb.setItems(liste);
        numCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }
}
