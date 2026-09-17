
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
        nomBaseDatos = "pruebas";
        driver = "org.mariadb.jdbc.Driver";
        url = "jdbc:mariadb://localhost:3306/" + nomBaseDatos;
        usuario = "root";
        contraseña = "";
    }
    
    public static Conexion getInstancia(){
        if (instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }
    
    public Connection getConnection() throws Exception{
        // Si la conexión no existe o se cerró, abrimos una nueva
        if (con == null || con.isClosed()) {
            Class.forName(driver);
            con = DriverManager.getConnection(url, usuario, contraseña);            
        }
        return con;
    }
}

