/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import main.ConexionBD;

/**
 * FXML Controller class
 *
 * @author rudy
 */
public class SuscribirseController {

    @FXML
    private TextField txNit;

    @FXML
    private TextField txNombre;

    @FXML
    private TextField txDireccion;

    @FXML
    private TextField txTelefono;

    @FXML
    private Button btnSuscribirse;

    @FXML
    private Label lblMensaje;
    
    @FXML
    private TextField txContrasenia;

    @FXML
    private void handleSuscribirse() {
        String nit = txNit.getText();
        String nombre = txNombre.getText();
        String direccion = txDireccion.getText();
        String telefono = txTelefono.getText();
        String contrasenia = txContrasenia.getText();
        if (nit.isEmpty() || nombre.isEmpty()) {
            lblMensaje.setText("El NIT y el Nombre son obligatorios.");
            return;
        }

        // Crear un nuevo cliente y llamar al método insertarUsuario()
        Cliente cliente = new Cliente(nit, nombre, direccion, telefono, contrasenia);
        if (cliente.insertarUsuario()) {
            lblMensaje.setText("Usuario registrado exitosamente.");

            try {
                // Cargar el archivo FXML de la pantalla de suscripción
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Vista1.fxml"));
                Parent root = loader.load();

                // Obtener el escenario actual
                Stage stage = (Stage) btnSuscribirse.getScene().getWindow();

                // Configurar la nueva escena y mostrarla
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lblMensaje.setText("Error al registrar el usuario.");
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        Button sourceButton = (Button) event.getSource(); // Obtener el botón que disparó el evento
        cargarVista("/vistas/Vista1.fxml", sourceButton);
    }

    @FXML
    private void cargarVista(String fxmlPath, Button accion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load();
            Stage stage = (Stage) accion.getScene().getWindow();
            stage.setScene(new Scene(vista));
            stage.show();
        } catch (IOException e) {
            System.out.println("ERROR PRESENTADO::" + e);
        }
    }
}