/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.ConexionBD;

// Abstracción
abstract class Usuario {
    protected String nit;
    protected String nombre;
    protected String direccion;
    protected String telefono;
    protected String contrasenia;

    // Constructor
    public Usuario(String nit, String nombre, String direccion, String telefono, String contrasenia) {
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
    }

    // Método abstracto para insertar un usuario en la base de datos
    public abstract boolean insertarUsuario();
}

// Encapsulamiento
class Cliente extends Usuario {
    public Cliente(String nit, String nombre, String direccion, String telefono, String contrasenia) {
        super(nit, nombre, direccion, telefono, contrasenia);
    }

    // Método para insertar un cliente en la base de datos
    @Override
    public boolean insertarUsuario() {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "INSERT INTO gestion_biblioteca.usuario (NIT, NOMBRE, DIRECCION, TELEFONO, CONTRASENIA) VALUES (?, ?, ?, ?, ?)";
            st = conn.prepareStatement(sql);
            st.setString(1, nit);
            st.setString(2, nombre);
            st.setString(3, direccion);
            st.setString(4, telefono);
            st.setString(5, contrasenia);

            int rowsInserted = st.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            System.out.println("ERROR:::" + ex);
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}