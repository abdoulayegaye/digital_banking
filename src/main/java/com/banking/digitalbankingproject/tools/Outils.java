package com.banking.digitalbankingproject.tools;

import com.banking.digitalbankingproject.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Outils {

    private void loadPage(ActionEvent event, String title, String url) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        String cleanUrl = url.replace(".fxml", "").replace("/fxml/", "");
        String fxmlPath = "/fxml/" + cleanUrl + ".fxml";
        
        System.out.println("Tentative de chargement du fichier FXML : " + fxmlPath);
        URL fxmlUrl = App.class.getResource(fxmlPath);
        
        if (fxmlUrl == null) {
            System.err.println("Fichier FXML introuvable : " + fxmlPath);
            throw new IOException("Impossible de trouver le fichier FXML: " + fxmlPath);
        }
        
        System.out.println("Fichier FXML trouvé à : " + fxmlUrl);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public static void load(ActionEvent event, String title, String url) throws IOException {
        new Outils().loadPage(event, title, url);
    }
}
