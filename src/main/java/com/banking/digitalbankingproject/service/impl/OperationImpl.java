package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private Db db = new Db();
    private int ok;
    private ResultSet resultSet;
    private List<Operation> operationList;
    @Override
    public boolean createOperation(Operation operation) {
        String sql = "INSERT INTO operations(amount, type, compte_id) VALUES(?,?,?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, operation.getAmount());
            db.getPstm().setString(2, operation.getType().toString());
            db.getPstm().setInt(3, operation.getCompte().getId());
            ok = db.executeMaj();
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok == 1;
    }

    @Override
    public List<Operation> getAllOperations() {
        String sql = "SELECT * FROM operations";
        operationList = new ArrayList<>();
        try {
            db.initPrepar(sql);
            resultSet = db.executeSelect();
            while (resultSet.next()){
                Operation op = new Operation();
                op.setType(TypeOperation.valueOf (resultSet.getString("type")));
                CompteImpl ic = new CompteImpl();
                op.setCompte(ic.GetCompeById(resultSet.getInt("compte_id")));
                op.setId(resultSet.getInt("id"));
                op.setAmount(resultSet.getDouble("amount"));
                op.setDateOp(resultSet.getTimestamp("date_op"));
                operationList.add(op);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return operationList;
    }

    @Override
    public List<Operation> getOperationsByIdCompte(int id_compte) {
        String sql = "SELECT * FROM operations WHERE compte_id = ?";
        operationList = new ArrayList<>();
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1,id_compte);
            resultSet = db.executeSelect();
            while (resultSet.next()){
                Operation op = new Operation();
                op.setType(TypeOperation.valueOf (resultSet.getString("type")));
                CompteImpl ic = new CompteImpl();
                op.setCompte(ic.GetCompeById(resultSet.getInt("compte_id")));
                op.setId(resultSet.getInt("id"));
                op.setAmount(resultSet.getDouble("amount"));
                op.setDateOp(resultSet.getTimestamp("date_op"));
                operationList.add(op);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return operationList;
    }
}
