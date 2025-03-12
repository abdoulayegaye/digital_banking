package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private final Db db = new Db();
    private final ICompte compteService = new CompteImpl();

    @Override
    public boolean effectuerOperation(String numeroCompte, double montant, TypeOperation type) {
        try {
            // 1. Vérifier si le compte existe
            Compte compte = compteService.getCompteByNumero(numeroCompte);
            if (compte == null) {
                return false;
            }

            // 2. Vérifier si l'opération est possible
            if (type == TypeOperation.RETRAIT && compte.getBalance() < montant) {
                return false;
            }

            // 3. Mettre à jour le solde du compte
            double newBalance = type == TypeOperation.DEPOT || type == TypeOperation.VERSEMENT ? 
                              compte.getBalance() + montant : 
                              compte.getBalance() - montant;
            
            compte.setBalance(newBalance);
            if (!compteService.updateCompte(compte)) {
                return false;
            }

            // 4. Enregistrer l'opération
            db.initPrepar("INSERT INTO operations (amount, type, compte_id) VALUES (?, ?, ?)");
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, type.name());
            db.getPstm().setInt(3, compte.getId());
            
            return db.executeMaj() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public List<Operation> getAllOperations() {
        List<Operation> operations = new ArrayList<>();
        try {
            db.initPrepar("SELECT o.*, c.numero FROM operations o JOIN comptes c ON o.compte_id = c.id ORDER BY o.date_op DESC");
            ResultSet rs = db.executeSelect();
            
            while (rs != null && rs.next()) {
                operations.add(extractOperationFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return operations;
    }

    @Override
    public List<Operation> getOperationsByCompte(String numeroCompte) {
        List<Operation> operations = new ArrayList<>();
        try {
            db.initPrepar("SELECT o.*, c.numero FROM operations o JOIN comptes c ON o.compte_id = c.id WHERE c.numero = ? ORDER BY o.date_op DESC");
            db.getPstm().setString(1, numeroCompte);
            ResultSet rs = db.executeSelect();
            
            while (rs != null && rs.next()) {
                operations.add(extractOperationFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return operations;
    }

    private Operation extractOperationFromResultSet(ResultSet rs) throws Exception {
        Operation operation = new Operation();
        operation.setId(rs.getInt("id"));
        operation.setDateOp(rs.getTimestamp("date_op").toInstant());
        operation.setAmount(rs.getDouble("amount"));
        operation.setType(TypeOperation.valueOf(rs.getString("type")));
        
        Compte compte = new Compte();
        compte.setId(rs.getInt("compte_id"));
        compte.setNumero(rs.getString("numero"));
        operation.setCompte(compte);
        
        return operation;
    }
}
