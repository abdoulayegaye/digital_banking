package com.banking.digitalbankingproject;

import Dao.ClientDAO;
import Dao.CompteDAO;
import Dao.OperationDAO;
import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Application Bancaire");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
            launch();
      /* IUser dao = new UserImpl();
       User User = new User();
       User.setPassword("admin");
       User.setUsername("ndifa");
       boolean ok = dao.createUser(User);
       if (ok) {
               System.out.println("User created");
               }else{
                System.out.println("User not created");
                }
*/


        }

    }
