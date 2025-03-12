package com.banking.digitalbankingproject.tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitaire pour la gestion des fenêtres et de la navigation
 */
public class Outils {
    
    // Map pour stocker les données partagées entre les vues
    private static final Map<String, Object> sharedData = new HashMap<>();
    
    /**
     * Charge une nouvelle page FXML
     * 
     * @param event Événement déclencheur
     * @param title Titre de la fenêtre
     * @param url Chemin vers le fichier FXML
     * @throws IOException En cas d'erreur de chargement du fichier FXML
     */
    private void loadPage(ActionEvent event, String title, String url) throws IOException {
        try {
            // Cacher la fenêtre actuelle
            ((Node) event.getSource()).getScene().getWindow().hide();
            
            // Charger la nouvelle page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
            Parent root = fxmlLoader.load();
            
            // Créer et configurer la nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false); // Empêcher le redimensionnement
            
            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            Notification.NotifError("Erreur", "Impossible de charger la page : " + url);
            throw e;
        }
    }

    /**
     * Méthode statique pour charger une page
     * 
     * @param event Événement déclencheur
     * @param title Titre de la fenêtre
     * @param url Chemin vers le fichier FXML
     * @throws IOException En cas d'erreur de chargement du fichier FXML
     */
    public static void load(ActionEvent event, String title, String url) throws IOException {
        new Outils().loadPage(event, title, url);
    }

    /**
     * Charge une nouvelle page FXML dans une fenêtre modale
     * 
     * @param title Titre de la fenêtre
     * @param url Chemin vers le fichier FXML
     * @return Le contrôleur de la nouvelle fenêtre
     * @throws IOException En cas d'erreur de chargement du fichier FXML
     */
    public static Object loadModal(String title, String url) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Outils.class.getResource(url));
            Parent root = fxmlLoader.load();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage(StageStyle.UTILITY); // Style modal
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);
            
            // Afficher la fenêtre de manière modale
            stage.showAndWait();
            
            return fxmlLoader.getController();
        } catch (IOException e) {
            Notification.NotifError("Erreur", "Impossible de charger la fenêtre modale : " + url);
            throw e;
        }
    }

    /**
     * Stocke l'ID du compte pour une utilisation ultérieure
     * 
     * @param compteId ID du compte à stocker
     */
    public static void setCompteId(int compteId) {
        sharedData.put("compteId", compteId);
    }

    /**
     * Récupère l'ID du compte stocké
     * 
     * @return ID du compte ou null si non trouvé
     */
    public static Integer getCompteId() {
        return (Integer) sharedData.get("compteId");
    }

    /**
     * Stocke une donnée partagée
     * 
     * @param key Clé de la donnée
     * @param value Valeur à stocker
     */
    public static void setSharedData(String key, Object value) {
        sharedData.put(key, value);
    }

    /**
     * Récupère une donnée partagée
     * 
     * @param key Clé de la donnée
     * @return Valeur stockée ou null si non trouvée
     */
    public static Object getSharedData(String key) {
        return sharedData.get(key);
    }

    /**
     * Supprime une donnée partagée
     * 
     * @param key Clé de la donnée à supprimer
     */
    public static void removeSharedData(String key) {
        sharedData.remove(key);
    }

    /**
     * Efface toutes les données partagées
     */
    public static void clearSharedData() {
        sharedData.clear();
    }
}
