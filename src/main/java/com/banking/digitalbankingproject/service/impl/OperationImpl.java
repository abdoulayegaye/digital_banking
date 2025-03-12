package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class OperationImpl implements IOperation {

    private static final Logger logger = Logger.getLogger(OperationImpl.class.getName());
    private Db db = new Db();
    private int ok;
    private ICompte compteService = new CompteImpl();

    @Override
    public boolean createOperation(Operation operation) {
        if (operation.getCompteId() <= 0) {
            logger.warning("L'ID du compte est invalide");
            return false;
        }
        if (operation.getMontant() <= 0) {
            logger.warning("Le montant de l'opération doit être positif");
            return false;
        }
        if (operation.getTypeOperation() == null || operation.getTypeOperation().isEmpty()) {
            logger.warning("Le type d'opération est invalide");
            return false;
        }

        String sql = "INSERT INTO operations VALUES(NULL, ?, ?, ?, NOW())";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, operation.getCompteId());
            db.getPstm().setDouble(2, operation.getMontant());
            db.getPstm().setString(3, operation.getTypeOperation());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la création de l'opération : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }

    @Override
    public List<Operation> getAllOperations() {
        String sql = "SELECT * FROM operations ORDER BY date DESC";
        List<Operation> operations = new ArrayList<>();
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setCompteId(rs.getInt("compteId"));
                operation.setMontant(rs.getDouble("montant"));
                operation.setTypeOperation(rs.getString("typeOperation"));
                operations.add(operation);
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des opérations : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return operations;
    }

    @Override
    public Operation getOperationById(int id) {
        String sql = "SELECT * FROM operations WHERE id = ?";
        Operation operation = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setCompteId(rs.getInt("compteId"));
                operation.setMontant(rs.getDouble("montant"));
                operation.setTypeOperation(rs.getString("typeOperation"));
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération de l'opération : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return operation;
    }

    @Override
    public boolean updateOperation(Operation operation) {
        if (operation.getCompteId() <= 0) {
            logger.warning("L'ID du compte est invalide");
            return false;
        }
        if (operation.getMontant() <= 0) {
            logger.warning("Le montant de l'opération doit être positif");
            return false;
        }
        if (operation.getTypeOperation() == null || operation.getTypeOperation().isEmpty()) {
            logger.warning("Le type d'opération est invalide");
            return false;
        }

        String sql = "UPDATE operations SET compteId = ?, montant = ?, typeOperation = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, operation.getCompteId());
            db.getPstm().setDouble(2, operation.getMontant());
            db.getPstm().setString(3, operation.getTypeOperation());
            db.getPstm().setInt(4, operation.getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la mise à jour de l'opération : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }

    @Override
    public boolean deleteOperation(int id) {
        String sql = "DELETE FROM operations WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la suppression de l'opération : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }
    
    @Override
    public List<Operation> getOperationsByCompteId(int compteId) {
        String sql = "SELECT * FROM operations WHERE compteId = ? ORDER BY date DESC";
        List<Operation> operations = new ArrayList<>();
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setCompteId(rs.getInt("compteId"));
                operation.setMontant(rs.getDouble("montant"));
                operation.setTypeOperation(rs.getString("typeOperation"));
                operations.add(operation);
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des opérations du compte : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return operations;
    }
    
    @Override
    public boolean effectuerDepot(int compteId, double montant) {
        if (montant <= 0) {
            logger.warning("Le montant du dépôt doit être positif");
            return false;
        }
        
        // Récupérer le compte
        Compte compte = compteService.getCompteById(compteId);
        if (compte == null) {
            logger.warning("Compte non trouvé");
            return false;
        }
        
        // Mettre à jour le solde du compte
        compte.setSolde(compte.getSolde() + montant);
        boolean updateResult = compteService.updateCompte(compte);
        
        if (!updateResult) {
            logger.warning("Échec de la mise à jour du solde du compte");
            return false;
        }
        
        // Créer l'opération
        Operation operation = new Operation();
        operation.setCompteId(compteId);
        operation.setMontant(montant);
        operation.setTypeOperation("DEPOT");
        
        return createOperation(operation);
    }
    
    @Override
    public boolean effectuerRetrait(int compteId, double montant) {
        if (montant <= 0) {
            logger.warning("Le montant du retrait doit être positif");
            return false;
        }
        
        // Récupérer le compte
        Compte compte = compteService.getCompteById(compteId);
        if (compte == null) {
            logger.warning("Compte non trouvé");
            return false;
        }
        
        // Vérifier si le solde est suffisant
        if (compte.getSolde() < montant) {
            logger.warning("Solde insuffisant pour effectuer le retrait");
            return false;
        }
        
        // Mettre à jour le solde du compte
        compte.setSolde(compte.getSolde() - montant);
        boolean updateResult = compteService.updateCompte(compte);
        
        if (!updateResult) {
            logger.warning("Échec de la mise à jour du solde du compte");
            return false;
        }
        
        // Créer l'opération
        Operation operation = new Operation();
        operation.setCompteId(compteId);
        operation.setMontant(montant);
        operation.setTypeOperation("RETRAIT");
        
        return createOperation(operation);
    }
    
    @Override
    public boolean effectuerVirement(int compteSourceId, int compteDestinationId, double montant) {
        if (montant <= 0) {
            logger.warning("Le montant du virement doit être positif");
            return false;
        }
        
        if (compteSourceId == compteDestinationId) {
            logger.warning("Les comptes source et destination ne peuvent pas être identiques");
            return false;
        }
        
        // Récupérer les comptes
        Compte compteSource = compteService.getCompteById(compteSourceId);
        Compte compteDestination = compteService.getCompteById(compteDestinationId);
        
        if (compteSource == null || compteDestination == null) {
            logger.warning("Un des comptes n'a pas été trouvé");
            return false;
        }
        
        // Vérifier si le solde est suffisant
        if (compteSource.getSolde() < montant) {
            logger.warning("Solde insuffisant pour effectuer le virement");
            return false;
        }
        
        // Effectuer le retrait du compte source
        boolean retraitResult = effectuerRetrait(compteSourceId, montant);
        
        if (!retraitResult) {
            logger.warning("Échec du retrait du compte source");
            return false;
        }
        
        // Effectuer le dépôt sur le compte destination
        boolean depotResult = effectuerDepot(compteDestinationId, montant);
        
        if (!depotResult) {
            // Annuler le retrait en cas d'échec du dépôt
            compteSource.setSolde(compteSource.getSolde() + montant);
            compteService.updateCompte(compteSource);
            logger.warning("Échec du dépôt sur le compte destination, le retrait a été annulé");
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean genererReleve(int compteId) {
        try {
            // Récupérer le compte
            Compte compte = compteService.getCompteById(compteId);
            if (compte == null) {
                logger.warning("Compte non trouvé");
                return false;
            }
            
            // Récupérer les opérations du compte
            List<Operation> operations = getOperationsByCompteId(compteId);
            
            // Générer le nom du fichier avec la date actuelle
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "releve_compte_" + compteId + "_" + dateFormat.format(new Date()) + ".txt";
            
            // Créer le fichier
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                // Écrire l'en-tête
                String header = "RELEVÉ DE COMPTE\n";
                header += "Numéro de compte: " + compteId + "\n";
                header += "Solde actuel: " + compte.getSolde() + " €\n";
                header += "Date d'édition: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n";
                header += "\n";
                header += "HISTORIQUE DES OPÉRATIONS\n";
                header += "----------------------------------------------------\n";
                header += "ID\tType\t\tMontant\t\tDate\n";
                header += "----------------------------------------------------\n";
                
                fos.write(header.getBytes());
                
                // Écrire les opérations
                for (Operation operation : operations) {
                    String line = operation.getId() + "\t" + operation.getTypeOperation() + "\t\t" + 
                                 operation.getMontant() + " €\t\t" + new Date() + "\n";
                    fos.write(line.getBytes());
                }
                
                // Écrire le pied de page
                String footer = "----------------------------------------------------\n";
                footer += "Fin du relevé\n";
                
                fos.write(footer.getBytes());
            }
            
            logger.info("Relevé généré avec succès: " + fileName);
            return true;
            
        } catch (Exception e) {
            logger.severe("Erreur lors de la génération du relevé : " + e.getMessage());
            return false;
        }
    }
}