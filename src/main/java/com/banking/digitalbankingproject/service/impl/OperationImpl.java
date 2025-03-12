package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.DatabaseConnection;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de gestion des opérations bancaires
 */
public class OperationImpl implements IOperation {
    
    private Connection connection;
    private CompteImpl compteService;
    
    /**
     * Constructeur initialisant la connexion à la base de données
     */
    public OperationImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.compteService = new CompteImpl() {
                @Override
                public List<Compte> getAllComptes() throws SQLException {
                    return List.of();
                }

                @Override
                public Compte getCompteById(int id) throws SQLException {
                    return null;
                }

                @Override
                public Compte getCompteByNumero(String numero) throws SQLException {
                    return null;
                }

                @Override
                public List<Compte> getComptesByClientId(int clientId) throws SQLException {
                    return List.of();
                }

                @Override
                public boolean updateBalance(int compteId, double newBalance) throws SQLException {
                    return false;
                }

                @Override
                public boolean deleteCompte(int id) throws SQLException {
                    return false;
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Effectue une opération bancaire (dépôt ou retrait)
     * 
     * @param compteId Identifiant du compte
     * @param montant Montant de l'opération
     * @param type Type d'opération (DEPOT ou RETRAIT)
     * @return Opération créée
     */
    @Override
    public Operation effectuerOperation(int compteId, double montant, TypeOperation type) throws SQLException {
        // Vérifier que le montant est positif
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro");
        }
        
        // Récupérer le compte
        Compte compte = compteService.getCompteById(compteId);
        if (compte == null) {
            throw new SQLException("Compte non trouvé");
        }
        
        // Vérifier le solde pour un retrait
        if (type == TypeOperation.RETRAIT && compte.getBalance() < montant) {
            throw new SQLException("Solde insuffisant pour effectuer ce retrait");
        }
        
        // Mettre à jour le solde du compte
        double newBalance;
        if (type == TypeOperation.DEPOT) {
            newBalance = compte.getBalance() + montant;
        } else {
            newBalance = compte.getBalance() - montant;
        }
        
        // Commencer une transaction
        connection.setAutoCommit(false);
        
        try {
            // Mettre à jour le solde
            compteService.updateBalance(compteId, newBalance);
            
            // Créer l'opération
            Operation operation = new Operation();
            operation.setDateOp(Instant.now());
            operation.setAmount(montant);
            operation.setType(type);
            operation.setCompte(compte);
            operation.setDescription(type == TypeOperation.DEPOT ? "Dépôt en espèces" : "Retrait en espèces");
            
            // Enregistrer l'opération
            saveOperation(operation);
            
            // Valider la transaction
            connection.commit();
            
            return operation;
        } catch (SQLException e) {
            // Annuler la transaction en cas d'erreur
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    /**
     * Effectue un virement entre deux comptes
     * 
     * @param compteSourceId Identifiant du compte source
     * @param compteDestinationId Identifiant du compte destination
     * @param montant Montant du virement
     * @return Opération de virement créée
     */
    @Override
    public Operation effectuerVirement(int compteSourceId, int compteDestinationId, double montant) throws SQLException {
        // Vérifier que le montant est positif
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro");
        }
        
        // Vérifier que les comptes sont différents
        if (compteSourceId == compteDestinationId) {
            throw new IllegalArgumentException("Les comptes source et destination doivent être différents");
        }
        
        // Récupérer les comptes
        Compte compteSource = compteService.getCompteById(compteSourceId);
        Compte compteDestination = compteService.getCompteById(compteDestinationId);
        
        if (compteSource == null || compteDestination == null) {
            throw new SQLException("Un des comptes n'a pas été trouvé");
        }
        
        // Vérifier le solde du compte source
        if (compteSource.getBalance() < montant) {
            throw new SQLException("Solde insuffisant pour effectuer ce virement");
        }
        
        // Calculer les nouveaux soldes
        double newBalanceSource = compteSource.getBalance() - montant;
        double newBalanceDestination = compteDestination.getBalance() + montant;
        
        // Commencer une transaction
        connection.setAutoCommit(false);
        
        try {
            // Mettre à jour les soldes
            compteService.updateBalance(compteSourceId, newBalanceSource);
            compteService.updateBalance(compteDestinationId, newBalanceDestination);
            
            // Créer l'opération de débit sur le compte source
            Operation operationDebit = new Operation();
            operationDebit.setDateOp(Instant.now());
            operationDebit.setAmount(montant);
            operationDebit.setType(TypeOperation.VIREMENT);
            operationDebit.setCompte(compteSource);
            operationDebit.setCompteDestination(compteDestination);
            operationDebit.setDescription("Virement vers " + compteDestination.getNumero());
            
            // Enregistrer l'opération de débit
            saveOperation(operationDebit);
            
            // Créer l'opération de crédit sur le compte destination
            Operation operationCredit = new Operation();
            operationCredit.setDateOp(Instant.now());
            operationCredit.setAmount(montant);
            operationCredit.setType(TypeOperation.DEPOT);
            operationCredit.setCompte(compteDestination);
            operationCredit.setDescription("Virement reçu de " + compteSource.getNumero());
            
            // Enregistrer l'opération de crédit
            saveOperation(operationCredit);
            
            // Valider la transaction
            connection.commit();
            
            return operationDebit;
        } catch (SQLException e) {
            // Annuler la transaction en cas d'erreur
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    /**
     * Récupère toutes les opérations d'un compte
     * 
     * @param compteId Identifiant du compte
     * @return Liste des opérations du compte
     */
    @Override
    public List<Operation> getOperationsByCompteId(int compteId) throws SQLException {
        List<Operation> operations = new ArrayList<>();
        String query = "SELECT * FROM operation WHERE compte_id = ? ORDER BY date_op DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, compteId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Operation operation = mapResultSetToOperation(rs);
                    operations.add(operation);
                }
            }
        }
        
        return operations;
    }
    
    /**
     * Récupère les opérations d'un compte pour une période donnée
     * 
     * @param compteId Identifiant du compte
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return Liste des opérations pour la période
     */
    @Override
    public List<Operation> getOperationsByCompteAndPeriod(int compteId, Instant dateDebut, Instant dateFin) throws SQLException {
        List<Operation> operations = new ArrayList<>();
        String query = "SELECT * FROM operation WHERE compte_id = ? AND date_op BETWEEN ? AND ? ORDER BY date_op DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, compteId);
            pstmt.setTimestamp(2, Timestamp.from(dateDebut));
            pstmt.setTimestamp(3, Timestamp.from(dateFin));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Operation operation = mapResultSetToOperation(rs);
                    operations.add(operation);
                }
            }
        }
        
        return operations;
    }
    
    /**
     * Enregistre une opération dans la base de données
     * 
     * @param operation Opération à enregistrer
     * @return Opération enregistrée avec son ID généré
     */
    private Operation saveOperation(Operation operation) throws SQLException {
        String query = "INSERT INTO operation (date_op, amount, type, compte_id, description, compte_destination_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.from(operation.getDateOp()));
            pstmt.setDouble(2, operation.getAmount());
            pstmt.setString(3, operation.getType().name());
            pstmt.setInt(4, operation.getCompte().getId());
            pstmt.setString(5, operation.getDescription());
            
            if (operation.getCompteDestination() != null) {
                pstmt.setInt(6, operation.getCompteDestination().getId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("La création de l'opération a échoué, aucune ligne affectée.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    operation.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création de l'opération a échoué, aucun ID obtenu.");
                }
            }
        }
        
        return operation;
    }
    
    /**
     * Convertit un ResultSet en objet Operation
     * 
     * @param rs ResultSet contenant les données de l'opération
     * @return Objet Operation créé à partir du ResultSet
     */
    private Operation mapResultSetToOperation(ResultSet rs) throws SQLException {
        Operation operation = new Operation();
        operation.setId(rs.getInt("id"));
        operation.setDateOp(rs.getTimestamp("date_op").toInstant());
        operation.setAmount(rs.getDouble("amount"));
        operation.setType(TypeOperation.valueOf(rs.getString("type")));
        operation.setDescription(rs.getString("description"));
        
        // Récupérer le compte associé
        int compteId = rs.getInt("compte_id");
        Compte compte = compteService.getCompteById(compteId);
        operation.setCompte(compte);
        
        // Récupérer le compte destination si présent
        int compteDestinationId = rs.getInt("compte_destination_id");
        if (!rs.wasNull()) {
            Compte compteDestination = compteService.getCompteById(compteDestinationId);
            operation.setCompteDestination(compteDestination);
        }
        
        return operation;
    }

    @Override
    public Operation getById(int id) {
        String sql = "SELECT o.*, c.numero as compte_numero FROM operations o " +
                     "JOIN comptes c ON o.compte_id = c.id " +
                     "WHERE o.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOperation(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public double calculerSoldeCompte(int compteId) {
        String sql = "SELECT SUM(amount) as total FROM operation WHERE compte_id = ? AND type = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, compteId);
            pstmt.setString(2, TypeOperation.DEPOT.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
}
