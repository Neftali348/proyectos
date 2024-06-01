/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author rudy
 */
public class DatosTemporales {
    
    private static DatosTemporales instance;
    String usuarioLogueado;

    
    public static synchronized DatosTemporales getInstance() {
        if (instance == null) {
            instance = new DatosTemporales();
        }
        return instance;
    }
    
    public String getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(String usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    
    
}
