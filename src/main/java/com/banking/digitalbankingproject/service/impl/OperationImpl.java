package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.Utils.DatabaseConnection;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private final Connection connection;
    private final CompteImpl compteService;

    public OperationImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.compteService = new CompteImpl();
    }

    @Override
    public void deposit(String numeroCompte, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }

        try {
            connection.setAutoCommit(false);

            // First update the balance
            String updateBalanceSql = "UPDATE comptes SET balance = balance + ? WHERE numero = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateBalanceSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setString(2, numeroCompte);
                int updated = pstmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Compte non trouvé");
                }
            }

            // Then record the operation
            String insertOperationSql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, (SELECT id FROM comptes WHERE numero = ?))";
            try (PreparedStatement pstmt = connection.prepareStatement(insertOperationSql)) {
                pstmt.setTimestamp(1, Timestamp.from(Instant.now()));
                pstmt.setDouble(2, amount);
                pstmt.setString(3, TypeOperation.DEPOT.toString());
                pstmt.setString(4, numeroCompte);
                pstmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erreur lors de l'annulation de la transaction", ex);
            }
            throw new RuntimeException("Erreur lors du dépôt", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la réinitialisation de l'auto-commit", e);
            }
        }
    }

    @Override
    public void withdraw(String numeroCompte, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }

        try {
            connection.setAutoCommit(false);

            // First check the balance
            double currentBalance = compteService.getBalance(numeroCompte);
            if (currentBalance < amount) {
                throw new IllegalStateException("Solde insuffisant");
            }

            // Then update the balance
            String updateBalanceSql = "UPDATE comptes SET balance = balance - ? WHERE numero = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateBalanceSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setString(2, numeroCompte);
                int updated = pstmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Compte non trouvé");
                }
            }

            // Finally record the operation
            String insertOperationSql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, (SELECT id FROM comptes WHERE numero = ?))";
            try (PreparedStatement pstmt = connection.prepareStatement(insertOperationSql)) {
                pstmt.setTimestamp(1, Timestamp.from(Instant.now()));
                pstmt.setDouble(2, amount);
                pstmt.setString(3, TypeOperation.RETRAIT.toString());
                pstmt.setString(4, numeroCompte);
                pstmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erreur lors de l'annulation de la transaction", ex);
            }
            throw new RuntimeException("Erreur lors du retrait", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la réinitialisation de l'auto-commit", e);
            }
        }
    }

    //@Override
    public List<Operation> getAccountHistory(String numeroCompte) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT o.*, c.numero as compte_numero " +
                "FROM operations o " +
                "JOIN comptes c ON o.compte_id = c.id " +
                "WHERE c.numero = ? " +
                "ORDER BY o.date_op DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroCompte);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDateOp(rs.getTimestamp("date_op").toInstant());
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));

                Compte compte = compteService.getCompte(numeroCompte);
                operation.setCompte(compte);

                operations.add(operation);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'historique", e);
        }
        return operations;
    }

    @Override
    public String generatePdfStatement(String numeroCompte, String directory) {
        try {
            Compte compte = compteService.getCompte(numeroCompte);
            String fileName = String.format("releve_%s_%s.pdf",
                    compte.getNumero(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
            String filePath = directory + File.separator + fileName;

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add header
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("Relevé de compte", titleFont));
            document.add(new Paragraph("Numéro de compte: " + compte.getNumero(), normalFont));
            document.add(new Paragraph("Client: " + compte.getClient().getNom() + " " + compte.getClient().getPrenom(), normalFont));
            document.add(new Paragraph("Solde actuel: " + String.format("%.2f FCFA", compte.getBalance()), normalFont));
            document.add(new Paragraph("\n"));

            // Create table
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add headers
            String[] headers = {"Date", "Type", "Montant"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setBorderWidth(2);
                table.addCell(cell);
            }

            // Add operations
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Operation op : getAccountHistory(numeroCompte)) {
                table.addCell(op.getDateOp()
                        .atZone(ZoneId.systemDefault())
                        .format(formatter));
                table.addCell(op.getType().toString());
                table.addCell(String.format("%.2f FCFA", op.getAmount()));
            }

            document.add(table);
            document.close();

            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du relevé PDF", e);
        }
    }

    @Override
    public void effectuerDepot(String numero, double montant) {

    }

    @Override
    public void effectuerRetrait(String numero, double montant) {

    }

    @Override
    public String genererReleve(String numero, String absolutePath) {
        return null;
    }

    @Override
    public Operation getOperationsCompte(String numero) {
        return null;
    }
}
