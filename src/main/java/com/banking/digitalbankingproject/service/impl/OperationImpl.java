package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private Db db = new Db();
    private ResultSet rs ;
    int ok ;

    @Override
    public List<Operation> getAllOperations() {
        List<Operation> operations = new ArrayList<Operation>();
        String sql = "select * from operations";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()){
                Operation operation = new Operation();
                ICompte compte = new CompteImpl();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op"));
                operation.setAmount(rs.getInt("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                operation.setCompte(compte.getCompteById(rs.getInt("compte_id")));
                operations.add(operation);
            }
            db.closeConnection();
        }catch(Exception e) {
            e.printStackTrace();

        }

        return operations;
    }

    @Override
    public boolean CreateOperation(Operation operation) {
        String sql = "INSERT INTO operations (amount, type, compte_id) VALUES ( ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, operation.getAmount());
            db.getPstm().setString(2, operation.getType().name());
            db.getPstm().setLong(3, operation.getCompte().getId());
            ok=db.getPstm().executeUpdate();

            int result = db.getPstm().getUpdateCount();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Operation> getOperationByCompteId(int CompteId) {
        List<Operation> operations = new ArrayList<Operation>();
        String sql = "select * from operations WHERE compte_id=?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, CompteId);
            rs = db.executeSelect();
            while (rs.next()){
                Operation operation = new Operation();
                ICompte compte = new CompteImpl();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op"));
                operation.setAmount(rs.getInt("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                operation.setCompte(compte.getCompteById(rs.getInt("compte_id")));
                operations.add(operation);
            }
            db.closeConnection();
        }catch(Exception e) {
            e.printStackTrace();

        }

        return operations;
    }

    @Override
    public boolean retraitOperation(Operation operation) {
        String sql = "UPDATE comptes SET solde = solde - ? WHERE id = ? AND solde >= ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, java.sql.Timestamp.from(Instant.now()));
            db.getPstm().setDouble(2, operation.getAmount());
            db.getPstm().setString(3, TypeOperation.DEPOT.name());
            ok=db.getPstm().executeUpdate();

            int result = db.getPstm().getUpdateCount();
            if (result > 0) {
               // return depotOperation(new Operation(0, Instant.now(), -operation.getAmount(), TypeOperation.RETRAIT, operation.getCompte()));
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean historiqueOperation(Operation operation) {
        return false;
    }

    @Override
    public boolean virementOperation(Operation operation) {
        return false;
    }


    }

