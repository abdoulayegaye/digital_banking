package com.example.projet_java_fx;
import com.example.projet_java_fx.entity.Clients;
import com.example.projet_java_fx.entity.Comptes;
import com.example.projet_java_fx.service.ICompte;
import com.example.projet_java_fx.service.impl.ClientImpl;
import com.example.projet_java_fx.service.impl.CompteImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/pages/lgin.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Login Pages ");
        stage.show();

    }

    public static void main(String[] args) {
        // Créer un client
//        Clients cl = new Clients();
//        cl.setId(2); // Assurez-vous que cet ID existe dans la table `clients`
//
//        // Créer un compte
//        Comptes c = new Comptes();
//        c.setNumero("12345");
//        c.setSolde(200);
//        c.setDate(Timestamp.valueOf(LocalDateTime.now()));
//        c.setIdclient(cl);

        // Insérer le compte dans la base de données
//        ICompte dao = new CompteImpl();
//        int ok = dao.create(c);

        // Afficher le résultat
//        if (ok == 1) {
//            System.out.println("Compte créé avec succès.");
//        } else {
//            System.out.println("Échec de la création du compte.");
//        }
      launch();
    }
}
