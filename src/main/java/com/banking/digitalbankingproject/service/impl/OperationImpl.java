package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    
    private Db db = new Db();
    private ResultSet rs;
    private int ok;
    private ICompte compteDao = new CompteImpl();

    @Override
    public int depot(Compte compte, double montant) throws Exception {
        try {
            System.out.println("Début du dépôt de " + montant + " sur le compte " + compte.getNumero());
            System.out.println("Informations du compte : ID=" + compte.getId() + ", Solde actuel=" + compte.getSolde());
            
            int updateResult = compteDao.updateSolde(compte.getNumero(), montant);
            System.out.println("Solde mis à jour avec succès, résultat : " + updateResult);
            
            String sql = "INSERT INTO operations (date_op, montant, type, compte_id) VALUES (?, ?, ?, ?)";
            System.out.println("Exécution de la requête : " + sql);
            System.out.println("Paramètres : date=" + Instant.now() + ", montant=" + montant + 
                             ", type=" + TypeOperation.DEPOT + ", compte_id=" + compte.getId());
            
            db.initPrepar(sql);
            db.getPstm().setObject(1, Instant.now());
            db.getPstm().setDouble(2, montant);
            db.getPstm().setString(3, TypeOperation.DEPOT.toString());
            db.getPstm().setInt(4, compte.getId());
            
            ok = db.executeMaj();
            System.out.println("Opération enregistrée avec succès, résultat : " + ok);
            
            if (ok > 0) {
                System.out.println("Opération de dépôt enregistrée avec succès dans la base de données");
            } else {
                System.err.println("ATTENTION: L'opération de dépôt n'a pas été enregistrée correctement");
            }
            
            db.closeConnection();
        } catch (Exception e) {
            System.err.println("Erreur lors du dépôt : " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur lors du dépôt : " + e.getMessage());
        }
        return ok;
    }

    @Override
    public int retrait(Compte compte, double montant) throws Exception {
        try {
            if (compte.getSolde() < montant) {
                throw new Exception("Solde insuffisant");
            }
            
            int updateResult = compteDao.updateSolde(compte.getNumero(), -montant);
            System.out.println("Solde mis à jour pour le retrait, résultat : " + updateResult);
            
            String sql = "INSERT INTO operations (date_op, montant, type, compte_id) VALUES (?, ?, ?, ?)";
            System.out.println("Exécution de la requête de retrait : " + sql);
            
            db.initPrepar(sql);
            db.getPstm().setObject(1, Instant.now());
            db.getPstm().setDouble(2, montant);
            db.getPstm().setString(3, TypeOperation.RETRAIT.toString());
            db.getPstm().setInt(4, compte.getId());
            ok = db.executeMaj();
            
            if (ok > 0) {
                System.out.println("Opération de retrait enregistrée avec succès dans la base de données");
            } else {
                System.err.println("ATTENTION: L'opération de retrait n'a pas été enregistrée correctement");
            }
            
            db.closeConnection();
        } catch (Exception e) {
            System.err.println("Erreur lors du retrait : " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur lors du retrait : " + e.getMessage());
        }
        return ok;
    }

    @Override
    public int virement(Compte source, Compte destination, double montant) throws Exception {
        try {
            if (source.getSolde() < montant) {
                throw new Exception("Solde insuffisant");
            }
            
            retrait(source, montant);
            
            depot(destination, montant);
            
            return 1;
        } catch (Exception e) {
            throw new Exception("Erreur lors du virement : " + e.getMessage());
        }
    }

    @Override
    public List<Operation> getAll() throws Exception {
        String sql = "SELECT o.id, o.date_op, o.montant, o.type, o.compte_id, c.numero " +
                    "FROM operations o " +
                    "JOIN comptes c ON o.compte_id = c.id ORDER BY o.date_op DESC";
        List<Operation> operations = new ArrayList<>();
        try {
            System.out.println("Récupération de toutes les opérations...");
            System.out.println("Exécution de la requête : " + sql);
            
            db.initPrepar(sql);
            rs = db.executeSelect();
            
            System.out.println("Résultat de la requête obtenu, parcours des résultats...");
            int count = 0;
            
            while (rs.next()) {
                count++;
                System.out.println("Chargement d'une opération... #" + count);
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Montant: " + rs.getDouble("montant"));
                System.out.println("Type: " + rs.getString("type"));
                System.out.println("Numéro de compte: " + rs.getString("numero"));
                
                Compte compte = compteDao.getByNumero(rs.getString("numero"));
                System.out.println("Compte récupéré: " + (compte != null ? compte.getNumero() : "null"));
                
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                
                java.sql.Timestamp timestamp = rs.getTimestamp("date_op");
                operation.setDateOperation(timestamp != null ? timestamp.toInstant() : Instant.now());
                
                operation.setAmount(rs.getDouble("montant"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                operation.setCompte(compte);
                operations.add(operation);
                
                System.out.println("Opération ajoutée à la liste : " + operation);
            }
            
            if (count == 0) {
                System.out.println("AUCUNE OPÉRATION TROUVÉE DANS LA BASE DE DONNÉES!");
            } else {
                System.out.println("Nombre total d'opérations récupérées : " + operations.size());
            }
            
            db.closeConnection();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des opérations : " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur lors de la récupération des opérations : " + e.getMessage());
        }
        return operations;
    }

    @Override
    public List<Operation> getOperationsCompte(Compte compte) throws Exception {
        String sql = "SELECT id, date_op, montant, type, compte_id FROM operations WHERE compte_id = ? ORDER BY date_op DESC";
        List<Operation> operations = new ArrayList<>();
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compte.getId());
            rs = db.executeSelect();
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                
                java.sql.Timestamp timestamp = rs.getTimestamp("date_op");
                operation.setDateOperation(timestamp != null ? timestamp.toInstant() : Instant.now());
                
                operation.setAmount(rs.getDouble("montant"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                operation.setCompte(compte);
                operations.add(operation);
            }
            db.closeConnection();
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération des opérations du compte : " + e.getMessage());
        }
        return operations;
    }
}
