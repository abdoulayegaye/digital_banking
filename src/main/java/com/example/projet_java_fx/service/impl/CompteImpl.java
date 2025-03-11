package com.example.projet_java_fx.service.impl;

import com.example.projet_java_fx.database.Db;
import com.example.projet_java_fx.entity.Clients;
import com.example.projet_java_fx.entity.Comptes;
import com.example.projet_java_fx.service.ICompte;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private Db db = new Db();
    private ResultSet rs;


    @Override
    public int create(Comptes compte) {
        String sql = "INSERT INTO comptes (numero, solde, date, id_client) VALUES (?,?,?,?)";
        int ok = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getSolde());
            db.getPstm().setTimestamp(3, compte.getDate());
            db.getPstm().setInt(4, compte.getIdclient().getId());

            ok = db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }

    @Override
    public double getSolde(int id) {
        String sql = "SELECT solde FROM comptes WHERE id = ?";
        double solde = 0.0;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                solde = rs.getDouble("solde");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return solde;
    }

        @Override
        public int closeAccount(int id) {
            String sql = "DELETE FROM comptes WHERE id = ?";
            int ok = 0;
            try {
                db.initPrepar(sql);
                db.getPstm().setInt(1, id);
                ok = db.executeMaj();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection();
            }
            return ok;
        }

    @Override
    public byte[] generateBankStatement(int id) {
        String sql = "SELECT * FROM comptes WHERE id = ?";
        byte[] statement = new byte[0];
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.setLeading(14.5f);
                        contentStream.newLineAtOffset(25, 700);
                        contentStream.showText("Bank Statement for Account ID: " + id);
                        contentStream.newLine();
                        contentStream.showText("Numero: " + rs.getString("numero"));
                        contentStream.newLine();
                        contentStream.showText("Solde: " + rs.getDouble("solde"));
                        contentStream.newLine();
                        contentStream.showText("Date: " + rs.getTimestamp("date"));
                        contentStream.newLine();
                        contentStream.showText("Client ID: " + rs.getInt("id_client"));
                        contentStream.endText();
                    }
                    document.save(baos);
                }
                statement = baos.toByteArray();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return statement;
    }

    @Override
    public int consultercompte(int Id) {
        String sql = "SELECT solde FROM comptes WHERE id = ?";
        int solde = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1,Id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                solde = rs.getInt("solde");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return solde;
    }

    @Override
    public List<Comptes> getAllComptess() {
        List<Comptes> Compte = new ArrayList<>();
        String sql = "SELECT * FROM COMPTES ";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Comptes comptes = new Comptes();
                comptes.setId(rs.getInt("id"));
                comptes.setNumero(rs.getString("numero"));
                comptes.setSolde(Integer.parseInt(rs.getString("solde")));
                comptes.setDate(Timestamp.valueOf(rs.getString("date")));
                Clients client = new Clients();
                client.setId(rs.getInt("id_client"));
                comptes.setIdclient(client);

//                comptesList.add(compte);
                Compte.add(comptes);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Compte; // Retourne la liste des clients
    }

    @Override
    public List<Comptes> getAllComptes() {
        String sql = "SELECT * FROM comptes";
        List<Comptes> comptesList = new ArrayList<>();
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Comptes compte = new Comptes();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setSolde((int) rs.getDouble("solde"));
                compte.setDate(rs.getTimestamp("date"));
                // Assuming there is a method to get a Client object from the client id
//                compte.setIdclient(getClient(rs.getInt("id_client")));
                comptesList.add(compte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return comptesList;
    }

    public int update(Comptes compte) {
        String sql = "UPDATE comptes SET numero = ?, solde = ?, date = ?, id_client = ? WHERE id = ?";
        int ok = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getSolde());
            db.getPstm().setTimestamp(3, compte.getDate());
            db.getPstm().setInt(4, compte.getIdclient().getId());
            db.getPstm().setInt(5, compte.getId());

            ok = db.executeMaj();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }
}
