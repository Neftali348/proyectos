/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rudy
 */
public class MenuController{

   
    @FXML
    private Label lblBienvenida;

     @FXML
    private Button btnCrearLibros;

    @FXML
    private Button btnPrestarLibros;

    @FXML
    private Button btnDevolverLibros;
    
    @FXML
    private Button btnCerrarSesion;

    
    // Método para manejar la acción de "Crear Libro"
    @FXML
    private void handleCrearLibro() {
        
        cargarVista("/vistas/Libros.fxml",btnCrearLibros);
    }

    // Método para manejar la acción de "Prestar Libro"
    @FXML
    private void handlePrestarLibro() {
        cargarVista("/vistas/PrestarLibro.fxml",btnPrestarLibros);
    }

    // Método para manejar la acción de "Devolver Libro"
    @FXML
    private void handleDevolverLibro() {
        cargarVista("/vistas/DevolverLibro.fxml",btnDevolverLibros);
    }
    
    @FXML
    private void handleCerrarSesion() {
        Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
        stage.close();
    }
    
     private void cargarVista(String fxmlPath, Button accion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load();
            Stage stage = (Stage) accion.getScene().getWindow();
            stage.setScene(new Scene(vista));
            stage.show();
        } catch (IOException e) {
            System.out.println("ERROR PRESENTADO::"+e);;
        }
    }

    // Método para establecer el mensaje de bienvenida
    public void setMensajeBienvenida(String usuario) {
        lblBienvenida.setText("Bienvenido, " + usuario + ", a la Biblioteca");
    }
}
