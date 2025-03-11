package com.banking.digitalbankingproject.controller;


import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OperationController {

    @FXML
    private Text clientBenTXT;
    @FXML
    private Text clientR;
    @FXML
    private Text clientV;
    @FXML
    private Text comTXT;
    @FXML
    private Text dErreur;
    @FXML
    private Text erreur;
    @FXML
    private Text erreur1;
    @FXML
    private Text erreur11;
    @FXML
    private Pane infoPANE;
    @FXML
    private Pane infoPANE1;
    @FXML
    private Pane infoVirPANE;
    @FXML
    private Text numCompteBen;
    @FXML
    private Text numCompteOp;
    @FXML
    private Text numCompteTXT;
    @FXML
    private Text numCompteTXT1;
    @FXML
    private Text rErreur;
    @FXML
    private Text rErreur1;
    @FXML
    private Pane rercheComptePANE;
    @FXML
    private Pane rercheComptePANE1;
    @FXML
    private Pane rercheCompteVirPANE;
    @FXML
    private Pane retraitPANE;
    @FXML
    private Pane depotPANE;
    @FXML
    private TextField retraitTFD;
    @FXML
    private Text soldeOpTXT;
    @FXML
    private Text clientDTXT;
    @FXML
    private Text soldeTXT;
    @FXML
    private Text soldeTXT1;
    @FXML
    private Pane virementPANE;
    @FXML
    private TextField virementTFD;
    @FXML
    private TextField depotTFD;
    @FXML
    private TextField rechercheTFD3;
    @FXML
    private TextField rechercheTFD1;
    @FXML
    private TextField rechercheTFD2;
    @FXML
    private TableView<Operation> opTableView;
    @FXML
    private TableColumn<Operation, String> compteCol;
    @FXML
    private TableColumn<Operation, Timestamp> dateCol;
    @FXML
    private TableColumn<Operation, Integer> idCol;
    @FXML
    private TableColumn<Operation, Double> montantCol;
    @FXML
    private TableColumn<Operation, TypeOperation> typeCol;
    @FXML
    private Pane entetePANE;
    @FXML
    private TextField filtreTFD;
    private FilteredList<Operation> listefiltrer;

    Compte compteCherche = null;
    Compte compteVir = null;
    List<Operation> operationList = new ArrayList<>();


    @FXML
    void Accueil(ActionEvent event) throws IOException {
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");

    }
    public void initialize(){
        depotPANE.setVisible(false);
        virementPANE.setVisible(false);
        retraitPANE.setVisible(false);
        opTableView.setVisible(true);
        entetePANE.setVisible(true);

        operationList = new OperationImpl().getAllOperations();
        if(!operationList.isEmpty()){
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            montantCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("dateOp"));
            compteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompte().getNumero()));

            listefiltrer = new FilteredList<>(FXCollections.observableList(operationList), p->true);
            opTableView.setItems(listefiltrer);

            filtreTFD.textProperty().addListener((observable, olValue, newValue) -> {
                filtrerOperations(newValue);
            } );
        }

    }

    @FXML
    void Depot(ActionEvent event) {
        erreur.setVisible(false);
        dErreur.setVisible(false);
        Compte Cc = CompteController.CompteSelectionne;
        depotPANE.setVisible(true);
        virementPANE.setVisible(false);
        retraitPANE.setVisible(false);
        opTableView.setVisible(false);
        entetePANE.setVisible(false);
        ControleDeValeur(depotTFD);
        if(Cc != null){
            rercheComptePANE.setVisible(false);
            infoPANE.setVisible(true);
            soldeTXT.setText("Solde: "+Cc.getBalance()+" F");
            numCompteTXT.setText("Num Compte: "+Cc.getNumero());
            clientDTXT.setText("Nom: "+Cc.getClient().getNom()+"\nPrenom: "+Cc.getClient().getPrenom()+"\nEmail: "+Cc.getClient().getEmail());
        } else if ( compteCherche != null) {
            rercheComptePANE.setVisible(false);
            infoPANE.setVisible(true);
            soldeTXT.setText("Solde: "+compteCherche.getBalance()+" F");
            numCompteTXT.setText("Num Compte: "+compteCherche.getNumero());
            clientDTXT.setText("Nom: "+compteCherche.getClient().getNom()+"\nPrenom: "+compteCherche.getClient().getPrenom()+"\nEmail: "+compteCherche.getClient().getEmail());

        } else {
            rercheComptePANE.setVisible(true);
        }

    }
    @FXML
    void Deposer(ActionEvent event){

        if (Double.parseDouble(depotTFD.getText()) >= 1000){
            Compte Cc = CompteController.getCompteSelectionne();
            ICompte iCompte = new CompteImpl();
            Operation operation = new Operation();
            IOperation iOperation = new OperationImpl();
            operation.setCompte(Cc != null ?Cc: compteCherche);
            operation.setAmount(Double.parseDouble(depotTFD.getText()));
            operation.setType(TypeOperation.DEPOT);
            Compte compte = operation.getCompte();
            compte.setBalance(compte.getBalance() + operation.getAmount());
            depotTFD.setText("");
            boolean ok = iOperation.createOperation(operation);
            if (ok){
                Notification.NotifSuccess("Success", "Depot éffectué avec succes");
                iCompte.UpdateCompte(compte);
            }else {
                Notification.NotifError("Error", "Erreur de depot");
            }
            initialize();
        }else {
            Notification.NotifError("Erreur", "Veillez saisir un montant surperrieur à 1 000F");
            dErreur.setVisible(true);
        }

    }

    @FXML
    void Retrait(ActionEvent event) {
        Compte Cc = CompteController.CompteSelectionne;
        depotPANE.setVisible(false);
        virementPANE.setVisible(false);
        retraitPANE.setVisible(true);
        opTableView.setVisible(false);
        entetePANE.setVisible(false);
        rErreur.setVisible(false);
        erreur1.setVisible(false);
        ControleDeValeur(retraitTFD);
        if(Cc != null ){
            rercheComptePANE1.setVisible(false);
            infoPANE1.setVisible(true);
            soldeTXT1.setText("Solde: "+Cc.getBalance()+" F");
            numCompteTXT1.setText("Num Compte: "+Cc.getNumero());
            clientR.setText("Nom: "+Cc.getClient().getNom()+"\nPrenom: "+Cc.getClient().getPrenom()+"\nEmail: "+Cc.getClient().getEmail());
        } else if (compteCherche != null) {
            rercheComptePANE1.setVisible(false);
            infoPANE1.setVisible(true);
            soldeTXT1.setText("Solde: "+compteCherche.getBalance()+" F");
            numCompteTXT1.setText("Num Compte: "+compteCherche.getNumero());
            clientR.setText("Nom: "+compteCherche.getClient().getNom()+"\nPrenom: "+compteCherche.getClient().getPrenom()+"\nEmail: "+compteCherche.getClient().getEmail());

        } else {
            rercheComptePANE1.setVisible(true);
        }

    }

    @FXML
    void Retitrer(ActionEvent event) {
        if (Double.parseDouble(retraitTFD.getText()) >= 1000){
            Compte Cc = CompteController.getCompteSelectionne();
            ICompte iCompte = new CompteImpl();
            Operation operation = new Operation();
            IOperation iOperation = new OperationImpl();

            operation.setCompte(Cc != null ?Cc: compteCherche);
            operation.setAmount(-Double.parseDouble(retraitTFD.getText()));
            operation.setType(TypeOperation.RETRAIT);
            Compte compte = operation.getCompte();
            if(-operation.getAmount() <= compte.getBalance()){
                compte.setBalance(compte.getBalance() + operation.getAmount());
                retraitTFD.setText("");

                boolean ok = iOperation.createOperation(operation);
                if (ok){
                    Notification.NotifSuccess("Success", "Retrait éffectué avec succes");
                    iCompte.UpdateCompte(compte);
                }else {
                    Notification.NotifError("Error", "Erreur de Retrait");
                }
                initialize();
            }else {
                rErreur.setText("Solde Insuffisant !!");
                rErreur.setVisible(true);
                Notification.NotifError("Erreur", "Vous ne disposez pas de ce solde");
            }

        }else {
            Notification.NotifError("Erreur", "Veillez saisir un montant surperrieur à 1 000F");
            rErreur.setVisible(true);
            rErreur.setText("Retirer Minimum 1 000F !!");
        }

    }

    @FXML
    void Virement(ActionEvent event) {
        Compte Cc = CompteController.getCompteSelectionne();
        depotPANE.setVisible(false);
        retraitPANE.setVisible(false);
        virementPANE.setVisible(true);
        opTableView.setVisible(false);
        entetePANE.setVisible(false);
        erreur11.setVisible(false);
        ControleDeValeur(virementTFD);
        if(Cc != null ){
            if(compteVir != null){
                infoVirPANE.setVisible(true);
                soldeOpTXT.setText("Solde: "+Cc.getBalance()+" F");
                numCompteOp.setText("Num Compte: "+Cc.getNumero());
                clientV.setText("Nom: "+Cc.getClient().getNom()+"\nPrenom: "+Cc.getClient().getPrenom()+"\nEmail: "+Cc.getClient().getEmail());
                numCompteBen.setText("Num Compte: "+compteVir.getNumero());
                clientBenTXT.setText("Nom: "+compteVir.getClient().getNom()+"\nPrenom: "+compteVir.getClient().getPrenom()+"\nEmail: "+compteVir.getClient().getEmail());
            }else {
                comTXT.setText("Compte du Beneficiaire");
                rercheCompteVirPANE.setVisible(true);
            }
        } else if (compteCherche != null) {
            if (compteVir != null){
                infoVirPANE.setVisible(true);
                soldeOpTXT.setText("Solde: "+compteCherche.getBalance()+" F");
                numCompteOp.setText("Num Compte: "+compteCherche.getNumero());
                clientV.setText("Nom: "+compteCherche.getClient().getNom()+"\nPrenom: "+compteCherche.getClient().getPrenom()+"\nEmail: "+compteCherche.getClient().getEmail());
                numCompteBen.setText("Num Compte: "+compteVir.getNumero());
                clientBenTXT.setText("Nom: "+compteVir.getClient().getNom()+"\nPrenom: "+compteVir.getClient().getPrenom()+"\nEmail: "+compteVir.getClient().getEmail());
            }else {
                comTXT.setText("Compte du Beneficiaire");
                rercheCompteVirPANE.setVisible(true);
            }
        }else {
            rercheCompteVirPANE.setVisible(true);
        }

    }

    @FXML
    void Virer(ActionEvent event) {
        Compte Cc = CompteController.getCompteSelectionne() != null? CompteController.getCompteSelectionne(): compteCherche;
        //ControleDeValeur(virementTFD);
        if(Double.parseDouble(virementTFD.getText()) >= 5){
            if(Double.parseDouble(virementTFD.getText()) <= Cc.getBalance()){
                IOperation operation = new OperationImpl();
                ICompte compte = new CompteImpl();
                boolean ok, ok2;

                //Retrait
                Operation retrait = new Operation();
                retrait.setType(TypeOperation.RETRAIT);
                retrait.setCompte(Cc);
                retrait.setAmount(-Double.parseDouble(virementTFD.getText()));
                Cc.setBalance(Cc.getBalance() + retrait.getAmount());
                ok = operation.createOperation(retrait);
                ok2 = compte.UpdateCompte(Cc);

                if (ok && ok2){
                    //Depot
                    Operation depot = new Operation();
                    depot.setAmount(Double.parseDouble(virementTFD.getText()));
                    depot.setType(TypeOperation.DEPOT);
                    depot.setCompte(compteVir);
                    compteVir.setBalance(compteVir.getBalance() + depot.getAmount());
                    ok = operation.createOperation(depot);
                    ok2 = compte.UpdateCompte(compteVir);
                    if (ok && ok2){
                        Notification.NotifSuccess("Success","Virement Effectué !!");
                        initialize();
                    }
                }else {
                    Notification.NotifError("Erreur", "Erreur de transfert");
                    return;
                }

            }else {
                Notification.NotifError("Erreur", "Solde insuffisant !!");
                rErreur1.setText("Solde insuffisant !!");
                rErreur1.setVisible(true);
                return;
            }
        }else {
            Notification.NotifError("Erreur", "Saisir un montant Minimum de 5 F !!");
            rErreur1.setText("Minimum 5 F !!");
            rErreur1.setVisible(true);
            return;
        }

    }


    @FXML
    void TrouveCompte(ActionEvent event) {
        String recherche = "";
        if (rercheComptePANE.isVisible() && depotPANE.isVisible()){
            if(rechercheTFD1.getText().isEmpty()){
                Notification.NotifError("Erreur", "Veillez saisir un numero de compte");
            }else {
                recherche = rechercheTFD1.getText();
                rechercheTFD1.setText("");
                if (!recherche.isEmpty()){
                    compteCherche = chercher(recherche);
                    if (compteCherche != null){
                        rercheComptePANE.setVisible(false);
                        Depot(event);
                        erreur.setVisible(false);
                    }else {
                        erreur.setVisible(true);
                    }
                }
            }
        }else if (rercheComptePANE1.isVisible() && retraitPANE.isVisible()){
            if (rechercheTFD2.getText().isEmpty()){
                Notification.NotifError("Erreur", "Veillez saisir un numero de compte");
            }else {
                recherche = rechercheTFD2.getText();
                rechercheTFD2.setText("");
                if (!recherche.isEmpty()){
                    compteCherche = chercher(recherche);
                    if (compteCherche != null){
                        rercheComptePANE1.setVisible(false);
                        erreur1.setVisible(false);
                        Retrait(event);
                    }else {
                        erreur1.setVisible(true);
                    }
                }
            }
        }else if(rercheCompteVirPANE.isVisible() && virementPANE.isVisible()){
            if (rechercheTFD3.getText().isEmpty()){
                Notification.NotifError("Erreur", "Veillez saisir un numero de compte");
            }else {
                recherche = rechercheTFD3.getText();
                rechercheTFD3.setText("");
                if (!recherche.isEmpty()){
                    if (compteCherche == null && CompteController.CompteSelectionne == null){
                        compteCherche = chercher(recherche);
                    }else {
                        if((compteCherche != null &&!recherche.equals(compteCherche.getNumero())) ||(CompteController.CompteSelectionne != null && !recherche.equals(CompteController.CompteSelectionne.getNumero()))){
                            compteVir = chercher(recherche);
                        }else {
                            Notification.NotifError("Erreur", "Impossible de faire le virement sur le même compte");
                            erreur11.setText("Impossible de selectionner le même compte");
                            erreur11.setVisible(true);
                            return;
                        }

                    }
                    if (compteCherche != null || compteVir != null){
                        rercheCompteVirPANE.setVisible(false);
                        Virement(event);
                        erreur11.setVisible(false);
                    }else {
                        erreur11.setText("Erreur!! Compte non trouvé");
                        erreur11.setVisible(true);
                    }
                }
            }
        }
    }

    private Compte chercher(String recherche){

        if (!recherche.isEmpty()){
            Compte compte = new CompteImpl().GetCompteByNumCompte(recherche);
            if (compte.getNumero() != null){
                return compte;
            }else {
                Notification.NotifError("Erreur", "Numero de carte non Valide");
            }
        }
        return null;
    }
    private void ControleDeValeur(TextField tfd){
        TextFormatter<Double> formatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")){
                return change;
            }else {
                return null;
            }
        });
        tfd.setTextFormatter(formatter);
    }
    public void filtrerOperations(String recherche){
        listefiltrer.setPredicate(operation -> {
            if (recherche == null || recherche.isEmpty()){
                return true;
            }
            if (operation.getType().toString().toLowerCase().contains(recherche.toLowerCase())){
                return true;
            }
            if (operation.getCompte().getNumero().toLowerCase().contains(recherche.toLowerCase())) {
                return true;

            }
            return false;
        });
    }
}
