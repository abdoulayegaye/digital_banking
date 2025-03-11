package com.example.projet_java_fx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Db {
        // pour la connexion
        private Connection cnx;
        // pour les requetes preparées
        private PreparedStatement pstm;
        // pour les requetes de consultation (SELECT)
        private ResultSet rs;
        // pour les requetes de mise a jour (insert ,update ,delete)
        private int ok;

        //methode d'ouverture de la connexion
        public Connection getConnection() {
            //parametres de Connexion
            String url ="jdbc:postgresql://localhost:5432/gestionFx";
            String user="postgres";
            String password="Bamby1998";
            try {
                Class.forName("org.postgresql.Driver");
                cnx = DriverManager.getConnection(url, user, password);
                System.out.println("Connexion reussie");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cnx;
        }
        public void initPrepar(String sql){
            try {
                getConnection();
                pstm=cnx.prepareStatement(sql);
             }catch (Exception e){
                e.printStackTrace();
            }
        }

        public ResultSet executeSelect(){
            rs = null;
            try {
                rs=pstm.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rs;
        }

        public int executeMaj(){
            try {
                ok=pstm.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ok;
        }

        public void closeConnection(){
            try {
                if (cnx != null) {
                    cnx.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public PreparedStatement getPstm() {
            return pstm;
        }
    }