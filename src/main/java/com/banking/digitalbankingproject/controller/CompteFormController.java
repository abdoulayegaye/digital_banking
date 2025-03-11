package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class CompteFormController {

    public Text clientTxt;
    public Pane pane;
    @FXML
    private Text erreurTXT;
    @FXML
    private TextField balanceTFD;

    public void initialize(){
        Client client = new ClientController().getclientCompte();
        if(client == null) {
            erreurTXT.setVisible(true);
            erreurTXT.setText("Veillez selectionner un client d'abord");
        }else {
            pane.setVisible(true);
            clientTxt.setText("Nom: "+client.getNom()+"\nPrnom: "+client.getPrenom()+"\nEmail: "+client.getEmail());
        }
    }
    public void Gestion_Compte(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion de Comptes", "/fxml/comptes.fxml");
    }

    public void Creer_Compte(ActionEvent event) throws IOException {
        Client client = new ClientController().getclientCompte();
        if(client == null){
            Notification.NotifError("Error", "Veillez selectionner un client d'abord");

        }else if(!balanceTFD.getText().isEmpty()){
                double balance = Double.parseDouble(balanceTFD.getText());
                if(balance < 5000 && balanceTFD.isVisible()){
                    Notification.NotifError("Error", "Minimum 5 000F");
                }else {
                    Compte compte = setCompte(client);

                    boolean ok = new CompteImpl().CreateCompte(compte);
                    if (ok){
                        Notification.NotifSuccess("Success", "Votre Compte a bien été créé!!");
                        Outils.load(event,"Gestion Comptes", "/fxml/comptes.fxml");

                    }else {
                        Notification.NotifError("Error", "Erreur d'ajout");
                    }

                }
        }else {

            Compte compte = setCompte(client);
            boolean ok = new CompteImpl().CreateCompte(compte);
            if (ok){
                Notification.NotifSuccess("Success", "Le Compte a bien été créé");
                Outils.load(event,"Gestion Comptes", "/fxml/comptes.fxml");
            }else {
                Notification.NotifError("Error", "Erreur d'ajout");
            }
            //otification.NotifSuccess("Bien", "Bien Fait");
        }

    }

    public void Ajouter_Balance(ActionEvent actionEvent) {
        balanceTFD.setVisible(true);
        TextFormatter<Double> formatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")){
                return change;
            }else {
                return null;
            }
        });

        balanceTFD.setTextFormatter(formatter);
//        formatter.valueProperty().addListener((observable, oldValue, newValue) ->{
//            System.out.println("Valeur actuelle: "+newValue);
//        });
    }

    public Compte setCompte(Client client){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String num = "CC"+client.getId()+"-"+now.format(formatter);
        Compte compte = new Compte();
        compte.setClient(client);
        compte.setNumero(num);
        compte.setBalance(
                balanceTFD.isVisible() ? Double.parseDouble(balanceTFD.getText()) : null
        );
        return compte;
    }
}
