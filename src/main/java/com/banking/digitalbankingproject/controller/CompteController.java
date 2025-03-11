package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.itextpdf.layout.properties.TextAlignment.CENTER;


public class CompteController {

    @FXML
    private TableView<Compte> tableview;
    @FXML
    private TableColumn<Compte, String> numCompteCol;
    @FXML
    private TableColumn<Compte, Double> soldeCol;
    @FXML
    private TableColumn<Compte, Client> clientCol;
    @FXML
    private TableColumn<Compte, String> nomCol;
    @FXML
    private TableColumn<Compte, String> prenomCol;
    @FXML
    private TableColumn<Compte, String> emailCol;
    @FXML
    private TableColumn<Compte, Date> dateOuvCol;
    @FXML
    private Pane LASPan;
    @FXML
    private Text TextL;
    @FXML
    private TextField searchTFD;
    @FXML
    private Pane InfoPANE;
    @FXML
    private Text numCompteTXT;
    @FXML
    private Text dateOuvTXT;
    @FXML
    private Text soldeTXT;
    @FXML
    private Text clientTXT;
    private List<Compte> comptes = null;
    private FilteredList<Compte> listefiltrer;
    @Getter
    public static Compte CompteSelectionne;


    public void initialize(){
        CompteSelectionne = null;

        comptes = new CompteImpl().GetAllCompte();
        if (comptes == null || comptes.isEmpty()){
            TextL.setText("Pas de compte pour le Momment");
            LASPan.setVisible(true);
        }else {
            numCompteCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
            soldeCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
            clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
            nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getNom()));
            prenomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getPrenom()));
            emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getEmail()));
            dateOuvCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            listefiltrer = new FilteredList<>(FXCollections.observableList(comptes), p->true);
            tableview.setItems(listefiltrer);
            tableview.setVisible(true);
            LASPan.setVisible(true);

            AjouterMenu();

            searchTFD.textProperty().addListener((observable, olValue, newValue) -> {
                filtrerComptes(newValue);
            } );
        }

    }
    public void filtrerComptes(String recherche){
        listefiltrer.setPredicate(compte -> {
            if (recherche == null || recherche.isEmpty()){
                return true;
            }
            if (compte.getNumero().toLowerCase().contains(recherche.toLowerCase())){
                return true;
            }if(compte.getClient().getNom().toLowerCase().contains(recherche.toLowerCase())){
                return true;
            }if (compte.getClient().getPrenom().toLowerCase().contains(recherche.toLowerCase())){
                return true;
            }
            return false;
        });
    }
    public void Accueil(ActionEvent event) throws IOException {
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");
    }

    public void Nouveau_Compte(ActionEvent event) throws IOException {
        Outils.load(event ,"Nouveau compte", "/fxml/compte_form.fxml");
    }

    protected void AjouterMenu(){

        ContextMenu contextMenu = new ContextMenu();
        MenuItem selectionner = new MenuItem("Selectionner");
        MenuItem consulterSolde = new MenuItem("Consulter le solde");
        MenuItem fermerCompte = new MenuItem("Fermer ce compter");
        MenuItem releve = new MenuItem("Generer le Relevé Bancaire");
        contextMenu.getItems().addAll(selectionner, consulterSolde, releve, fermerCompte);

        selectionner.setOnAction(event ->{
            CompteSelectionne = tableview.getSelectionModel().getSelectedItem();
            Notification.NotifSuccess("Success", "Client sélectionner");
        });

        consulterSolde.setOnAction(event ->{
            tableview.setVisible(false);
            Compte compte = tableview.getSelectionModel().getSelectedItem();
            numCompteTXT.setText("Numero de compte: "+compte.getNumero());
            soldeTXT.setText("Solde: \n"+compte.getBalance()+" F");
            dateOuvTXT.setText("Date d'ouverture: "+compte.getCreatedAt());
            clientTXT.setText("NOM: "+compte.getClient().getNom()+"\nPRENOM: "+compte.getClient().getPrenom()+"\nEMAIL: "+compte.getClient().getEmail());
            InfoPANE.setVisible(true);
        });

        fermerCompte.setOnAction(event -> {
            Compte compte = tableview.getSelectionModel().getSelectedItem();
            ICompte iCompte = new CompteImpl();
            boolean ok = iCompte.DeleteCompte(compte);
            if(ok){
                Notification.NotifSuccess("Success", "Ce Compte a été Fermé !");
            }else {
                Notification.NotifError("Erreur", "Impossible de supprimer ce Compte");
            }
        });

        releve.setOnAction(event -> {
            Compte compte = tableview.getSelectionModel().getSelectedItem();
            GenererPdf(compte);
        });

        tableview.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->{
            if(event.getButton() == MouseButton.SECONDARY){
                if (tableview.getSelectionModel().getSelectedItem() != null){
                    tableview.setContextMenu(contextMenu);
                }
            }
        });
    }

    public void Retour(ActionEvent event) {
        InfoPANE.setVisible(false);
        initialize();
    }
    private void GenererPdf(Compte compte){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy-ss");
        String date = now.format(formatter1).toString();

        String cheminFichier = "src/main/resources/releve/releve_compte_"+compte.getNumero()+"_"+date+".pdf";
        String cheminLogo = "src/main/resources/images/logo.jpeg";
        String cheminSignature = "src/main/resources/images/signature.jpeg";

        List<Operation> operationList = new OperationImpl().getOperationsByIdCompte(compte.getId());

        try {
            // Création du PDF
            PdfWriter writer = new PdfWriter(cheminFichier);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);


            // ✅ Ajouter le logo en haut à gauche
            ImageData logoData = ImageDataFactory.create(cheminLogo);
            Image logo = new Image(logoData);
            logo.setHeight(40);
            logo.setWidth(40);
            document.add(logo);
            document.add(new Paragraph("Digital Banking")).setTopMargin(-5);

            // ✅ Titre et infos
            document.add(new Paragraph("Relevé de Compte").setBold().setFontSize(22).setTextAlignment(CENTER));
            document.add(new Paragraph("Nom: "+compte.getClient().getNom()).setFontSize(12));
            document.add(new Paragraph("Prenom: "+compte.getClient().getPrenom()).setFontSize(12));
            document.add(new Paragraph("Email: "+compte.getClient().getEmail()).setFontSize(12));
            document.add(new Paragraph("Numéro de compte: "+compte.getNumero()).setFontSize(12));
            document.add(new Paragraph("Date: "+ now.format(formatter)).setFontSize(12));
            document.add(new Paragraph("\n"));

            if(!operationList.isEmpty()){

                // ✅ Table des opérations
                float[] columnWidths = {100F, 200F, 100F};
                Table table = new Table(columnWidths);

                // En-têtes
                table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold().setTextAlignment(CENTER)));
                table.addHeaderCell(new Cell().add(new Paragraph("Description").setBold().setTextAlignment(CENTER)));
                table.addHeaderCell(new Cell().add(new Paragraph("Montant").setBold().setTextAlignment(CENTER)));

                document.add(new Paragraph("Leste des Opérations Effectuées ").setFontSize(14).setBold().setTextAlignment(CENTER));
                document.add(new Paragraph("\n"));

                operationList.forEach(operation -> {
                    table.addCell(operation.getDateOp().toString()).setTextAlignment(CENTER);
                    table.addCell(operation.getType().toString()).setTextAlignment(CENTER);
                    table.addCell(operation.getAmount()+" FCFA").setTextAlignment(CENTER);
                });
                    table.setAutoLayout();
                    table.setTextAlignment(CENTER);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                // Ajout de la table
                document.add(table);

                // ✅ Solde final
                document.add(new Paragraph("\nSolde actuel: "+compte.getBalance()+" FCFA").setBold().setFontSize(14));

                // ✅ Espace avant signature/cachet
                document.add(new Paragraph("\n\n"));

            }else {
                document.add(new Paragraph("Aucune Opteration effectuée").setTextAlignment(CENTER).setBold().setFontSize(14));
                // ✅ Solde final
                document.add(new Paragraph("\nSolde actuel: "+compte.getBalance()+" FCFA").setBold().setFontSize(14));

            }

            // ✅ Remerciement / Footer
            document.add(new Paragraph("\nMerci pour votre confiance."));

            document.add(new Paragraph("Le Directeur General: Dieu-Beni JINANONN"));

            // ✅ Signature à droite
            ImageData signatureData = ImageDataFactory.create(cheminSignature);
            Image signature = new Image(signatureData);
            signature.setWidth(120);
            signature.setHeight(60);
            signature.setPaddingTop(-3);
            signature.setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(signature);

            // ✅ Fermer le document
            document.close();
            Notification.NotifSuccess("Success", "Relevé généré avec Succès");

        } catch (IOException e) {
            Notification.NotifError("Error", "Erreur de génération");
        }
    }
}
