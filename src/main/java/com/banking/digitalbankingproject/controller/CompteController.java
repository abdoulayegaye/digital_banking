package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.Utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CompteController {
    @FXML
    private ComboBox<Client> comboClient;
    @FXML
    private TextField txtSoldeInitial;
    @FXML
    private TableView<Compte> tableComptes;
    @FXML
    private TableColumn<Compte, String> colNumero;
    @FXML
    private TableColumn<Compte, Double> colSolde;
    @FXML
    private TableColumn<Compte, String> colDateCreation;
    @FXML
    private TableColumn<Compte, String> colClient;
    @FXML
    private Button btnCreer;
    @FXML
    private Button btnFermer;

    private ICompte compteService;
    private IClient clientService;
    private ObservableList<Compte> comptesList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        try {
            compteService = new CompteImpl();
            clientService = new ClientImpl();
            setupComboBox();
            setupTableColumns();
            loadComptes();
            setupButtons();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de connexion", "Impossible de se connecter à la base de données");
        }
    }

    private void setupComboBox() {
        ObservableList<Client> clients = FXCollections.observableArrayList(clientService.getAllClients());
        comboClient.setItems(clients);
        comboClient.setConverter(new StringConverter<>() {
            @Override
            public String toString(Client client) {
                return client != null ? client.getNom() + " " + client.getPrenom() : "";
            }

            @Override
            public Client fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });
    }

    private void setupTableColumns() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colDateCreation.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .format(dateFormatter)
                )
        );
        colClient.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> {
                            Client client = cellData.getValue().getClient();
                            return client.getNom() + " " + client.getPrenom();
                        }
                )
        );
    }

    private void loadComptes() {
        comptesList = FXCollections.observableArrayList(compteService.getAllComptes());
        tableComptes.setItems(comptesList);
    }

    private void setupButtons() {
        btnCreer.setOnAction(e -> handleCreerCompte());
        btnFermer.setOnAction(e -> handleFermerCompte());
    }

    private void handleCreerCompte() {
        if (validateInputs()) {
            Client selectedClient = comboClient.getValue();
            double soldeInitial = Double.parseDouble(txtSoldeInitial.getText());

            Compte newCompte = new Compte();
            newCompte.setBalance(soldeInitial);
            newCompte.setClient(selectedClient);

            try {
                Compte savedCompte = compteService.createCompte(newCompte, selectedClient.getId());
                comptesList.add(savedCompte);
                clearInputs();
                AlertUtil.showSuccess("Compte créé",
                        String.format("Le compte %s a été créé avec succès", savedCompte.getNumero()));
            } catch (Exception e) {
                AlertUtil.showError("Erreur", "Impossible de créer le compte");
            }
        }
    }

    private void handleFermerCompte() {
        Compte selectedCompte = tableComptes.getSelectionModel().getSelectedItem();
        if (selectedCompte != null) {
            if (AlertUtil.showConfirmation("Fermer le compte",
                    String.format("Êtes-vous sûr de vouloir fermer le compte %s ?", selectedCompte.getNumero()))) {
                try {
                    compteService.closeCompte(selectedCompte.getNumero());
                    comptesList.remove(selectedCompte);
                    AlertUtil.showSuccess("Compte fermé",
                            String.format("Le compte %s a été fermé avec succès", selectedCompte.getNumero()));
                } catch (Exception e) {
                    AlertUtil.showError("Erreur", "Impossible de fermer le compte");
                }
            }
        } else {
            AlertUtil.showError("Sélection requise", "Veuillez sélectionner un compte");
        }
    }

    private boolean validateInputs() {
        if (comboClient.getValue() == null) {
            AlertUtil.showError("Client requis", "Veuillez sélectionner un client");
            return false;
        }

        try {
            double solde = Double.parseDouble(txtSoldeInitial.getText());
            if (solde < 0) {
                AlertUtil.showError("Solde invalide", "Le solde initial ne peut pas être négatif");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Format invalide", "Le solde initial doit être un nombre valide");
            return false;
        }

        return true;
    }

    private void clearInputs() {
        comboClient.setValue(null);
        txtSoldeInitial.clear();
        tableComptes.getSelectionModel().clearSelection();
    }
}
