/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kmc.kaspermusic.utilidades;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marti
 */
public class UtilidadConexionBD {

    private Connection conexion;
    private String rutaBD = "src/main/resources/kasper.db";

    // Método privado para realizar la conexión a la base de datos
    public void realizarConexion() {
        try {
            // Ruta a mi base de datos
            String rutaBD = "jdbc:sqlite:" + this.rutaBD;

            // Archivo físico de la base de datos
            File kasperDB = new File(this.rutaBD);
            // Comprueba si el archivo existe
            boolean existeBD = kasperDB.exists();

            // Establecer conexión con la base de datos
            conexion = DriverManager.getConnection(rutaBD);
            System.out.println("Se ha realizado la conexión con la base de datos exitosamente.");

            if (!existeBD) {
                crearTablas();
                System.out.println("Se han creado las tablas en la base de datos.");
            }

        } catch (SQLException ex) {
            System.err.println("ERROR al intentar contectar con la base de datos: " + ex.getMessage());
        }
    }

    // Método publico para obtener la conexión y hacer uso de ella en otras clases
    public Connection getConexion() {
        if (conexion != null) {
            realizarConexion(); // Si no hay conexión la crea con este método
        }
        return conexion;
    }

    // Método publico para cerrar la conexión cuando ya no se use
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException ex) {
            System.err.println("ERROR al cerrar la conexión: " + ex.getMessage());
        }
    }

    // Método privado para crear las tablas
    private void crearTablas() {
        try (Statement consulta = conexion.createStatement()) {

            // TABLA USUARIOS
            String tablaUsuarios = """
                                   CREATE TABLE IF NOT EXISTS usuarios (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        nombre TEXT NOT NULL, UNIQUE,
                                        contrasena TEXT NOT NULL,
                                        rol TEXT NOT NULL
                                   );
                                   """;

            // TABLA CANCIONES
            String tablaCanciones = """
                                    CREATE TABLE IF NOT EXISTS canciones (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        titulo TEXT NOT NULL,
                                        artista TEXT NOT NULL,
                                        rutaCancion TEXT NOT NULL
                                    );
                                    """;

            // TABLA FAVORITOS
            String tablaFavoritos = """
                                    CREATE TABLE IF NOT EXISTS favoritos (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        usuario_id INTEGER NOT NULL,
                                        cancion_id INTEGER NOT NULL,
                                        FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                                        FOREIGN KEY (cancion_id) REFERENCES canciones(id)  ON DELETE CASCADE
                                    );
                                    """;

            consulta.execute(tablaCanciones);
            consulta.execute(tablaUsuarios);
            consulta.execute(tablaFavoritos);

            /**
             * Llamo al método privado insertarDatos para que añada campos si no
             * ha fallado en la creacion de la tabla
             */
            insertarDatos();

        } catch (SQLException ex) {
            System.err.println("ERROR al intentar crear las tablas: " + ex.getMessage());
        }
    }

    private void insertarDatos() {
        /**
         * TODO: Insertar datos en las tablas para que si no existe la base de
         * datos se inserten los valores iniciales en cada tabla
         */
    }

}
