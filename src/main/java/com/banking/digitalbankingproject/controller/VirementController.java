package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class VirementController {

        @FXML
        private TextField id_c;

        @FXML
        private Text num_crediteur;

        @FXML
        private Text num_debiteur;

        @FXML
        private TextField solde;
        Compte compte;
        Compte compte2;

        public void initialize() {
                compte2 = CompteController.getCompteselect();;
        }

        @FXML
        void rechercher_compte(ActionEvent event) {
                if (id_c.getText().equals("")) {
                        return;
                }else {
                        //compte = new Compte();
                        compte = new CompteImpl().getCompte(id_c.getText());

                        if (compte.getNumero() != null) {
                                num_crediteur.setText("Numero compte crediteur: "+compte2.getNumero());
                                num_debiteur.setText("Numero compte Debiteur: "+compte.getNumero());

                        }else {
                                num_crediteur.setText("");
                                Notification.NotifError("Erreur", "Compte non valide");
                        }
                }

        }

        @FXML
        void valider(ActionEvent event) {
                if (compte.getNumero() != null) {
                        try {
                                IOperation op = new OperationImpl();
                                Double mon = Double.parseDouble(solde.getText());
                                Operation retrait = new Operation();
                                retrait.setCompte(compte2);
                                retrait.setAmount(mon);
                                retrait.setType(TypeOperation.RETRAIT);
                                boolean ok = op.CreateOperation(retrait);
                                if (ok) {
                                        Operation depot = new Operation();
                                        depot.setCompte(compte);
                                        depot.setAmount(mon);
                                        depot.setType(TypeOperation.DEPOT);
                                       ok = op.CreateOperation(depot);
                                }if (ok){
                                        Notification.NotifSuccess("success", "Operation valide");
                                        Outils.load(event, "Historrique", "/FXML/Historique.fxml");
                                }

                        }catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }


}
