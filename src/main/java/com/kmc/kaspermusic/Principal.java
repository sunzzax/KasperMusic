package com.kmc.kaspermusic;

import com.kmc.kaspermusic.utilidades.UtilidadConexionBD;
import com.kmc.kaspermusic.utilidades.UtilidadRutasFxml;
import com.kmc.kaspermusic.utilidades.UtilidadVentanas;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author marti
 */
public class Principal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Creo una instancia de la clase UtilidadVentanas para poder mostrar la
         * ventana principal
         */
        UtilidadVentanas cv = new UtilidadVentanas(primaryStage, UtilidadRutasFxml.VISTA_SESION);
        cv.cambiarVentana();
        UtilidadConexionBD conexionBD = new UtilidadConexionBD();
        conexionBD.realizarConexion();
    }

    /**
     * Método main el cual lanza el programa
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

}
