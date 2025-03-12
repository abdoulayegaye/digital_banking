package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import static java.awt.SystemColor.text;

public class CompteController implements Initializable {

    @FXML private VBox compteForm;
    @FXML private TableView<Compte> comptesTable;
    @FXML private TableColumn<Compte, Integer> idCol;
    @FXML private TableColumn<Compte, String> numeroCol;
    @FXML private TableColumn<Compte, Double> balanceCol;
    @FXML private TableColumn<Compte, String> createdAtCol;
    @FXML private TableColumn<Compte, String> clientCol;
    @FXML private TableColumn<Compte, String> typeCol;
    @FXML private TableColumn<Compte, String> etatCol;

    @FXML private TextField numeroField;
    @FXML private TextField balanceField;
    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<Compte.TypeCompte> typeCombo;
    @FXML private ComboBox<Compte.EtatCompte> etatCombo;
    @FXML private TextField createdAtField;
    @FXML private TextField searchField;

    @FXML private Button newCompteBtn;
    @FXML private Button saveBtn;
    @FXML private Button updateBtn;
    @FXML private Button deleteBtn;
    @FXML private Button cancelBtn;
    @FXML private Button generateReleveBtn;

    private CompteImpl compteImpl = new CompteImpl();
    private OperationImpl operationImpl = new OperationImpl();
    private int selectedCompteId;
    private boolean isTableVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadClients();
        setupClientCombo();
        setupTypeCombo();
        setupEtatCombo();
        LoadTable();
        updateButtonStates(true);

