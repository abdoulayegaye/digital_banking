package org.example.javafx.service.impl;

import org.example.javafx.dao.DBConnexion;
import org.example.javafx.entities.Client;
import org.example.javafx.entities.Compte;
import org.example.javafx.service.ICompte;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class CompteImpl implements ICompte {

    private DBConnexion db = new DBConnexion();
    private ResultSet rs;
    private int ok;
    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.id AS compte_id, c.numero, c.solde, c.date_ouverture, " +
                "cl.id_client AS client_id, cl.nom, cl.prenom, cl.email " +
                "FROM Comptes c " +
                "JOIN Clients cl ON c.client_id = cl.id_client"; // Jointure sur client_id = id_client

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Créer un nouvel objet Compte
                Compte compte = new Compte();
                compte.setId(rs.getInt("compte_id")); // Utiliser l'alias "compte_id"
                compte.setNumero(rs.getString("numero"));
                compte.setSolde(rs.getDouble("solde"));
                compte.setDate_ouverture(rs.getTimestamp("date_ouverture"));

                // Créer un nouvel objet Client
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));

                // Associer le Client au Compte
                compte.setClient(client);

                // Ajouter le Compte à la liste
                comptes.add(compte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return comptes;
    }
    @Override
    public int create(Compte compte) {
        String sql = "INSERT INTO Comptes (numero, solde, date_ouverture, client_id) VALUES (?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getSolde());
            // Définir la date d'ouverture
            if (compte.getDate_ouverture() != null) {
                db.getPstm().setTimestamp(3, compte.getDate_ouverture());
            } else {
                db.getPstm().setNull(3, java.sql.Types.TIMESTAMP); // Définir la date comme NULL dans la base de données
            }
            db.getPstm().setInt(4, compte.getClient().getId());
            ok = db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }


//    @Override
//    public int associeClient(int compteId, int clientId) {
//        String sql = "UPDATE Comptes SET client_id = ? WHERE id = ?";
//        try {
//            db.initPrepar(sql);
//            db.getPstm().setInt(1, clientId);
//            db.getPstm().setInt(2, compteId);
//            ok = db.executeMaj();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.closeConnection();
//        }
//        return ok;
//    }


    @Override
    public double getSolde(int compteId) {
        String sql = "SELECT solde FROM Comptes WHERE id = ?";
        double solde = -1; // Valeur par défaut si le compte n'est pas trouvé
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            rs = db.executeSelect();
            if (rs.next()) {
                solde = rs.getDouble("solde");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return solde;
    }

    @Override
    public int closeAccount(int compteId) {
        String checkSql = "SELECT solde FROM Comptes WHERE id = ?";
        String deleteSql = "DELETE FROM Comptes WHERE id = ? AND solde = 0";

        try {
            // Vérifier si le compte existe et récupérer le solde
            db.initPrepar(checkSql);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();

            if (rs.next()) {
                double solde = rs.getDouble("solde");

                if (solde != 0) {
                    System.out.println("Le compte ne peut pas être fermé car son solde est de " + solde);
                    return 0; // Refusé : solde différent de 0
                }

                // Fermer le compte car solde == 0
                db.initPrepar(deleteSql);
                db.getPstm().setInt(1, compteId);
                ok = db.executeMaj();

                if (ok > 0) {
                    System.out.println("Compte fermé avec succès !");
                } else {
                    System.out.println("Échec de la fermeture du compte.");
                }
            } else {
                System.out.println("Compte introuvable !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }


    @Override
    public byte[] generateBankStatement(int compteId) {
        return new byte[0];
    }


}
