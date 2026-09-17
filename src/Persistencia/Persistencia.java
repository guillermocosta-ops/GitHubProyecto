
package Persistencia;

import java.util.*;
import java.sql.*;

public class Persistencia {
    
    public ListadoProductos devolverProducto() throws Exception {
    Conexion DB = Conexion.getInstancia();
    Connection conn = DB.getConnection();

    String sql = "SELECT nombre, precio FROM productos";
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(sql);
    ListadoProductos Listado = ListadoProductos.getInstancia();
    
    while (rs.next()){
        Producto p = new Producto();

        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));

        Listado.agregarProducto(p);
    }
    
    conn.close();

    return Listado;
}
    public void IngresarDatosProducto(String nombre, double precio) throws Exception {
        Conexion DB = Conexion.getInstancia();
        Connection conn = DB.getConnection();
        String insertar = "INSERT INTO productos(nombre, precio) VALUES('" + nombre + "', " + precio + ")";
        //System.out.println(insertar);
        PreparedStatement st = conn.prepareStatement(insertar);
        st.executeUpdate();
        conn.close();
    }
    
}
