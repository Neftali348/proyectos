package main;



import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author rudy
 */
public class Main extends Application {
    @FXML
    private Stage stage;
   
    @Override
    public void start (Stage primaryStage) throws Exception{
            try{
            FXMLLoader cargar = new FXMLLoader(); 
            cargar.setLocation (Main.class.getResource ("/Vistas/Vista1.fxml"));
            Pane Main = (Pane) cargar.load();
            Scene s= new Scene (Main); 
            primaryStage.setScene (s);
            primaryStage. show ();
            }catch (Exception e){
            e.printStackTrace();
            }
}
  
    @FXML
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
