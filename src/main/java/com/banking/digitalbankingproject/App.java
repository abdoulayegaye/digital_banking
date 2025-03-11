package com.banking.digitalbankingproject;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

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
        /*IUser iUser = new UserImpl();
        User user = new User();
        user.setUsername("hanane");
        user.setPassword("passer");
        boolean ok = iUser.createUser(user);
        if (ok) {
            System.out.println("User created !");
        }*/
        /*IClient iClient = new ClientImpl();
        Client client = new Client();
        client.setNom("Abderemane");
        client.setPrenom("Hanane");
        client.setEmail("abderemane@gmail.com");
        int ok = iClient.creerClient(client);
        if (ok == 1) {
            System.out.println("creation success");
        }else {
            System.out.println("creation failed    ");
        }*/

        launch();
    }
}