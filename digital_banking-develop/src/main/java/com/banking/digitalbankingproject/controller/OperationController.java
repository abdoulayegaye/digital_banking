package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class OperationController implements Initializable {
    private Db db = new Db();

    @FXML
    private TableColumn<Operation, Double> AMOUNTCol;

    @FXML
    private TextField AmountTfd;

    @FXML
    private TableColumn<Operation, Integer> COMPTE_IDCol;

    @FXML
    private TextField Compte_idTfd;

    @FXML
    private Button ConsulterCol;

    @FXML
    private TableColumn<Operation, String> DATE_OPCol;

    @FXML
    private DatePicker Date_opDtP;

    @FXML
    private Button DepotTfd;

    @FXML
    private Button GenererBtn;

    @FXML
    private Button RetraitBtn;

    @FXML
    private TableColumn<Operation, String> TYPECol;

    @FXML
    private TableColumn<Operation, Integer> IDCol;

    @FXML
    private TableView<Operation> operationTb;

    @FXML
    private TextField TypeTfd;

    @FXML
    private Button VirerBtn;

    @FXML
    private ComboBox<Compte> compteCbB;

    @FXML
    public void depot(ActionEvent actionEvent) {
        String sql = "INSERT INTO operations(date_op, amount, type, compte_id) VALUES(?, ?, ?, ?)";

        if (compteCbB.getValue() == null) {
            System.out.println("Veuillez sélectionner un compte.");
            return;
        }

        int selectedCompte = compteCbB.getValue().getId();

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, String.valueOf(Date_opDtP.getValue()));
            db.getPstm().setDouble(2, Double.parseDouble(AmountTfd.getText()));
            db.getPstm().setString(3, TypeTfd.getText());
            db.getPstm().setInt(4, selectedCompte);

            int rowsAffected = db.executeMaj();
            db.closeConnection();

            if (rowsAffected > 0) {
                System.out.println("Dépôt effectué avec succès !");
            } else {
                System.out.println("Échec du dépôt.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur SQL lors du dépôt : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Montant invalide. Veuillez entrer un nombre valide.");
        }
    }

    public void loadComboBox() {
        if (compteCbB != null) {
            ObservableList<Compte> comptes = getComptesFromDatabase();
            compteCbB.setItems(comptes);
        } else {
            System.err.println("Le ComboBox (compteCbB) est nul.");
        }
    }

    private ObservableList<Compte> getComptesFromDatabase() {
        ObservableList<Compte> comptes = FXCollections.observableArrayList();
        String sql = "SELECT * FROM comptes";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                comptes.add(compte);
            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comptes;
    }

    public void loadTableOperation() {
        ObservableList<Operation> listeOperation = FXCollections.observableArrayList(getOperations());
        operationTb.setItems(listeOperation);

        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        DATE_OPCol.setCellValueFactory(new PropertyValueFactory<>("date_op"));
        AMOUNTCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TYPECol.setCellValueFactory(new PropertyValueFactory<>("type"));
        COMPTE_IDCol.setCellValueFactory(new PropertyValueFactory<>("compte_id"));
    }

    public List<Operation> getOperations() {
        ObservableList<Operation> operations = FXCollections.observableArrayList();
        String sql = "SELECT * FROM operations";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Operation o = new Operation();
                o.setId(rs.getInt("id"));
                o.setDate_op(rs.getString("date_op"));
                o.setAmount(rs.getDouble("amount"));
                o.setType(TypeOperation.valueOf(rs.getString("type")));
                o.setCompte_id(String.valueOf(rs.getInt("compte_id")));
                operations.add(o);
            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBox();
        loadTableOperation();
    }
}
