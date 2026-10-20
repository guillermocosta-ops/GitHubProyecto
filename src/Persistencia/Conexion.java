
package Persistencia;

import java.util.*;
import java.sql.*;

public class Conexion {
    private static Conexion instancia;
    
    private Connection con;
    private String driver;
    private String url;
    private String usuario;
    private String contraseña;
    private String nomBaseDatos;
    
    private Conexion(){
        nomBaseDatos = "bd_prueba";
        driver = "org.mariadb.jdbc.Driver";
        url = "jdbc:mariadb://localhost:3307/" + nomBaseDatos;
        usuario = "root";
        contraseña = "";
    }
    public static Connection getConexion() throws SQLException {
    return getInstancia().getConnection();
    }
    
    public static Conexion getInstancia(){
        if (instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }
    
    public Connection getConnection() throws SQLException {
        try {
            if (con == null || con.isClosed()) {
                Class.forName(driver);
                con = DriverManager.getConnection(url, usuario, contraseña);            
            }
            return con;
        } catch (Exception e) {
            throw new SQLException("Error al conectar a la base de datos: " + e.getMessage(), e);
        }
    }
}


