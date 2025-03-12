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
                "JOIN Clients cl ON c.client_id = cl.id_client";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("compte_id"));
                compte.setNumero(rs.getString("numero"));
                compte.setSolde(rs.getDouble("solde"));
                compte.setDate_ouverture(rs.getTimestamp("date_ouverture"));

                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                compte.setClient(client);
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
}
