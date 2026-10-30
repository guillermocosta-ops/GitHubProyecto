
package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {

    // Única instancia de la clase (Singleton). Singleton es un patrón de diseño que permite tener una sola instancia de una clase en todo el programa.
    private static final Conexion INSTANCIA = new Conexion();

    // Datos necesarios para conectarse a MariaDB.
    private final String base = "bd_prueba";
    private final String driver = "org.mariadb.jdbc.Driver";
    private final String url = "jdbc:mariadb://localhost:3306/" + base;
    private final String usuario = "root";
    private final String contrasena = "";

    // Constructor privado para evitar crear otras instancias.
    private Conexion() { }

    // Devuelve la instancia de Conexion.
    public static Conexion getInstancia() {
        return INSTANCIA;
    }

    // Abre una nueva conexión con MariaDB.
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontró el driver MariaDB", ex);
        }

        return DriverManager.getConnection(url, usuario, contrasena);
    }

    // Permite obtener una conexión desde otras clases.
    public static Connection getConexion() throws SQLException {
        return getInstancia().getConnection();
    }

    /*
     * EXPLICACIÓN GENERAL DE LA CLASE
     *
     * Esta clase se encarga de conectar nuestro programa Java
     * con la base de datos MariaDB.
     *
     * Primero tenemos los datos de conexión:
     * el nombre de la base, el driver, la dirección,
     * el usuario y la contraseña.
     *
     * Utilizamos el patrón Singleton para tener un único
     * objeto de la clase Conexion. Esto se consigue con
     * una instancia estática y un constructor privado.
     *
     * Cuando otra clase necesita acceder a la base de datos,
     * llama al método getConexion().
     *
     * Este método utiliza getConnection(), que primero
     * intenta cargar el driver de MariaDB mediante
     * Class.forName().
     *
     * Si no encuentra el driver, se lanza una excepción.
     * Si lo encuentra, DriverManager intenta conectarse
     * utilizando la dirección, el usuario y la contraseña.
     *
     * Si la conexión se establece correctamente,
     * se devuelve un objeto Connection que permite
     * ejecutar consultas SQL desde Persistencia.
     *
     * Aunque utilizamos Singleton, cada llamada a
     * getConexion() abre una conexión nueva.
     *
     * Esta clase solamente abre conexiones.
     * No guarda, modifica ni elimina información.
     * Esas operaciones las realiza Persistencia.java.
     */
}
