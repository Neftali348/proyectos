
package Modelo;

public class Libro {

    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;
    private String editorial;
    private int cantidadDisponibilidad;

    public Libro(String isbn, String titulo, String autor, int anioPublicacion, String editorial, int cantidadDisponibilidad) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.editorial = editorial;
        this.cantidadDisponibilidad = cantidadDisponibilidad;
    }

    // Getters y setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getCantidadDisponibilidad() {
        return cantidadDisponibilidad;
    }

    public void setCantidadDisponibilidad(int cantidadDisponibilidad) {
        this.cantidadDisponibilidad = cantidadDisponibilidad;
    }
}
