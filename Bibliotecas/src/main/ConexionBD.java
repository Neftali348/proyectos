package main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author miguelcatalan
 */
public class ConexionBD {

    private static String URL = "jdbc:postgresql://roundhouse.proxy.rlwy.net:16298/postgres";
    private static Connection conn = null;
    private Properties properties = new Properties();
    private static final String USER = "postgres";
    private static final String PASSWORD = "tzByRdDEXlOfnFHXnewxvYAnwElYjtZy";
    private static final Logger logger = Logger.getLogger(ConexionBD.class.getName());


    private ConexionBD() {
        // Constructor privado para evitar la instanciación externa
    }

    public static Connection getConnection() {
        //if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");
                Properties props = new Properties();
                props.setProperty("user", USER);
                props.setProperty("password", PASSWORD);
                conn = DriverManager.getConnection(URL, props);
                System.out.println("DB conectada");
            } catch (ClassNotFoundException | SQLException ex) {
                System.out.println("ERROR al conectarse a la base de datos::: "+ex);
                logger.log(Level.SEVERE, "Error connecting to database", ex);
            }
            
       // }
        return conn;
    }
}
