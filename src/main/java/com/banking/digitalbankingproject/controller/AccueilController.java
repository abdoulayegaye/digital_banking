package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import com.banking.digitalbankingproject.database.Db;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AccueilController {
    @FXML
    private Label lblTotalClients;

    @FXML
    private Label lblTotalComptes;

    @FXML
    private Label lblTotalOperations;

    @FXML
    private Label lblTotalDepots;

    @FXML
    private Label lblTotalRetraits;

    @FXML
    private Label lblSoldeGlobal;

    @FXML
    private Label lblTotalVirements;


    @FXML
    private Button btnClients;

    @FXML
    private Button btnComptes;

    @FXML
    private Button btnOperations;

    @FXML
    private Button btnSettings;
    @FXML
    public void initialize() {
        loadStatistics();
    }

    @FXML
    private void handleClients(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des clients", "/fxml/clients.fxml");
    }
    @FXML
    private void handleComptes(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des comptes", "/fxml/comptes.fxml");
    }
    @FXML
    private void handleOperations(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des operations", "/fxml/operations.fxml");
    }


    public void loadStatistics() {
        Db db = new Db();
        DecimalFormat df = new DecimalFormat("#,##0.00"); // Format : 1 234 567.89

        try {
            // Nombre total de clients
            String sqlClients = "SELECT COUNT(*) FROM clients";
            db.initPrepar(sqlClients);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                lblTotalClients.setText("Clients: " + rs.getInt(1));
            }

            // Nombre total de comptes
            String sqlComptes = "SELECT COUNT(*) FROM comptes";
            db.initPrepar(sqlComptes);
            rs = db.executeSelect();
            if (rs.next()) {
                lblTotalComptes.setText("Comptes: " + rs.getInt(1));
            }

            // Nombre total d'opérations
            String sqlOperations = "SELECT COUNT(*) FROM operations";
            db.initPrepar(sqlOperations);
            rs = db.executeSelect();
            if (rs.next()) {
                lblTotalOperations.setText("Opérations: " + rs.getInt(1));
            }

            // Total des dépôts
            String sqlDepots = "SELECT COALESCE(SUM(amount), 0) FROM operations WHERE type = 'DEPOT'";
            db.initPrepar(sqlDepots);
            rs = db.executeSelect();
            if (rs.next()) {
                BigDecimal totalDepots = rs.getBigDecimal(1);
                lblTotalDepots.setText("Total Dépôts: " + df.format(totalDepots) + " €");
            }

            // Total des retraits
            String sqlRetraits = "SELECT COALESCE(SUM(amount), 0) FROM operations WHERE type = 'RETRAIT'";
            db.initPrepar(sqlRetraits);
            rs = db.executeSelect();
            if (rs.next()) {
                BigDecimal totalRetraits = rs.getBigDecimal(1);
                lblTotalRetraits.setText("Total Retraits: " + df.format(totalRetraits) + " €");
            }

            // Solde global (Somme des soldes des comptes)
            String sqlSolde = "SELECT COALESCE(SUM(balance), 0) FROM comptes";
            db.initPrepar(sqlSolde);
            rs = db.executeSelect();
            if (rs.next()) {
                BigDecimal soldeGlobal = rs.getBigDecimal(1);
                lblSoldeGlobal.setText("Solde Global: " + df.format(soldeGlobal) + " €");
            }

            // Nombre total de virements
            String sqlVirements = "SELECT COUNT(*) FROM operations WHERE type = 'VIREMENT'";
            db.initPrepar(sqlVirements);
            rs = db.executeSelect();
            if (rs.next()) {
                lblTotalVirements.setText("Virements: " + rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}