        if (comptesTable.getItems().isEmpty()) {
            Notification.NotifError("Avertissement", "Aucun compte n’a été chargé. Vérifiez la base de données ou ajoutez des comptes.");
        }
    }

    public void LoadTable() {
        ObservableList<Compte> comptes = getComptes();
        comptesTable.setItems(comptes);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        createdAtCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCreatedAt()).asString()
        );
        clientCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(getClientFullName(cellData.getValue().getClient())));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));

    }

    public ObservableList<Compte> getComptes() {
        ObservableList<Compte> comptes = FXCollections.observableArrayList();
        try {
            List<Compte> compteList = compteImpl.listCompte("");
            if (compteList != null && !compteList.isEmpty()) {
                comptes.addAll(compteList);
            } else {
                System.out.println("Aucune donnée retournée par listCompte.");
            }
        } catch (Exception e) {
            Notification.NotifError("Erreur", "Échec du chargement des comptes : " + e.getMessage());
            System.err.println("Erreur dans getComptes : " + e.getMessage());
        }
        return comptes;
    }

    private void loadClients() {
        try {
            clientCombo.getItems().addAll(compteImpl.getAllClients());
            if (clientCombo.getItems().isEmpty()) {
                Notification.NotifError("Avertissement", "Aucun client trouvé pour la ComboBox.");
            }
        } catch (Exception e) {
            Notification.NotifError("Erreur", "Échec du chargement des clients : " + e.getMessage());
        }
    }

    private void setupClientCombo() {
        clientCombo.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return client != null ? client.getFullName() : "";
            }

            @Override
            public Client fromString(String string) {
                return null;
            }
        });
    }

    private void setupTypeCombo() {
        typeCombo.setItems(FXCollections.observableArrayList(Compte.TypeCompte.values()));
        typeCombo.setConverter(new StringConverter<Compte.TypeCompte>() {
            @Override
            public String toString(Compte.TypeCompte type) {
                return type != null ? type.name() : "";
            }

            @Override
            public Compte.TypeCompte fromString(String string) {
                return null;
            }
        });
    }

    private void setupEtatCombo() {
        etatCombo.setItems(FXCollections.observableArrayList(Compte.EtatCompte.values()));
        etatCombo.setConverter(new StringConverter<Compte.EtatCompte>() {
            @Override
            public String toString(Compte.EtatCompte etat) {
                return etat != null ? etat.name() : "";
            }

            @Override
            public Compte.EtatCompte fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    void handleAddCompte(ActionEvent event) {
        toggleForm();
        clearForm();
        numeroField.setVisible(false);
        createdAtField.setVisible(false);
        updateButtonStates(false);
        saveBtn.setDisable(false);
        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);
        generateReleveBtn.setDisable(true);

    }

    @FXML
    void handleSaveCompte(ActionEvent event) {
        if (!validateForm()) return;
        Compte compte = new Compte();
        compte.setNumero(generateAccountNumber());
        compte.setBalance(Double.parseDouble(balanceField.getText()));
        compte.setClient(clientCombo.getValue());
        compte.setCreatedAt(Date.from(Instant.now()));
        compte.setType(typeCombo.getValue());
        compte.setEtat(etatCombo.getValue());

        int result = compteImpl.createCompte(compte);
        if (result == 1) {
            Notification.NotifSuccess("Succès", "Compte créé avec succès");
            LoadTable();
            toggleForm();
            clearForm();
        } else {
            Notification.NotifError("Erreur", "Échec de la création du compte");
        }
    }

    @FXML
    void handleUpdateCompte(ActionEvent event) {
        if (!validateForm()) return;
        Compte compte = comptesTable.getSelectionModel().getSelectedItem();
        compte.setBalance(Double.parseDouble(balanceField.getText()));
        compte.setClient(clientCombo.getValue());
        compte.setType(typeCombo.getValue());
        compte.setEtat(etatCombo.getValue());

        int result = compteImpl.updateComptes(compte);
        if (result == 1 && balanceField.getText() != null){
            Notification.NotifSuccess("Succès", "Compte mis à jour");
            LoadTable();
            clearForm();
            toggleForm();
            updateButtonStates(true);
        } else {

            Notification.NotifError("Erreur", "Échec de la mise à jour du compte");
        }
    }

    @FXML
    void handleDeleteCompte(ActionEvent event) {
        int result = compteImpl.closeCompte(numeroField.getText());
        if (result == 1) {
            Notification.NotifSuccess("Succès", "Compte fermé");
            LoadTable();
            clearForm();
            toggleForm();
            updateButtonStates(true);
        } else {

            Notification.NotifError("Erreur", "Échec de la fermeture du compte");
        }
    }

    @FXML
    void getData(MouseEvent event) {
        Compte selected = comptesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedCompteId = selected.getId();
            populateForm(selected);
            toggleForm();
            updateButtonStates(false);
            deleteBtn.setDisable(false);
            updateBtn.setDisable(false);
            generateReleveBtn.setDisable(false);
            saveBtn.setDisable(true);
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        toggleForm();
        clearForm();
        updateButtonStates(true);
    }

    @FXML
    void filterComptes(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();
        ObservableList<Compte> filteredList = FXCollections.observableArrayList();
        for (Compte compte : getComptes()) {
            if (compte.getNumero().toLowerCase().contains(searchText) ||
                    getClientFullName(compte.getClient()).toLowerCase().contains(searchText) ||
                    compte.getType().name().toLowerCase().contains(searchText) ||
                    compte.getEtat().name().toLowerCase().contains(searchText)) {
                filteredList.add(compte);
            }
        }
        comptesTable.setItems(filteredList);
    }

    @FXML
    void handleGenerateReleve(ActionEvent event) {
        Compte selected = comptesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            generateRelevePDF(selected);
        } else {
            Notification.NotifError("Erreur", "Aucun compte sélectionné");
        }
    }

    private void toggleForm() {
        isTableVisible = !isTableVisible;
        comptesTable.setVisible(isTableVisible);
        compteForm.setVisible(!isTableVisible);
    }

    private boolean validateForm() {
        if (balanceField.getText().isEmpty() || clientCombo.getValue() == null ||
                typeCombo.getValue() == null || etatCombo.getValue() == null) {
            Notification.NotifError("Validation", "Veuillez remplir tous les champs obligatoires.");
            return false;
        }
        try {
            double balance = Double.parseDouble(balanceField.getText());
            if (balance < 0) {
                Notification.NotifError("Validation", "Le solde ne peut pas être négatif.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            Notification.NotifError("Validation", "Format de solde invalide.");
            return false;
        }
    }

    private void populateForm(Compte compte) {
        numeroField.setText(compte.getNumero());
        balanceField.setText(String.valueOf(compte.getBalance()));
        clientCombo.getSelectionModel().select(compte.getClient());
        typeCombo.getSelectionModel().select(compte.getType());
        etatCombo.getSelectionModel().select(compte.getEtat());
        createdAtField.setText(String.valueOf(new java.sql.Date(compte.getCreatedAt().getTime())));;
    }

    private void clearForm() {
        numeroField.clear();
        balanceField.clear();
        clientCombo.getSelectionModel().clearSelection();
        typeCombo.getSelectionModel().clearSelection();
        etatCombo.getSelectionModel().clearSelection();
        createdAtField.clear();
    }

    private String formatDate(Instant instant) {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(instant.atZone(ZoneId.systemDefault()));
    }

    private String getClientFullName(Client client) {
        return client != null && client.getNom() != null ? client.getNom() + " " + (client.getPrenom() != null ? client.getPrenom() : "") : "Inconnu";
    }

    private String generateAccountNumber() {
        return "CPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void updateButtonStates(boolean isNewCompteMode) {
        newCompteBtn.setDisable(!isNewCompteMode);
        saveBtn.setDisable(!isNewCompteMode);
        updateBtn.setDisable(isNewCompteMode);
        deleteBtn.setDisable(isNewCompteMode);
        generateReleveBtn.setDisable(isNewCompteMode);
    }

    public void generateRelevePDF(Compte compte) {
        PDDocument doc = null;
        try {
            doc = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                // Titre
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("DIGITAL BANKING - Relevé Bancaire");
                contentStream.endText();

                float yPosition = 700;
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                drawText(contentStream, "Numéro de compte: " + compte.getNumero(), 50, yPosition -= 20);
                drawText(contentStream, "Type de compte: " + compte.getType(), 50, yPosition -= 20);
                drawText(contentStream, "Solde: " + compte.getBalance() + " €", 50, yPosition -= 20);
                drawText(contentStream, "État: " + compte.getEtat(), 50, yPosition -= 20);
                drawText(contentStream, "Client: " + getClientFullName(compte.getClient()), 50, yPosition -= 20);
                Instant instant = (compte.getCreatedAt() instanceof java.sql.Date)
                        ? Timestamp.valueOf(((java.sql.Date) compte.getCreatedAt()).toLocalDate().atStartOfDay()).toInstant()
                        : compte.getCreatedAt().toInstant();
                drawText(contentStream, "Date de création: " + formatDate(instant), 50, yPosition -= 20);
                drawText(contentStream, "Date de génération: " + new Date(), 50, yPosition -= 20);
                yPosition -= 30;
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                drawText(contentStream, "Historique des Opérations", 50, yPosition);

                List<Operation> operations = operationImpl.getOperationsByAccount(compte.getNumero());

                if (operations.isEmpty()) {
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    drawText(contentStream, "Aucune opération enregistrée.", 50, yPosition -= 20);
                } else {
                    float margin = 50;
                    float tableWidth = 500;
                    float rowHeight = 20;
                    float[] columnWidths = {40, 180, 100, 100}; // ID, Date, Montant, Type
                    float tableTopY = yPosition - 20;
                    int numRows = operations.size() + 1; // +1 pour l’en-tête
                    float tableBottomY = tableTopY - (numRows * rowHeight); // Calcul de la hauteur totale

                    contentStream.setNonStrokingColor(0.9f, 0.9f, 0.9f); // Gris clair
                    contentStream.addRect(margin, tableTopY - rowHeight, tableWidth, rowHeight);
                    contentStream.fill();
                    contentStream.setNonStrokingColor(0, 0, 0); // Retour à noir

                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    float xPosition = margin;
                    drawText(contentStream, "ID", xPosition + 5, tableTopY - 15);
                    xPosition += columnWidths[0];
                    drawText(contentStream, "Date", xPosition + 5, tableTopY - 15);
                    xPosition += columnWidths[1];
                    drawText(contentStream, "Montant (€)", xPosition + 5, tableTopY - 15);
                    xPosition += columnWidths[2];
                    drawText(contentStream, "Type", xPosition + 5, tableTopY - 15);


                    contentStream.setLineWidth(0.5f);
                    float nextY = tableTopY;


                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    for (Operation op : operations) {
                        nextY -= rowHeight;
                        xPosition = margin;


                        String dateStr = op.getDateOp() != null ? formatDate(op.getDateOp()) : "Inconnue";
                        drawText(contentStream, String.valueOf(op.getId()), xPosition + 5, nextY - 15);
                        xPosition += columnWidths[0];
                        drawText(contentStream, dateStr.length() > 25 ? dateStr.substring(0, 25) + "..." : dateStr, xPosition + 5, nextY - 15);
                        xPosition += columnWidths[1];
                        drawText(contentStream, String.format("%.2f", op.getAmount()), xPosition + 5, nextY - 15);
                        xPosition += columnWidths[2];
                        drawText(contentStream, op.getType().toString(), xPosition + 5, nextY - 15);


                        contentStream.moveTo(margin, nextY);
                        contentStream.lineTo(margin + tableWidth, nextY);
                        contentStream.stroke();
                    }


                    xPosition = margin;
                    for (float width : columnWidths) {
                        contentStream.moveTo(xPosition, tableTopY);
                        contentStream.lineTo(xPosition, tableBottomY);
                        contentStream.stroke();
                        xPosition += width;
                    }
                    contentStream.moveTo(xPosition, tableTopY);
                    contentStream.lineTo(xPosition, tableBottomY);
                    contentStream.stroke();

                    contentStream.moveTo(margin, tableTopY);
                    contentStream.lineTo(margin + tableWidth, tableTopY);
                    contentStream.stroke();
                }
            }


            String fileName = "releve_" + compte.getNumero() + "_" + System.currentTimeMillis() + ".pdf";
            doc.save(fileName);
            Notification.NotifSuccess("Succès", "Relevé généré : " + new File(fileName).getAbsolutePath());

        } catch (IOException e) {
            Notification.NotifError("Erreur", "Échec de génération du PDF : " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }



    @FXML
    public void Retour(ActionEvent event) throws IOException {
        Outils.load(event, "Deconnexion", "/fxml/accueil.fxml");
    }
}