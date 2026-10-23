package Persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class Conexion {
    private static final Conexion INSTANCIA = new Conexion();
    private final String base = "bd_prueba";
    private final String driver = "org.mariadb.jdbc.Driver";
    private final String url = "jdbc:mariadb://localhost:3306/" + base;
    private final String usuario = "root";
    private final String contrasena = "";
    private Conexion() { }
    public static Conexion getInstancia() { return INSTANCIA; }
    public Connection getConnection() throws SQLException {
        try { Class.forName(driver); }
        catch (ClassNotFoundException ex) { throw new SQLException("No se encontró el driver MariaDB", ex); }
        return DriverManager.getConnection(url,usuario,contrasena);
    }
    public static Connection getConexion() throws SQLException { return getInstancia().getConnection(); }
}
