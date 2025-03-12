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
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Page de connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
      /*  IUser dao = new UserImpl();
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        boolean ok = dao.createUser(user);
        if (ok) {
            System.out.println("User created");
        }else {
            System.out.println("User not created");
        }

        IUser da = new UserImpl();
        User u = new User();
        user.setUsername("ahma");
        user.setPassword("123");
        boolean oui = dao.createUser(user);
        if (oui) {
            System.out.println("User created");
        }else {
            System.out.println("User not created");
        }
*/

        launch();
    }
}