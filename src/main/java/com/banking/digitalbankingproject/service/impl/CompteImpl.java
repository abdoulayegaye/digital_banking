package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
//import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/digital_banking_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private final IOperation operationService = new OperationImpl();

    @Override
    public void creerCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, compte.getNumero());
            pstmt.setDouble(2, compte.getSolde());
            pstmt.setTimestamp(3, compte.getDateOuverture());
            pstmt.setInt(4, compte.getClientId());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    compte.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur création compte : " + e.getMessage());
        }
    }

    @Override
    public List<Compte> listerComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM comptes";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                comptes.add(new Compte(
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getDouble("balance"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("client_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur listage comptes : " + e.getMessage());
        }
        return comptes;
    }

    @Override
    public Compte consulterCompte(int id) {
        String sql = "SELECT * FROM comptes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Compte(
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getDouble("balance"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("client_id")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur consultation compte : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void fermerCompte(int id) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur fermeture compte : " + e.getMessage());
        }
    }

    @Override
    public void genererPdf(int id) {
        Compte compte = consulterCompte(id);
        if (compte == null) {
            System.out.println("Compte non trouvé pour l'ID : " + id);
            return;
        }

        try {
            String dest = "compte_" + compte.getNumero() + "_rapport.pdf";
            PdfWriter writer = new PdfWriter(new File(dest));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Rapport du Compte").setBold().setFontSize(18));
            document.add(new Paragraph("Numéro : " + compte.getNumero()));
            document.add(new Paragraph("Solde : " + compte.getSolde() + " €"));
            document.add(new Paragraph("Date de création : " +
                    new SimpleDateFormat("dd/MM/yyyy").format(compte.getDateOuverture())));
            document.add(new Paragraph("Client ID : " + compte.getClientId()));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Historique des Opérations").setBold().setFontSize(14));
            List<Operation> operations = operationService.listerOperationsParCompte(id);
            if (operations.isEmpty()) {
                document.add(new Paragraph("Aucune opération."));
            } else {
                Table table = new Table(UnitValue.createPercentArray(new float[]{20, 20, 40, 20}));
                table.setWidth(UnitValue.createPercentValue(100));
                table.addHeaderCell("date_op");
                table.addHeaderCell("amount");
                table.addHeaderCell("type");
                table.addHeaderCell("Compte_id");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                for (Operation op : operations) {
                    table.addCell(op.getType());
                    table.addCell(String.valueOf(op.getMontant()));
                    table.addCell(dateFormat.format(op.getDateOperation()));
                    table.addCell(String.valueOf(op.getCompteId()));
                }
                document.add(table);
            }

            document.close();
            System.out.println("PDF généré : " + dest);
        } catch (Exception e) {
            System.out.println("Erreur génération PDF : " + e.getMessage());
        }
    }

    @Override
    public void depot(int compteId, double montant) {
        String sql = "UPDATE comptes SET balance = balance + ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, montant);
            pstmt.setInt(2, compteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur dépôt : " + e.getMessage());
        }
    }

    @Override
    public void retrait(int compteId, double montant) {
        String sql = "UPDATE comptes SET balance = balance - ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, montant);
            pstmt.setInt(2, compteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur retrait : " + e.getMessage());
        }
    }

    @Override
    public void virement(int compteSourceId, int compteDestId, double montant) {
        retrait(compteSourceId, montant);
        depot(compteDestId, montant);
    }
}