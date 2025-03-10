package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {

    private Db db = new Db();

    @Override
    public boolean createOperation(Operation operation) {
        // Vérifier si le compte source est null
        if (operation.getCompte() == null) {
            Notification.NotifError("Erreur", "Le compte source est obligatoire");
            return false;
        }

        // Vérifier si le compte de destination est requis pour un virement
        if (operation.getType() == TypeOperation.VIREMENT && operation.getCompteDestination() == null) {
            Notification.NotifError("Erreur", "Le compte destination est obligatoire pour un virement");
            return false;
        }

        // Vérifier si le compte source est fermé
        Compte compteSource = operation.getCompte();
        if ("FERME".equals(compteSource.getStatut())) {
            Notification.NotifError("Erreur", "Le compte source est fermé");
            return false;
        }

        // Calculer le nouveau solde du compte source
        double nouveauSoldeSource = compteSource.getBalance();
        switch (operation.getType()) {
            case VERSEMENT:
                nouveauSoldeSource += operation.getAmount();
                break;
            case RETRAIT:
            case VIREMENT:
                nouveauSoldeSource -= operation.getAmount();
                break;
            default:
                Notification.NotifError("Erreur", "Type d'opération non supporté");
                return false;
        }

        // Vérifier si le solde est suffisant pour un retrait ou un virement
        if (nouveauSoldeSource < 0) {
            Notification.NotifError("Erreur", "Solde insuffisant pour effectuer cette opération");
            return false;
        }

        // Mettre à jour le solde du compte source
        CompteImpl compteDao = new CompteImpl();
        if (!compteDao.updateBalance(compteSource.getId(), nouveauSoldeSource)) {
            Notification.NotifError("Erreur", "Échec de la mise à jour du solde du compte source");
            return false;
        }

        // Pour un virement, mettre à jour le solde du compte destination
        if (operation.getType() == TypeOperation.VIREMENT) {
            Compte compteDestination = operation.getCompteDestination();
            if ("FERME".equals(compteDestination.getStatut())) {
                Notification.NotifError("Erreur", "Le compte destination est fermé");
                return false;
            }

            double nouveauSoldeDestination = compteDestination.getBalance() + operation.getAmount();
            if (!compteDao.updateBalance(compteDestination.getId(), nouveauSoldeDestination)) {
                Notification.NotifError("Erreur", "Échec de la mise à jour du solde du compte destination");
                return false;
            }
        }

        // Enregistrer l'opération dans la base de données
        String sql = "INSERT INTO operations (date_op, amount, type, compte_id, compte_destination_id) VALUES (?, ?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, java.sql.Timestamp.from(operation.getDateOp()));
            db.getPstm().setDouble(2, operation.getAmount());
            db.getPstm().setString(3, operation.getType().name());
            db.getPstm().setInt(4, operation.getCompte().getId());

            // Définir l'ID du compte de destination (peut être null pour les opérations non-virement)
            if (operation.getType() == TypeOperation.VIREMENT) {
                db.getPstm().setInt(5, operation.getCompteDestination().getId());
            } else {
                db.getPstm().setNull(5, java.sql.Types.INTEGER); // Définir comme NULL pour les opérations non-virement
            }

            // Exécuter la requête
            int result = db.executeMaj();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Erreur lors de l'ajout de l'opération : " + e.getMessage());
            return false;
        } finally {
            db.closeConnection();
        }
    }

    private void updateCompteBalance(Operation operation) {
        Compte compte = operation.getCompte();
        double oldBalance = compte.getBalance();
        double newBalance = oldBalance;

        switch(operation.getType()) {
            case VERSEMENT:
                newBalance += operation.getAmount();
                break;
            case RETRAIT:
            case VIREMENT:
                newBalance -= operation.getAmount();
                break;
            default:
                return; // Ne rien faire si le type d'opération est inconnu
        }

        if (newBalance == oldBalance) return; // Éviter une mise à jour inutile

        compte.setBalance(newBalance);

        String sqlUpdate = "UPDATE comptes SET balance = ? WHERE id = ?";
        try {
            db.initPrepar(sqlUpdate);
            db.getPstm().setDouble(1, newBalance);
            db.getPstm().setInt(2, compte.getId());
            db.executeMaj();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Operation> getAllOperations() {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT o.id, o.date_op, o.amount, o.type, o.compte_id, o.compte_destination_id, " +
                "c1.id AS compte_source_id, c1.balance AS compte_source_balance, c1.numero AS compte_source_numero, " +
                "c2.id AS compte_destination_id, c2.balance AS compte_destination_balance, c2.numero AS compte_destination_numero " +
                "FROM operations o " +
                "LEFT JOIN comptes c1 ON o.compte_id = c1.id " +
                "LEFT JOIN comptes c2 ON o.compte_destination_id = c2.id";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op").toInstant());
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));

                // Charger le compte source
                Compte compteSource = new Compte();
                compteSource.setId(rs.getInt("compte_source_id"));
                compteSource.setBalance(rs.getDouble("compte_source_balance"));
                compteSource.setNumero(rs.getString("compte_source_numero")); // Récupérer le numéro de compte
                operation.setCompte(compteSource);

                // Charger le compte destination (si applicable)
                if (rs.getObject("compte_destination_id") != null) {
                    Compte compteDestination = new Compte();
                    compteDestination.setId(rs.getInt("compte_destination_id"));
                    compteDestination.setBalance(rs.getDouble("compte_destination_balance"));
                    compteDestination.setNumero(rs.getString("compte_destination_numero")); // Récupérer le numéro de compte
                    operation.setCompteDestination(compteDestination);
                }

                operations.add(operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return operations;
    }

    @Override
    public List<Operation> getOperationsByCompteId(int compteId) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations WHERE compte_id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op").toInstant());
                operation.setAmount(rs.getDouble("amount"));
                String typeStr = rs.getString("type");
                if ("DEPOT".equalsIgnoreCase(typeStr)) {
                    typeStr = "VERSEMENT";
                }
                operation.setType(TypeOperation.valueOf(typeStr));
                operations.add(operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return operations;
    }

    @Override
    public List<Operation> getOperationsByDate(Instant startDate, Instant endDate) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT o.id, o.date_op, o.amount, o.type, o.compte_id, o.compte_destination_id, " +
                "c1.id AS compte_source_id, c1.balance AS compte_source_balance, c1.numero AS compte_source_numero, " +
                "c2.id AS compte_destination_id, c2.balance AS compte_destination_balance, c2.numero AS compte_destination_numero " +
                "FROM operations o " +
                "LEFT JOIN comptes c1 ON o.compte_id = c1.id " +
                "LEFT JOIN comptes c2 ON o.compte_destination_id = c2.id " +
                "WHERE o.date_op >= ? AND o.date_op < ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, java.sql.Timestamp.from(startDate));
            db.getPstm().setTimestamp(2, java.sql.Timestamp.from(endDate));
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op").toInstant());
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));

                // Charger le compte source
                Compte compteSource = new Compte();
                compteSource.setId(rs.getInt("compte_source_id"));
                compteSource.setBalance(rs.getDouble("compte_source_balance"));
                compteSource.setNumero(rs.getString("compte_source_numero"));
                operation.setCompte(compteSource);

                // Charger le compte destination (si applicable)
                if (rs.getObject("compte_destination_id") != null) {
                    Compte compteDestination = new Compte();
                    compteDestination.setId(rs.getInt("compte_destination_id"));
                    compteDestination.setBalance(rs.getDouble("compte_destination_balance"));
                    compteDestination.setNumero(rs.getString("compte_destination_numero"));
                    operation.setCompteDestination(compteDestination);
                }

                operations.add(operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return operations;
    }

    @Override
    public int countOperations() {
        String sql = "SELECT COUNT(*) FROM operations";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return rs.getInt(1); // Retourne le nombre d'opérations
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return 0;
    }
}
