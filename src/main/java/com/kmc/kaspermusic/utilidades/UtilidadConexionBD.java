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

/**
 *
 * @author marti
 */
public class UtilidadConexionBD {

    private Connection conexion;
    private final String rutaBD = "archivobd/kasper.db";

    // Método privado para realizar la conexión a la base de datos
    public void realizarConexion() {
        try {
            // Archivo físico de la base de datos
            File kasperDB = new File(this.rutaBD);
            // Comprueba si el archivo existe
            boolean existeBD = kasperDB.exists();

            // Establecer conexión con la base de datos
            conexion = DriverManager.getConnection("jdbc:sqlite:" + this.rutaBD);
            System.out.println("Se ha realizado la conexión con la base de datos exitosamente.");

            // Creo una consulta la cual activa las claves foraneas por si acaso estan desactivadas
            try (Statement consultaFK = conexion.createStatement()) {
                consultaFK.execute("PRAGMA foreign_keys = ON");
            }

            /**
             * Si la base de datos no existe llamo a un método el cual crea las
             * tablas iniciales y otro método el cual añade contenido inicial a
             * las tablas
             */
            if (!existeBD) {
                crearTablas();
                insertarDatos();
                System.out.println("Se han creado las tablas en la base de datos.");
            }

        } catch (SQLException ex) {
            System.err.println("ERROR al intentar contectar con la base de datos: " + ex.getMessage());
        }
    }

    // Método publico para obtener la conexión y hacer uso de ella en otras clases
    public Connection getConexion() {
        if (conexion == null) {
            realizarConexion(); // Si no hay conexión la crea con este método
        }
        return conexion;
    }

