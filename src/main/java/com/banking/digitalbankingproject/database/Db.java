package com.banking.digitalbankingproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import static com.banking.digitalbankingproject.database.Constants.*;

public class Db {
    private static final Logger logger = Logger.getLogger(Db.class.getName());
    private Connection cnx;
    private PreparedStatement pstm;
    private ResultSet rs;
    private int ok;

    private void connect(){
        try{
            logger.info("Tentative de connexion à la base de données: " + URL);
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Connexion à la base de données réussie");
        }catch (Exception e){
            logger.severe("Erreur de connexion à la BD : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initPrepar(String sql){
        try{
            connect();
            pstm = cnx.prepareStatement(sql);
            logger.info("Préparation de la requête SQL: " + sql);
        }catch (Exception e){
            logger.severe("Erreur lors de la préparation de la requête: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ResultSet executeSelect(){
        rs = null;
        try{
            rs = pstm.executeQuery();
            logger.info("Exécution de la requête SELECT réussie");
        }catch (Exception e){
            logger.severe("Erreur lors de l'exécution de la requête SELECT: " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    public int executeMaj(){
        try{
            ok = pstm.executeUpdate();
            logger.info("Exécution de la requête de mise à jour réussie, " + ok + " ligne(s) affectée(s)");
        }catch (Exception e){
            logger.severe("Erreur lors de l'exécution de la requête de mise à jour: " + e.getMessage());
            e.printStackTrace();
        }
        return ok;
    }

    public void closeConnection(){
        try{
            if (cnx != null) {
                cnx.close();
                logger.info("Connexion à la base de données fermée");
            }
        }catch (Exception e){
            logger.severe("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
