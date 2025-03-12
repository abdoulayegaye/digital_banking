package com.banking.digitalbankingproject;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Page de connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {


// Fermeture de compte


//        ICompte daoCompte = new CompteImpl();
//
//        String numero = "CPT-3C788F5E";
//
//        try {
//            int ferme = daoCompte.closeCompte(numero);
//            if (ferme == 1) {
//                System.out.println("Le compte " + numero + " a été fermé avec succès.");
//            } else {
//                System.out.println("Impossible de fermer le compte " + numero);
//            }
//        } catch (IllegalArgumentException e) {
//            System.out.println("Erreur : " + e.getMessage());
//        }


        //Creation de compte

//        ICompte daoCompte = new CompteImpl();
//        IClient daoClient = new ClientImpl();
//        Compte compte = new Compte();
//
//        compte.setBalance(0);
//        compte.setCreatedAt(Instant.now());
//        compte.setClient(daoClient.get(2));
//        int ok = daoCompte.createCompte(compte);
//        if (ok == 1) {
//            System.out.println("Compte created");
//
//        }else{
//            System.out.println("Compte not created");
//        }


        //Association de compte

//        ICompte daoCompte = new CompteImpl();
//        IClient daoClient = new ClientImpl();
//
//        CompteImpl compteService = new CompteImpl();
//
//        Compte compte = daoCompte.getCompteById(2);
//        Client client = daoClient.get(11);
//
//        if (compte != null && client != null) {
//            compteService.associateCompte(compte, client);
//        } else {
//            System.out.println("Compte ou client introuvable !");
//        }


        //Consultation de solde

//        ICompte compteService = new CompteImpl();
//
//        String numero = "CPT-3C788F5E";
//
//        try {
//            double solde = compteService.consultSolde(numero);
//            System.out.println("Le solde du compte " + numero + " est de : " + solde + " FCFA");
//        } catch (IllegalArgumentException e) {
//            System.out.println("Erreur : " + e.getMessage());
//        }


        launch();
    }
}