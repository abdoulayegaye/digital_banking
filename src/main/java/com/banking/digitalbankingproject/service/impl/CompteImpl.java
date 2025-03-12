package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.tools.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private Db db = new Db();
    private ResultSet rs;

    @Override
    public List<Compte> listCompte(String client) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT co.*, c.nom, c.prenom FROM comptes co " +
                "LEFT JOIN clients c ON co.client_id = c.id " +
                "WHERE c.nom LIKE ? OR c.nom IS NULL";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + (client == null || client.isEmpty() ? "" : client) + "%");
            rs = db.getPstm().executeQuery();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getDate("created_at"));
                compte.setType(Compte.TypeCompte.valueOf(rs.getString("type")));
                compte.setEtat(Compte.EtatCompte.valueOf(rs.getString("etat")));

                IClient clientDao = new ClientImpl();
                Client clientObj = clientDao.get(rs.getInt("client_id"));
                compte.setClient(clientObj != null ? clientObj : new Client());

                comptes.add(compte);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des comptes : " + e.getMessage());
            throw new RuntimeException("Échec de la récupération des comptes", e);
        } finally {
            try {
                if (rs != null) rs.close();
                db.closeConnection();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        return comptes;
    }

    @Override
    public int createCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id, type, etat) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        int ok = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setDate(3, new java.sql.Date(compte.getCreatedAt().getTime()));
            db.getPstm().setInt(4, compte.getClient().getId());
            db.getPstm().setString(5, compte.getType().name());
            db.getPstm().setString(6, compte.getEtat().name());
            ok = db.executeMaj();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }

    @Override
    public void associateCompte(Compte compte, Client client) {

    }

    @Override
    public double consultSolde(String numero) {
        return 0;
    }

    @Override
    public int closeCompte(String numero) {
        String sql = "UPDATE comptes SET etat = ? WHERE numero = ? AND balance = 0";
        int ok = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, Compte.EtatCompte.INACTIF.name());
            db.getPstm().setString(2, numero);
            ok = db.executeMaj();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }

    @Override
    public Compte getCompteByNumero(String numero) {
        String sql = "SELECT * FROM comptes WHERE numero = ?";
        Compte compte = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numero);
            rs = db.getPstm().executeQuery();
            if (rs.next()) {
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getDate("created_at"));
                compte.setType(Compte.TypeCompte.valueOf(rs.getString("type")));
                compte.setEtat(Compte.EtatCompte.valueOf(rs.getString("etat")));

                IClient clientDao = new ClientImpl();
                compte.setClient(clientDao.get(rs.getInt("client_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return compte;
    }

    @Override
    public Compte getCompteById(int id) {
        String sql = "SELECT * FROM comptes WHERE id = ?";
        Compte compte = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.getPstm().executeQuery();
            if (rs.next()) {
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getDate("created_at"));
                compte.setType(Compte.TypeCompte.valueOf(rs.getString("type")));
                compte.setEtat(Compte.EtatCompte.valueOf(rs.getString("etat")));

                IClient clientDao = new ClientImpl();
                compte.setClient(clientDao.get(rs.getInt("client_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return compte;
    }

    @Override
    public int updateCompte(Compte compte) {
        return 0;
    }

    @Override
    public int updateComptes(Compte compteExiste) {
        String sql = "UPDATE comptes SET balance=?, created_at=?, client_id=?, type=?, etat=? WHERE id=?";
        int ok = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, compteExiste.getBalance());
            db.getPstm().setDate(2, new java.sql.Date(compteExiste.getCreatedAt().getTime()));
            db.getPstm().setInt(3, compteExiste.getClient().getId());
            db.getPstm().setString(4, compteExiste.getType().name());
            db.getPstm().setString(5, compteExiste.getEtat().name());
            db.getPstm().setInt(6, compteExiste.getId());
            if (compteExiste.getBalance() == 0) {
                Notification.NotifError("Erreur", "Impossible de changer l'état car le solde est nul");
            } else {
                ok = db.executeMaj();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }

    @Override
    public int deleteCompte(String numero) {
        return 0;
    }

    public ObservableList<Client> getAllClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients ORDER BY nom";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect(sql);
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return clients;
    }

    @Override
    public void generateRelevePDF(Compte compte) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.newLineAtOffset(100, 700);
                content.showText("Relevé bancaire - Compte " + compte.getNumero());
                content.newLineAtOffset(0, -20);
                content.showText("Type de compte : " + compte.getType());
                content.newLineAtOffset(0, -20);
                content.showText("Solde : " + compte.getBalance());
                content.newLineAtOffset(0, -20);
                content.showText("État : " + compte.getEtat());
                content.endText();
            }

            doc.save("releve_" + compte.getNumero() + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllAccountNumbers() {
        List<String> accounts = new ArrayList<>();
        String sql = "SELECT numero FROM comptes";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect(sql);
            while (rs.next()) {
                accounts.add(rs.getString("numero"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return accounts;
    }
}