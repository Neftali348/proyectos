package Controladores;

import Modelo.DatosTemporales;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.ConexionBD;

public class Vista1Controller {


    ConexionBD conn;

    @FXML
    private Button butIniciarsesion;

    @FXML
    private TextField txNit;

    @FXML
    private TextField txContraseña;

    @FXML
    private Label lblMensaje; // Un Label para mostrar mensajes al usuario

    private Stage stage;

    @FXML
    private Button butSuscribete; // Declaración del botón "Suscríbete ahora"

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleIniciarSesion() {
        String correo = txNit.getText();
        String contraseña = txContraseña.getText();
        System.out.println("DATOS INGRESADOS:::" + correo + "CONTRA" + contraseña + "***");

        if (correo.isEmpty() || contraseña.isEmpty()
                || correo.equals("") || contraseña.equals("")) {
            lblMensaje.setText("Por favor, complete todos los campos.");
            return;
        }

        if (autenticarUsuario(correo, contraseña)) {
            lblMensaje.setText("Inicio de sesión exitoso.");
            // Aquí podrías cambiar de vista o abrir una nueva ventana
            try {
                // Cargar el archivo FXML de la pantalla de suscripción
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Menu.fxml"));
                Parent root = loader.load();

                // Obtener el escenario actual
                Stage stage = (Stage) butSuscribete.getScene().getWindow();

                // Configurar la nueva escena y mostrarla
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lblMensaje.setText("Correo o contraseña incorrectos.");
        }
    }

    private boolean autenticarUsuario(String correo, String contraseña) {
        System.out.println("ENTRA A AUTENTICAR");
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT * FROM gestion_biblioteca.usuario WHERE NIT = ? AND contrasenia = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, correo);
            st.setString(2, contraseña);
            rs = st.executeQuery();
            DatosTemporales.getInstance().setUsuarioLogueado(correo);
            return rs.next(); // Si se encuentra una fila, el usuario está autenticado

        } catch (SQLException ex) {
            Logger.getLogger(Vista1Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            // Cerrar los recursos: ResultSet, PreparedStatement y la conexión
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LibrosController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SuscribirseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void handleSuscribirse() {
      

        try {
            // Cargar el archivo FXML de la pantalla de suscripción
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Suscribirse.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) butSuscribete.getScene().getWindow();

            // Configurar la nueva escena y mostrarla
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
