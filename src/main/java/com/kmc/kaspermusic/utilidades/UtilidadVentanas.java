package com.kmc.kaspermusic.utilidades;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UtilidadVentanas {

    /**
     * Variables privadas de la clase
     */
    private final Stage stageActual;
    private final String rutaVistaFXML;

    /**
     * Constructor con parámetros donde inicializo las dos variables declaradas
     * arriba
     *
     * @param stageActual
     * @param rutaVistaFXML
     */
    public UtilidadVentanas(Stage stageActual, String rutaVistaFXML) {
        this.stageActual = stageActual;
        this.rutaVistaFXML = rutaVistaFXML;
    }

    /**
     * Método privado el cual cambia a la ventana que recibe por los parámetros
     *
     * @param stageActual
     * @param rutaVistaFxml
     */
    private void cambiarVentanaActual(Stage stageActual, String rutaVistaFxml) {
        try {
            Parent root = FXMLLoader.load(UtilidadVentanas.class.getResource(rutaVistaFxml));
            Scene scene = new Scene(root);
            stageActual.setScene(scene);
            stageActual.centerOnScreen();
            stageActual.setResizable(false);
            stageActual.setTitle("KasperMusic");
            stageActual.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Unico método public el cual llama al método privado que cambia de ventana
     */
    public void cambiarVentana() {
        cambiarVentanaActual(stageActual, rutaVistaFXML);
    }

}
