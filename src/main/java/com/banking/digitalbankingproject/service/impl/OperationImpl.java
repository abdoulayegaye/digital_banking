package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private int id;

    private List<Operation> operations = new ArrayList<>();

    @Override
    public boolean depot(int compteId, double montant) {
        boolean ok = false;
        String sqlUpdate = "UPDATE comptes SET balance = balance + ? WHERE id = ?";
        String sqlInsert = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (NOW(), ?, ?, ?)";

        try {
            //Mettre à jour le solde du compte
            db.initPrepar(sqlUpdate);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteId);
            int updateResult = db.executeMaj();

            if (updateResult > 0) {
                //Insérer l'opération dans la table des operations
                db.initPrepar(sqlInsert);
                db.getPstm().setDouble(1, montant);
                db.getPstm().setString(2, "DEPOT");
                db.getPstm().setInt(3, compteId);
                int insertResult = db.executeMaj();

                if (insertResult > 0) {
                    ok = true;
                }
            }

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean retrait(int compteId, double montant) {
        boolean ok = false;
        String sqlUpdate = "UPDATE comptes SET balance = balance - ? WHERE id = ?";
        String sqlInsert = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (NOW(), ?, ?, ?)";

        try {
            String sqlCheck = "SELECT balance FROM comptes WHERE id = ?";
            db.initPrepar(sqlCheck);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance < montant) {
                    System.out.println("Fonds insuffisants !");
                    return false;
                }
            }

            db.initPrepar(sqlUpdate);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteId);
            int updateResult = db.executeMaj();

            if (updateResult > 0) {
                db.initPrepar(sqlInsert);
                db.getPstm().setDouble(1, montant);
                db.getPstm().setString(2, "RETRAIT");
                db.getPstm().setInt(3, compteId);
                int insertResult = db.executeMaj();

                if (insertResult > 0) {
                    ok = true;
                }
            }

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean virement(int compteSourceId, int compteDestinationId, double montant) {
        boolean ok = false;

        String sqlCheck = "SELECT balance FROM comptes WHERE id = ?";
        String sqlUpdateDebit = "UPDATE comptes SET balance = balance - ? WHERE id = ?";
        String sqlUpdateCredit = "UPDATE comptes SET balance = balance + ? WHERE id = ?";
        String sqlInsert = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (NOW(), ?, ?, ?)";

        try {
            // Vérifier si le compte source a assez de fonds
            db.initPrepar(sqlCheck);
            db.getPstm().setInt(1, compteSourceId);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance < montant) {
                    return false;
                }
            } else {
                return false;
            }

            // Débiter le compte source
            db.initPrepar(sqlUpdateDebit);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteSourceId);
            int debitResult = db.executeMaj();

            // Créditer le compte destination
            db.initPrepar(sqlUpdateCredit);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteDestinationId);
            int creditResult = db.executeMaj();

            // Enregistrer l'opération pour le compte source
            db.initPrepar(sqlInsert);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, "VIREMENT_SORTANT");
            db.getPstm().setInt(3, compteSourceId);
            int insertDebit = db.executeMaj();

            // Enregistrer l'opération pour le compte destination
            db.initPrepar(sqlInsert);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, "VIREMENT_ENTRANT");
            db.getPstm().setInt(3, compteDestinationId);
            int insertCredit = db.executeMaj();

            // Vérification de la réussite
            if (debitResult > 0 && creditResult > 0 && insertDebit > 0 && insertCredit > 0) {
                ok = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }

    @Override
    public List<Operation> consulterHistorique(int compteId) {
        return List.of();
    }

    @Override
    public List<Operation> consulterHistorique(int compteId, String dateDebut, String dateFin) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT o.*, c.numero FROM operations o " +
                "JOIN comptes c ON o.compte_id = c.id " +
                "WHERE o.compte_id = ? " +
                "AND o.date_op >= ?::timestamp " +
                "AND o.date_op <= ?::timestamp " +
                "ORDER BY o.date_op ASC";

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            db.getPstm().setString(2, dateDebut);
            db.getPstm().setString(3, dateFin);
            rs = db.executeSelect();

        while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op").toInstant());
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));

                Compte compte = new Compte();
                compte.setNumero(rs.getString("numero"));
                operation.setCompte(compte);

                operations.add(operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return operations;
    }

}
