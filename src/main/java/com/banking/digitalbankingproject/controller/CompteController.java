package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CompteController {

    @FXML
    private TableColumn<Compte, String> cliC;

    @FXML
    private Text client;

    @FXML
    private TableColumn<Compte, Timestamp> dateC;

    @FXML
    private Pane form_compte;

    @FXML
    private TableColumn<Compte, String> numC;
    @FXML
    private TableColumn<Compte, String> nomCliC;

    @FXML
    private TextField solde;

    @FXML
    private TableColumn<Compte, Double> soldeC;

    @FXML
    private TableView<Compte> tableViewComptes;

    @FXML
    private TextField txtRecherche;

    private ICompte compteService = new CompteImpl();
    @Getter
    private static Compte compteselect = null;
    private List<Compte> compteList;


    @FXML
    private void initialize() {
        compteList = compteService.getAllComptes();
        soldeC.setCellValueFactory(new PropertyValueFactory<>("balance"));
        numC.setCellValueFactory(new PropertyValueFactory<>("numero"));
        dateC.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        cliC.setCellValueFactory(new PropertyValueFactory<>("client"));
        nomCliC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getNom()));
        FilteredList<Compte> filteredList = recherche();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem selectionner = new MenuItem("Selectionner");
        selectionner.setOnAction(event -> {
            compteselect = tableViewComptes.getSelectionModel().getSelectedItem();
            Notification.NotifSuccess("succes","Compte selectionner");
        });
        MenuItem supprimer = new MenuItem("Fermer compte");
        supprimer.setOnAction(event -> {
            boolean ok = compteService.supprimerCompte(tableViewComptes.getSelectionModel().getSelectedItem());
            if (ok) {
                Notification.NotifSuccess("succes","Compte fermer");
                initialize();
            }else{
                Notification.NotifError("erreur","Compte non fermer");
            }
        });
        MenuItem imprimer = new MenuItem("Imprimer historique de compte");
        imprimer.setOnAction(event -> {
            Compte compte = tableViewComptes.getSelectionModel().getSelectedItem();
            generatePDF(compte);
        });
        contextMenu.getItems().addAll(selectionner, supprimer, imprimer);

        //if (tableViewComptes.getSelectionModel().getSelectedItem() != null) {
            tableViewComptes.setContextMenu(contextMenu);

        tableViewComptes.setItems(filteredList);

    }

    @FXML
    void Retour_Accueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouter_compte(ActionEvent event) {
        if (ClientController.getClientselect() != null) {
            form_compte.setVisible(true);
            client.setText("Nom: "+ClientController.getClientselect().getNom()+ "\nPrenom: "+ClientController.getClientselect().getPrenom());
        }else {
            Notification.NotifError("erreur","aucun client selectionner");
        }
    }

    @FXML
    void valider_compte(ActionEvent event) {
        Compte compte =new Compte();
        boolean ok = false;
        try {
            compte.setBalance(Double.parseDouble(solde.getText()));
        }catch (NumberFormatException e) {
            Notification.NotifError("erreur","aucun solde");
            return;
        }

        compte.setClient(ClientController.getClientselect());
        compte.setNumero(genererNumeroCompte());

            ok = compteService.ajouterCompte(compte);

        if (ok) {
            Notification.NotifSuccess("succes", "enregistré");
            form_compte.setVisible(false);
            solde.setText("");
            initialize();
        }else {
            Notification.NotifError("erreur", "erreur");
        }

    }
    public static String genererNumeroCompte() {
        // Obtenir l'année et le mois actuels (ex : 202403 pour mars 2024)
        String datePrefix = new SimpleDateFormat("yyyyMM").format(new Date());

        // Générer un numéro unique de 6 chiffres
        Random random = new Random();
        int numeroAleatoire = 100000 + random.nextInt(900000); // Assure un nombre à 6 chiffres

        // Combiner les deux parties
        return datePrefix + numeroAleatoire;
    }

    private FilteredList<Compte> recherche(){
        FilteredList<Compte> filteredList = new FilteredList<>(FXCollections.observableList(compteList),p -> true);
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(compte -> {
                if ( compte.getNumero().toLowerCase().contains((newValue).toLowerCase())||
                        compte.getClient().getNom().toLowerCase().contains((newValue).toLowerCase())) {
                    return true;
                }
                return false;

            });
        });
        return filteredList;
    }
    private void generatePDF(Compte compte) {
        String filename = "/Amabotawa_marx/digital_banking/digital_banking/src/main/resources/releves/compte"+compte.getNumero()+".pdf";
        try {
            //File file = new File(filename);
            PdfWriter writer = new PdfWriter(filename);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("RELEVE DE COMPTE").setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Nom: "+compte.getClient().getNom()+"\nPrenom: "+compte.getClient().getPrenom()));
            document.add(new Paragraph("MONTANT EN COMPTE: "+compte.getBalance()));

            List<Operation> operations= new OperationImpl().getOperationByCompteId(compte.getId());
            Table table = new Table(UnitValue.createPercentArray(new float[]{10, 25, 20, 20, 25})).useAllAvailableWidth();
            table.addHeaderCell("ID");
            table.addHeaderCell("Date");
            table.addHeaderCell("Montant");
            table.addHeaderCell("Type");
            table.addHeaderCell("Compte");

            operations.forEach(op -> {

                // Remplissage du tableau avec les opérations

                    table.addCell(String.valueOf(op.getId()));
                    table.addCell(op.getDateOp().toString());
                    table.addCell(String.format("%.2f €", op.getAmount()));
                    table.addCell(op.getType().name());
                    table.addCell(op.getCompte().getNumero()); // Suppose que `Compte` a un champ `numero`

            });
            document.add(table);
            document.close();

            //System.out.println("PDF généré avec succès : " + filename.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
