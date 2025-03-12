package com.banking.digitalbankingproject;

import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

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
//        IUser iUser = new UserImpl();
//        User user = new User();
//        user.setUsername("user");
//        user.setPassword("passer");
//        boolean ok = iUser.createUser(user);
//        if (ok) {
//            System.out.println("user created succed");
//        }
        launch();
    }
}