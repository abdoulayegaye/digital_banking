package com.banking.digitalbankingproject.util;

import com.banking.digitalbankingproject.entity.Compte;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class CallbackFactory {

    public static Callback<ListView<Compte>, ListCell<Compte>> getCompteListCellFactory() {
        return param -> new ListCell<>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null) {
                    setText(null);
                } else {
                    setText(compte.getNumero() + " - " + (compte.isActif() ? "Actif" : "Fermé"));
                }
            }
        };
    }
}