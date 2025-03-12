package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public int deposit(String accountNumber, double amount) {
        CompteImpl compteDao = new CompteImpl();
        Compte compte = compteDao.getCompteByNumero(accountNumber);
        if (compte == null) {
            throw new IllegalArgumentException("Compte introuvable.");
        }

        Operation operation = new Operation();
        operation.setDateOp(Instant.now());
        operation.setAmount(amount);
        operation.setType(TypeOperation.DEPOT);
        operation.setCompte(compte);

        String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, Timestamp.from(operation.getDateOp()));
            db.getPstm().setDouble(2, operation.getAmount());
            db.getPstm().setString(3, operation.getType().name());
            db.getPstm().setInt(4, operation.getCompte().getId());

            ok = db.executeMaj();
            compte.setBalance(compte.getBalance() + amount);
            compteDao.updateCompte(compte);

            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return ok;
    }

    @Override
    public int withdraw(String accountNumber, double amount) {
        CompteImpl compteDao = new CompteImpl();
        Compte compte = compteDao.getCompteByNumero(accountNumber);
        if (compte == null) {
            throw new IllegalArgumentException("Compte introuvable.");
        }
        if (compte.getBalance() < amount) {
            Notification notification = new Notification();
            Notification.NotifError("error", "Solde Insuffisant");
            throw new IllegalArgumentException("Solde insuffisant.");
        }


        Operation operation = new Operation();
        operation.setDateOp(Instant.now());
        operation.setAmount(amount);
        operation.setType(TypeOperation.RETRAIT);
        operation.setCompte(compte);

        String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, Timestamp.from(operation.getDateOp()));
            db.getPstm().setDouble(2, operation.getAmount());
            db.getPstm().setString(3, operation.getType().name());
            db.getPstm().setInt(4, operation.getCompte().getId());

            ok = db.executeMaj();
            compte.setBalance(compte.getBalance() - amount);
            compteDao.updateCompte(compte);

            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return ok;
    }

    @Override
    public int transfer(String sourceAccount, String destAccount, double amount) {
        try {

            int withdrawResult = withdraw(sourceAccount, amount);
            if (withdrawResult <= 0) return 0;

            int depositResult = deposit(destAccount, amount);
            if (depositResult <= 0) return 0;

            CompteImpl compteDao = new CompteImpl();
            Compte compteSource = compteDao.getCompteByNumero(sourceAccount);

            Operation operation = new Operation();
            operation.setDateOp(Instant.now());
            operation.setAmount(amount);
            operation.setType(TypeOperation.VIREMENT);
            operation.setCompte(compteSource);

            String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, ?)";
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, Timestamp.from(operation.getDateOp()));
            db.getPstm().setDouble(2, operation.getAmount());
            db.getPstm().setString(3, operation.getType().name());
            db.getPstm().setInt(4, operation.getCompte().getId());
            int result = db.executeMaj();

            db.closeConnection();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Operation> getOperationsByAccount(String accountNumber) {
        List<Operation> operations = new ArrayList<>();
        CompteImpl compteDao = new CompteImpl();
        Compte compte = compteDao.getCompteByNumero(accountNumber);

        if (compte == null) {
            throw new IllegalArgumentException("Compte introuvable.");
        }

        String sql = "SELECT * FROM operations WHERE compte_id = ? ORDER BY date_op DESC";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compte.getId());
            rs = db.executeSelect(sql);

            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op").toInstant());
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                operation.setCompte(compte);
                operations.add(operation);
            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

    @Override
    public List<Operation> getAllOperations() {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations ORDER BY date_op DESC";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect(sql);

            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op").toInstant());
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                int compteId = rs.getInt("compte_id");
                CompteImpl compteDao = new CompteImpl();
                operation.setCompte(compteDao.getCompteById(compteId));

                operations.add(operation);
            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }


}