package com.example.projet_java_fx.service.impl;

import com.example.projet_java_fx.database.Db;
import com.example.projet_java_fx.entity.Operation;
import com.example.projet_java_fx.enums.TypeOperation;
import com.example.projet_java_fx.service.IOperation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    @Override
    public int effectuerVirement(String numeroCompteSource, String numeroCompteDestination, double montant) {
        String sqlDebit = "UPDATE Comptes SET solde = solde - ? WHERE numero = ? AND solde >= ?";
        String sqlCredit = "UPDATE Comptes SET solde = solde + ? WHERE numero = ?";
        int okDebit = 0;
        int okCredit = 0;

        try {
            // Début de la transaction
            db.getConnection().setAutoCommit(false);

            // Débiter le compte source
            db.initPrepar(sqlDebit);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, numeroCompteSource);
            db.getPstm().setDouble(3, montant);
            okDebit = db.executeMaj();

            if (okDebit > 0) {
                // Créditer le compte destination
                db.initPrepar(sqlCredit);
                db.getPstm().setDouble(1, montant);
                db.getPstm().setString(2, numeroCompteDestination);
                okCredit = db.executeMaj();

                if (okCredit > 0) {
                    // Enregistrer la transaction pour le débit
                    enregistrerTransaction(numeroCompteSource, montant, TypeOperation.RETRAIT);
                    // Enregistrer la transaction pour le crédit
                    enregistrerTransaction(numeroCompteDestination, montant, TypeOperation.VERSEMENT);

                    // Commit de la transaction
                    db.getConnection().commit();
                } else {
                    // Rollback en cas d'échec du crédit
                    db.getConnection().rollback();
                }
            } else {
                // Rollback en cas d'échec du débit
                db.getConnection().rollback();
            }
        } catch (Exception e) {
            try {
                // Rollback en cas d'exception
                db.getConnection().rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                // Rétablir l'auto-commit
                db.getConnection().setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return (okDebit > 0 && okCredit > 0) ? 1 : 0;
    }

    @Override
    public int effectuerDepot(String numeroCompte, double montant) {
        String sql = "UPDATE Comptes SET solde = solde + ? WHERE numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, numeroCompte);
            ok = db.executeMaj();

            if (ok > 0) {
                // Enregistrer la transaction
                enregistrerTransaction(numeroCompte, montant, TypeOperation.VERSEMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int effectuerRetrait(String numeroCompte, double montant) {
        String sql = "UPDATE Comptes SET solde = solde - ? WHERE numero = ? AND solde >= ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, numeroCompte);
            db.getPstm().setDouble(3, montant);
            ok = db.executeMaj();

            if (ok > 0) {
                // Enregistrer la transaction
                enregistrerTransaction(numeroCompte, montant, TypeOperation.RETRAIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }


    @Override
    public List<Operation> consulterHistorique(String numeroCompte) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT o.* FROM Operations o JOIN Comptes c ON o.compte_id = c.id WHERE c.numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numeroCompte);
            rs = db.executeSelect();
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op"));
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                operations.add(operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return operations;
    }

    private void enregistrerTransaction(String numeroCompte, double montant, TypeOperation type) {
        String sql = "INSERT INTO Operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, (SELECT id FROM Comptes WHERE numero = ?))";
        try {
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            db.getPstm().setDouble(2, montant);
            db.getPstm().setString(3, type.toString());
            db.getPstm().setString(4, numeroCompte);
            db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}