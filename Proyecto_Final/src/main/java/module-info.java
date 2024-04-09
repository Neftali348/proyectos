module com.mycompany.proyecto_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.proyecto_final to javafx.fxml;
    exports com.mycompany.proyecto_final;
}
