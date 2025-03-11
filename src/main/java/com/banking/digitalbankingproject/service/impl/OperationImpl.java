package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private final Db db;
    private final ICompte compteService;

    public OperationImpl() {
        this.db = new Db();
        this.compteService = new CompteImpl();
    }

    @Override
    public boolean effectuerDepot(int compteId, double montant) {
        if (montant <= 0) return false;

        Compte compte = compteService.getCompteById(compteId);
        if (compte == null) return false;

        try {
            db.getConnection().setAutoCommit(false);
            
            // Mise à jour du solde
            double nouveauSolde = compte.getBalance() + montant;
            if (!compteService.updateSolde(compteId, nouveauSolde)) {
                db.getConnection().rollback();
                return false;
            }

            // Enregistrement de l'opération
            String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (NOW(), ?, ?, ?)";
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, TypeOperation.VERSEMENT.toString());
            db.getPstm().setInt(3, compteId);
            
            boolean success = db.executeMaj() > 0;
            if (success) {
                db.getConnection().commit();
                return true;
            } else {
                db.getConnection().rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                db.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                db.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.closeStatement();
        }
    }

    @Override
    public boolean effectuerRetrait(int compteId, double montant) {
        if (montant <= 0) return false;

        Compte compte = compteService.getCompteById(compteId);
        if (compte == null || compte.getBalance() < montant) return false;

        try {
            db.getConnection().setAutoCommit(false);
            
            // Mise à jour du solde
            double nouveauSolde = compte.getBalance() - montant;
            if (!compteService.updateSolde(compteId, nouveauSolde)) {
                db.getConnection().rollback();
                return false;
            }

            // Enregistrement de l'opération
            String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (NOW(), ?, ?, ?)";
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, TypeOperation.RETRAIT.toString());
            db.getPstm().setInt(3, compteId);
            
            boolean success = db.executeMaj() > 0;
            if (success) {
                db.getConnection().commit();
                return true;
            } else {
                db.getConnection().rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                db.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                db.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.closeStatement();
        }
    }

    @Override
    public boolean effectuerVirement(int compteSourceId, int compteDestinationId, double montant) {
        if (montant <= 0 || compteSourceId == compteDestinationId) return false;

        Compte compteSource = compteService.getCompteById(compteSourceId);
        Compte compteDestination = compteService.getCompteById(compteDestinationId);

        if (compteSource == null || compteDestination == null || compteSource.getBalance() < montant) {
            return false;
        }

        try {
            db.getConnection().setAutoCommit(false);

            // Débit du compte source
            double nouveauSoldeSource = compteSource.getBalance() - montant;
            if (!compteService.updateSolde(compteSourceId, nouveauSoldeSource)) {
                db.getConnection().rollback();
                return false;
            }

            // Crédit du compte destination
            double nouveauSoldeDestination = compteDestination.getBalance() + montant;
            if (!compteService.updateSolde(compteDestinationId, nouveauSoldeDestination)) {
                db.getConnection().rollback();
                return false;
            }

            // Enregistrement des opérations
            String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (NOW(), ?, ?, ?)";
            
            // Opération de débit
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, TypeOperation.VIREMENT.toString());
            db.getPstm().setInt(3, compteSourceId);
            if (db.executeMaj() <= 0) {
                db.getConnection().rollback();
                return false;
            }

            // Opération de crédit
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, TypeOperation.VIREMENT.toString());
            db.getPstm().setInt(3, compteDestinationId);
            
            boolean success = db.executeMaj() > 0;
            if (success) {
                db.getConnection().commit();
                return true;
            } else {
                db.getConnection().rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                db.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                db.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.closeStatement();
        }
    }

    @Override
    public List<Operation> getHistoriqueOperations(int compteId) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations WHERE compte_id = ? ORDER BY date_op DESC";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                operations.add(extractOperationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return operations;
    }

    @Override
    public List<Operation> getAllOperations() {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations ORDER BY date_op DESC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                operations.add(extractOperationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return operations;
    }

    @Override
    public Operation getOperationById(int id) {
        String sql = "SELECT * FROM operations WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return extractOperationFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return null;
    }

    private Operation extractOperationFromResultSet(ResultSet rs) throws SQLException {
        Operation operation = new Operation();
        operation.setId(rs.getInt("id"));
        operation.setDateOp(rs.getTimestamp("date_op").toInstant());
        operation.setAmount(rs.getDouble("amount"));
        operation.setType(TypeOperation.valueOf(rs.getString("type")));
        
        Compte compte = compteService.getCompteById(rs.getInt("compte_id"));
        operation.setCompte(compte);
        
        return operation;
    }
}
