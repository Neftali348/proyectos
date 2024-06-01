package Controladores;

import Modelo.DatosTemporales;
import Modelo.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.cell.PropertyValueFactory;
import main.ConexionBD;

public class PrestarLibroController {

    @FXML
    private TextField filtroTextField;

    @FXML
    private ListView<LibroSeleccionado> seleccionadosListView;

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

    private ObservableList<Libro> listaLibros;
    private ObservableList<LibroSeleccionado> librosSeleccionados;

    public PrestarLibroController() {
        listaLibros = FXCollections.observableArrayList();
        librosSeleccionados = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponibilidad"));

        cargarLibrosDisponibles();

        // Agrega el manejador de eventos para la selección de la tabla
        tablaLibros.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic para seleccionar libro
                Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
                if (libroSeleccionado != null) {
                    agregarLibroSeleccionado(libroSeleccionado);
                }
            }
        });

        // Vincula la lista observable con el ListView
        seleccionadosListView.setItems(librosSeleccionados);

        // Agrega el manejador de eventos para el filtroTextField
        filtroTextField.setOnAction(event -> filtrarYSeleccionarLibro());
    }

    @FXML
    private void cargarLibrosDisponibles() {
        Connection conn = ConexionBD.getConnection();
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

            tablaLibros.setItems(listaLibros);
        }
    }

    private void agregarLibroSeleccionado(Libro libro) {
        // Buscar si el libro ya está en la lista seleccionada
        for (LibroSeleccionado libroSeleccionado : librosSeleccionados) {
            if (libroSeleccionado.getLibro().equals(libro)) {
                // Incrementar la cantidad si ya está en la lista
                libroSeleccionado.setCantidad(libroSeleccionado.getCantidad() + 1);
                seleccionadosListView.refresh();
                return;
            }
        }

        // Si no está en la lista, agregarlo con cantidad 1
        librosSeleccionados.add(new LibroSeleccionado(libro, 1));
    }

    private void filtrarYSeleccionarLibro() {
        String filtro = filtroTextField.getText().trim().toLowerCase();

        if (!filtro.isEmpty()) {
            for (Libro libro : listaLibros) {
                if (libro.getTitulo().toLowerCase().contains(filtro)) {
                    tablaLibros.getSelectionModel().select(libro);
                    tablaLibros.scrollTo(libro); // Asegurarse de que el libro esté visible en la tabla
                    return;
                }
            }
        }
    }

    @FXML
    private void prestarLibros() {
        Connection conn = null;
        PreparedStatement st = null;
        String nit = DatosTemporales.getInstance().getUsuarioLogueado();
        try {
            ResultSet rs = null;
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // Iniciar una transacción
            // Insertar en la tabla transaccion
            String sql1 = "insert into gestion_biblioteca.transaccion (nit,estatus) values(?,?)";
            st = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, nit);
            st.setString(2, "1");
            st.executeUpdate();

            // Obtener el ID de la transacción generada
            rs = st.getGeneratedKeys();
            int idTransaccion = 0;
            if (rs.next()) {
                idTransaccion = rs.getInt(1);
                System.out.println("ID de la transacción: " + idTransaccion);
            }

            // Cerrar el ResultSet
            rs.close();
            rs = null;

            // Actualizar la cantidad de libros y crear detalle de transacción
            for (LibroSeleccionado libroSeleccionado : librosSeleccionados) {
                Libro libro = libroSeleccionado.getLibro();
                int cantidadSeleccionada = libroSeleccionado.getCantidad();

                // Actualizar la disponibilidad de libros
                String sql2 = "UPDATE gestion_biblioteca.LIBROS SET CANTIDAD_DISPONIBILIDAD = CANTIDAD_DISPONIBILIDAD - ? WHERE ISBN = ?";
                st = conn.prepareStatement(sql2);
                st.setInt(1, cantidadSeleccionada);
                st.setString(2, libro.getIsbn());
                st.executeUpdate();

                // Insertar en la tabla detalle_transaccion
                String sql3 = "insert into gestion_biblioteca.detalle_transaccion (id_transaccion,id_libro) values(?,?)";
                st = conn.prepareStatement(sql3);
                st.setInt(1, idTransaccion);
                st.setString(2, libro.getIsbn());
                st.executeUpdate();
            }
            
            conn.commit();
            // Limpiar la lista de libros seleccionados
            librosSeleccionados.clear();
            seleccionadosListView.refresh();

            // Recargar los libros disponibles en la tabla
            cargarLibrosDisponibles();
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir la transacción en caso de error
                } catch (SQLException ex1) {
                    Logger.getLogger(PrestarLibroController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            Logger.getLogger(PrestarLibroController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PrestarLibroController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PrestarLibroController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void txcancelar(ActionEvent event) {
        Button sourceButton = (Button) event.getSource(); // Obtener el botón que disparó el evento
        cargarVista("/vistas/Menu.fxml", sourceButton);
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
