
package Controladores;

import Modelo.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.cell.PropertyValueFactory;
import main.ConexionBD;

public class LibrosController {

    @FXML
    private TableView<Libro> tablaLibros;
    @FXML
    private TableColumn<Libro, String> colISBN;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, Integer> colAnio;
    @FXML
    private TableColumn<Libro, String> colEditorial;
    @FXML
    private TableColumn<Libro, Integer> colCantidad;

    @FXML
    private TextField txISBN;
    @FXML
    private TextField txTitulo;
    @FXML
    private TextField txAutor;
    @FXML
    private TextField txAnio;
    @FXML
    private TextField txEditorial;
    @FXML
    private TextField txCantidad;

    private ObservableList<Libro> listaLibros;
    private Connection conn;

    private Libro[][] matrizLibros; // Matriz para almacenar los datos de libros recuperados de la base de datos

    public LibrosController() {
        conn = ConexionBD.getConnection();
        listaLibros = FXCollections.observableArrayList();
        cargarLibros();
    }

    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponibilidad"));

        tablaLibros.setItems(listaLibros);
    }

    @FXML
    private void handleGuardar() {
        String isbn = txISBN.getText();
        String titulo = txTitulo.getText();
        String autor = txAutor.getText();
        int anio = Integer.parseInt(txAnio.getText());
        String editorial = txEditorial.getText();
        int cantidad = Integer.parseInt(txCantidad.getText());

        // Agregar el libro al vector temporal
        Libro libroNuevo = new Libro(isbn, titulo, autor, anio, editorial, cantidad);
        listaLibros.add(libroNuevo);

        // Insertar el libro en la base de datos
        insertarLibro(libroNuevo);

        // Limpiar el formulario
        limpiarFormulario();
    }

    private void insertarLibro(Libro libro) {
        try {
            String sql = "INSERT INTO gestion_biblioteca.LIBROS (ISBN, TITULO, AUTOR, ANIO_PUBLICACION, EDITORIAL, CANTIDAD_DISPONIBILIDAD) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, libro.getIsbn());
            st.setString(2, libro.getTitulo());
            st.setString(3, libro.getAutor());
            st.setInt(4, libro.getAnioPublicacion());
            st.setString(5, libro.getEditorial());
            st.setInt(6, libro.getCantidadDisponibilidad());

            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LibrosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleOrdenarPorISBN() {
        listaLibros.sort(Comparator.comparing(Libro::getIsbn));
    }

    @FXML
    private void handleOrdenarPorTitulo() {
        listaLibros.sort(Comparator.comparing(Libro::getTitulo));
    }   

    @FXML
    private void handleOrdenarPorAutor() {
        listaLibros.sort(Comparator.comparing(Libro::getAutor));
    }

    @FXML
    public void handleEditar() {
        // Implementar la lógica de edición aquí
    }

    @FXML
    public void handleEliminar() {
        // Implementar la lógica de eliminación aquí
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        cargarVista("/vistas/Menu.fxml", sourceButton);
    }

    private void limpiarFormulario() {
        txISBN.clear();
        txTitulo.clear();
        txAutor.clear();
        txAnio.clear();
        txEditorial.clear();
        txCantidad.clear();
    }

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

    private void cargarLibros() {
        listaLibros.clear();

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM gestion_biblioteca.LIBROS";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String titulo = rs.getString("TITULO");
                String autor = rs.getString("AUTOR");
                int anio = rs.getInt("ANIO_PUBLICACION");
                String editorial = rs.getString("EDITORIAL");
                int cantidad = rs.getInt("CANTIDAD_DISPONIBILIDAD");

                Libro libro = new Libro(isbn, titulo, autor, anio, editorial, cantidad);
                listaLibros.add(libro);
            }
        } catch (SQLException ex) {
            System.out.println("ERROR::" + ex);
            Logger.getLogger(LibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar los recursos: ResultSet, PreparedStatement y la conexión
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LibrosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LibrosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
