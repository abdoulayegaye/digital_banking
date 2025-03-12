package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.DatabaseConnection;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.enums.TypeCompte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.*;

/**
 * Implémentation du service de gestion des comptes
 */
public abstract class CompteImpl implements ICompte {

    private Connection connection;
    private ClientImpl clientService;

    /**
     * Constructeur initialisant la connexion à la base de données
     */
    public CompteImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.clientService = new ClientImpl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère tous les comptes de la base de données
     *
     * @return Liste de tous les comptes
     */
    @Override
    public List<Compte> getAll() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comptes;
    }

    /**
     * Récupère un compte par son identifiant
     *
     * @param id Identifiant du compte
     * @return Compte trouvé ou null
     */
    @Override
    public Compte getById(int id) {
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCompte(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Récupère tous les comptes d'un client
     *
     * @param clientId Identifiant du client
     * @return Liste des comptes du client
     */
    @Override
    public List<Compte> getByClientId(int clientId) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.client_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(mapResultSetToCompte(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comptes;
    }

    /**
     * Enregistre un nouveau compte dans la base de données
     *
     * @param compte Compte à enregistrer
     * @param typeCompte Type du compte à créer
     * @return Compte enregistré avec son ID généré
     */
    @Override
    public Compte saveCompte(Compte compte, TypeCompte typeCompte) throws SQLException {
        String query = "INSERT INTO compte (numero, balance, created_at, client_id, type_compte_id) " +
                "VALUES (?, ?, ?, ?, (SELECT id FROM type_compte WHERE libelle = ?))";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, compte.getNumero());
            pstmt.setDouble(2, compte.getBalance());
            pstmt.setTimestamp(3, Timestamp.from(compte.getCreatedAt() != null ? compte.getCreatedAt() : Instant.now()));
            pstmt.setInt(4, compte.getClient().getId());
            pstmt.setString(5, typeCompte.name());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du compte a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    compte.setId(generatedKeys.getInt(1));
                    compte.setTypeCompte(typeCompte);
                } else {
                    throw new SQLException("La création du compte a échoué, aucun ID obtenu.");
                }
            }
        }

        return compte;
    }

    /// Met &agrave; jour le solde d'un compute
    ///
    /// @param compteId Identifiant du compte
    /// @param nouveauSolde Nouveau solde
    /// @return true si la mise &agrave; jour a r&eacute;ussi
    @Override
    public boolean updateSolde(int compteId, double nouveauSolde) {
        String sql = "UPDATE comptes SET solde = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, nouveauSolde);
            pstmt.setInt(2, compteId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un compte par son identifiant
     * 
     * @param id Identifiant du compte à supprimer
     * @return true si la suppression a réussi
     */
    @Override
    public boolean delete(int id) {
        // Vérifier d'abord si le compte a des opérations
        try {
            if (compteHasOperations(id)) {
                System.out.println("Impossible de supprimer le compte car il possède des opérations.");
                return false;
            }
            
            String query = "DELETE FROM comptes WHERE id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, id);
                
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Génère un numéro de compte unique
     *
     * @return Numéro de compte généré
     */
    @Override
    public String generateAccountNumber() {
        TypeCompte typeCompte = null;
        String prefix = typeCompte == TypeCompte.COURANT ? "CC" : "CE";
        long timestamp = System.currentTimeMillis();
        return prefix + timestamp;
    }

    /**
     * Vérifie si un compte possède des opérations
     *
     * @param compteId Identifiant du compte
     * @return true si le compte a au moins une opération
     */
    private boolean compteHasOperations(int compteId) throws SQLException {
        String query = "SELECT COUNT(*) FROM operation WHERE compte_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, compteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Convertit un ResultSet en objet Compte
     *
     * @param rs ResultSet contenant les données du compte
     * @return Objet Compte créé à partir du ResultSet
     */
    private Compte mapResultSetToCompte(ResultSet rs) throws SQLException {
        Compte compte = new Compte();
        compte.setId(rs.getInt("id"));
        compte.setNumero(rs.getString("numero"));

        // Récupérer le client associé
        int clientId = rs.getInt("client_id");
        Client client = clientService.getClientById(clientId);
        compte.setClient(client);

        // Récupérer le type de compte
        String typeCompteStr = rs.getString("type_compte");
        compte.setTypeCompte(TypeCompte.valueOf(typeCompteStr));

        return compte;
    }

    public Compte findByNumero() {
        Compte byNumero = findByNumero(null);
        Compte byNumero1 = byNumero;
        return
                byNumero1;
    }

    @Override
    public Compte findByNumero(String numero) {
        Compte result = null;
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.numero = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numero);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = mapResultSetToCompte(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean update(Compte compte) {
        String sql = "UPDATE comptes SET type = ?, solde = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, compte.getType());
            pstmt.setDouble(2, compte.getSolde());
            pstmt.setString(3, compte.getDescription());
            pstmt.setInt(4, compte.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean add(Compte compte) {
        String sql = "INSERT INTO comptes (numero, client_id, type, solde, description, date_creation) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Si le numéro de compte n'est pas défini, générer un numéro unique
            if (compte.getNumero() == null || compte.getNumero().isEmpty()) {
                compte.setNumero(

                        generateAccountNumber());
            }

            pstmt.setString(1, compte.getNumero());
            pstmt.setInt(2, compte.getClientId());
            pstmt.setString(3, compte.getType());
            pstmt.setDouble(4, compte.getSolde() != null ? compte.getSolde() : 0.0);
            pstmt.setString(5, compte.getDescription());
            Instant LocalDateTime;
            LocalDateTime = null;
            Instant instant = null;
            String formatted = ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.systemDefault())
                    .format(instant);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        compte.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
