package com.banking.digitalbankingproject.tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Outils {

    // Expression régulière pour valider un email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    /**
     * Charge une nouvelle page dans l'application.
     *
     * @param event L'événement déclencheur.
     * @param title Le titre de la nouvelle fenêtre.
     * @param url   Le chemin vers le fichier FXML.
     */
    private void loadPage(ActionEvent event, String title, String url) throws IOException {
        // Cache la fenêtre actuelle
        ((Node) event.getSource()).getScene().getWindow().hide();

        // Charge le fichier FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
        Parent root = fxmlLoader.load();

        // Crée une nouvelle scène
        Scene scene = new Scene(root);

        // Crée une nouvelle fenêtre
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    /**
     * Méthode statique pour charger une nouvelle page.
     *
     * @param event L'événement déclencheur.
     * @param title Le titre de la nouvelle fenêtre.
     * @param url   Le chemin vers le fichier FXML.
     */
    public static void load(ActionEvent event, String title, String url) {
        try {
            // Appelle la méthode loadPage pour charger la nouvelle page
            new Outils().loadPage(event, title, url);
        } catch (IOException e) {
            // Gère l'exception en affichant une erreur
            System.err.println("Erreur lors du chargement de la page : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Vérifie si l'email est valide.
     *
     * @param email L'email à valider.
     * @return true si l'email est valide, sinon false.
     */
    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }
}