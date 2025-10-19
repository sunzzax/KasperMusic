package com.kmc.kaspermusic.utilidades;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
            stageActual.setMaximized(true);
            stageActual.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/logo.png")));
            stageActual.setMinWidth(640);
            stageActual.setMinHeight(440);
            stageActual.setFullScreenExitHint("");
            stageActual.setTitle("KasperMusic");
            stageActual.show();
            
            /**
             * Al presionar F11  se invierte el estado de pantalla completa
             * Si la ventana NO esta en pantalla completa (false), entra en pantalla completa (true)
             * Si ya esta en pantalla completa (true) vuelve a modo ventana (false)
             */
            scene.setOnKeyPressed((event) -> {
                switch (event.getCode()) {
                    case F11 -> stageActual.setFullScreen(!stageActual.isFullScreen());
                }
            });
            
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