    // Método publico para cerrar la conexión cuando ya no se use
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                conexion = null;
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException ex) {
            System.err.println("ERROR al cerrar la conexión: " + ex.getMessage());
        }
    }

    // Método privado para crear las tablas
    private void crearTablas() {
        try (Statement consultaTablas = conexion.createStatement()) {

            // TABLA USUARIOS
            String tablaUsuarios = """
                                   CREATE TABLE IF NOT EXISTS usuarios (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        nombre TEXT NOT NULL UNIQUE,
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
                                        genero TEXT NOT NULL,
                                        rutaArchivo TEXT NOT NULL,
                                        rutaImagen TEXT NOT NULL,
                                        UNIQUE(titulo, artista)
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

            // Lanzo las consultas 
            consultaTablas.execute(tablaUsuarios);
            consultaTablas.execute(tablaCanciones);
            consultaTablas.execute(tablaFavoritos);

        } catch (SQLException ex) {
            System.err.println("ERROR al intentar crear las tablas: " + ex.getMessage());
        }
    }

    private void insertarDatos() {
        try (Statement consultaDatos = conexion.createStatement()) {

            /**
             * Desactivo el autocommit para agrupar todos los inserts en una
             * sola transacción.
             *
             * Con autocommit activado, cada insert se escribe en disco
             * inmediatamente, lo que es más lento. Al desactivarlo, los cambios
             * se acumulan en memoria y se escriben juntos al llamar a commit(),
             * lo que acelera la operación.
             *
             * Si ocurre un error, se puede hacer rollback() para deshacer todos
             * los cambios. Finalmente, se vuelve a activar el autocommit para
             * que futuras operaciones funcionen normalmente.
             */
            conexion.setAutoCommit(false);

            String insertarDatos[] = {
                "INSERT OR IGNORE INTO usuarios (nombre, contrasena, rol) VALUES ('ricardo', '1234', 'usuario')",
                "INSERT OR IGNORE INTO usuarios (nombre, contrasena, rol) VALUES ('admin', '1234', 'administrador')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Blinding Lights', 'The Weeknd', 'Pop', '/canciones/blinding_lights.mp3', '/imagenes/canciones/blinding_lights.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Shape of You', 'Ed Sheeran', 'Pop', '/canciones/shape_of_you.mp3', '/imagenes/canciones/shape_of_you.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Levitating', 'Dua Lipa', 'Pop', '/canciones/levitating.mp3', '/imagenes/canciones/levitating.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Uptown Funk', 'Bruno Mars', 'Pop', '/canciones/uptown_funk.mp3', '/imagenes/canciones/uptown_funk.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Rolling in the Deep', 'Adele', 'Pop', '/canciones/rolling_in_the_deep.mp3', '/imagenes/canciones/rolling_in_the_deep.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Bad Romance', 'Lady Gaga', 'Pop', '/canciones/bad_romance.mp3', '/imagenes/canciones/bad_romance.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Happy', 'Pharrell Williams', 'Pop', '/canciones/happy.mp3', '/imagenes/canciones/happy.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Bohemian Rhapsody', 'Queen', 'Rock', '/canciones/bohemian_rhapsody.mp3', '/imagenes/canciones/bohemian_rhapsody.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Hotel California', 'Eagles', 'Rock', '/canciones/hotel_california.mp3', '/imagenes/canciones/hotel_california.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Stairway to Heaven', 'Led Zeppelin', 'Rock', '/canciones/stairway_to_heaven.mp3', '/imagenes/canciones/stairway_to_heaven.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Smells Like Teen Spirit', 'Nirvana', 'Rock', '/canciones/smells_like_teen_spirit.mp3', '/imagenes/canciones/smells_like_teen_spirit.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Sweet Child O Mine', 'Guns N Roses', 'Rock', '/canciones/sweet_child_o_mine.mp3', '/imagenes/canciones/sweet_child_o_mine.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Highway to Hell', 'AC/DC', 'Rock', '/canciones/highway_to_hell.mp3', '/imagenes/canciones/highway_to_hell.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Wonderwall', 'Oasis', 'Rock', '/canciones/wonderwall.mp3', '/imagenes/canciones/wonderwall.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Claro de Luna', 'Beethoven', 'Clasica', '/canciones/claro_de_luna.mp3', '/imagenes/canciones/claro_de_luna.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Sinfonía Nº5', 'Beethoven', 'Clasica', '/canciones/sinfonia_5.mp3', '/imagenes/canciones/sinfonia_5.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('El Lago de los Cisnes', 'Tchaikovsky', 'Clasica', '/canciones/lago_de_los_cisnes.mp3', '/imagenes/canciones/lago_de_los_cisnes.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Canon en Re', 'Pachelbel', 'Clasica', '/canciones/canon_en_re.mp3', '/imagenes/canciones/canon_en_re.png')",
                "INSERT OR IGNORE INTO canciones (titulo, artista, genero, rutaArchivo, rutaImagen) VALUES('Réquiem', 'Mozart', 'Clasica', '/canciones/requiem_mozart.mp3', '/imagenes/canciones/requiem_mozart.png')"
            };

            // Ejecuto todos los INSERTS dentro de la transacción
            for (String insertarDato : insertarDatos) {
                consultaDatos.executeUpdate(insertarDato);
            }

            /**
             * Aquí confirmo todas las operaciones ejecutadas dentro de la
             * transacción y si todo ha ido bien, los datos se guardan en la
             * base de datos .
             */
            conexion.commit();
            System.out.println("Datos ingresados correctamente en las tablas.");

        } catch (SQLException ex) {
            try {
                /**
                 * Si llegase a ocurrir algo durante los INSERTS deshago todos
                 * los cambios hechos dentro de la transacción para no dejar la
                 * base de datos a medias.
                 */
                conexion.rollback();
            } catch (SQLException e) {
                System.err.println("Error al hacer rollback: " + e.getMessage());
            }
            System.err.println("ERROR al intentar insertar datos en las tablas: " + ex.getMessage());
        } finally {
            /**
             * Aquí restauro el autocommit, es importante para otras operaciones
             * que haga mas adelante fuera de esta transacción.
             */
            try {
                // Restauro el autocommit
                conexion.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("ERROR al intentar restaurar el autocommit: " + e.getMessage());
            }
        }
    }

}
