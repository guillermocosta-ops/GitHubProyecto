
package Persistencia;
import Logica.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Persistencia {
    public boolean validarUsuario(String cedula, String contrasena) {
        String sql = "SELECT * FROM usuario WHERE cedula = '" + cedula + "' AND contrasena = '" + contrasena + "'";
    
        try (Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
        
            return rs.next(); // Retorna true si encuentra una coincidencia
        
        } catch (SQLException e) {
             System.err.println("Error al validar usuario: " + e.getMessage());
             return false;
    }
        
}
    //*************************************
    //                Identificar que ROL tiene
    //*******************************************
    
    public String obtenerRol(String cedula) {
    
        if (existeEnTabla("admin", cedula)) {
            return "admin";
        } 
    
        else if (existeEnTabla("docente", cedula)) {
            return "docente";
        } 
    
        else if (existeEnTabla("estudiante", cedula)) {
            return "estudiante";
    }
    
    return ""; // Si no la encuentra en ninguna
    }
    
    private boolean existeEnTabla(String nombreTabla, String cedula) {
    String sql = "SELECT 1 FROM " + nombreTabla + " WHERE TRIM(cedula) = ?";
    
    try (Connection con = Conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, cedula.trim());

        try (ResultSet rs = pst.executeQuery()) {
            return rs.next(); // Retorna true si encontró la cédula en esa tabla
        }
    } catch (Exception e) {
        System.err.println("Error consultando tabla " + nombreTabla + ": " + e.getMessage());
        return false;
    }
    }
    
    //*******************************************
    //               Traer listado de estudiantes
    //*******************************************
    
    public Estudiantes obtenerEstudiantes(){
        Estudiantes coleccion = new Estudiantes();
        
        String sql = "SELECT usuario.cedula, usuario.nombre"
                + " FROM usuario " +
                "INNER JOIN estudiante ON usuario.cedula = estudiante.cedula";
        
        try(Connection con = Conexion.getConexion();
                PreparedStatement post = con.prepareStatement(sql);
                
                ResultSet result = post.executeQuery()){
            
                        int contador = 0;
                        while (result.next()){
                           String ci = result.getString("cedula");
                           String nombre = result.getString("nombre");
                                                      
                        Estudiante est = new Estudiante(ci, nombre);
                        
                        coleccion.agregarEstudiante(est);
                        contador ++;
                        }
                System.out.println("Se cargaron: " + contador + " alumnos");
         }
        catch (Exception e){
                System.err.println("Error al listar estudiantes" + e.getMessage());
                e.printStackTrace();//muestra erron en NetBeans - lo puse porque no me funciona y no encuentro error
                }
        return coleccion;
        }
     
    
    
    public List<Usuario> obtenerTodosLosUsuarios() {
    
        java.util.List<Usuario> listaUsuarios = new java.util.ArrayList<>();
    
    // Traigo desde la BD el usuario y su rol
    String sql = "SELECT u.cedula, u.nombre, "
               + "  CASE "
               + "    WHEN d.cedula IS NOT NULL THEN 'Docente' "
               + "    WHEN e.cedula IS NOT NULL THEN 'Estudiante' "
               + "    WHEN a.cedula IS NOT NULL THEN 'Admin' "
               + "  END AS rol "
               + "FROM usuario u "
               + "LEFT JOIN docente d ON u.cedula = d.cedula "
               + "LEFT JOIN estudiante e ON u.cedula = e.cedula "
               + "LEFT JOIN admin a ON u.cedula = a.cedula";

    try (java.sql.Connection con = Conexion.getConexion();
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            // Por cada fila devuelta por la BD, creamos un objeto Usuario
            Usuario u = new Usuario( rs.getString("cedula"), rs.getString("nombre"), rs.getString("rol"));
            
            // Lo agregamos a la lista
            listaUsuarios.add(u);
        }

    } catch (java.sql.SQLException e) {
        System.err.println("Error al listar usuarios: " + e.getMessage());
    }
    
    return listaUsuarios; // Retorna la lista llena de objetos
}
    
    public List<Nota> buscarCalificaciones(String cedula) {
    java.util.List<Nota> listaNotas = new java.util.ArrayList<>();
    
    String sql = "SELECT idNota, calificacion, fecha, tipo, cedulaEstudiante "
               + "FROM nota "
               + "WHERE nota.cedulaEstudiante = '" + cedula + "'";

    try (java.sql.Connection con = Conexion.getConexion();
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            Nota nota = new Nota(
                    rs.getInt("idNota"),
                rs.getInt("calificacion"),
                rs.getDate("fecha").toLocalDate(), // Convierte java.sql.Date a LocalDate
                rs.getString("tipo"),
                Integer.parseInt(rs.getString("cedulaEstudiante"))//convierto ci a int
            );
            listaNotas.add(nota);
        }

    } catch (java.sql.SQLException e) {
        System.err.println("Error al buscar calificaciones: " + e.getMessage());
    }

    return listaNotas;
}
    //********************************
    //                   MODIFICAR NOTA
    //**********************************
    
    public void modificarNota(int idNota, int nuevaNota) {
    String sql = "UPDATE nota SET calificacion = " + nuevaNota + " WHERE idNota = " + idNota;

    try (java.sql.Connection con = Conexion.getConexion();
         java.sql.Statement st = con.createStatement()) {

        st.executeUpdate(sql);

    } catch (java.sql.SQLException e) {
        System.err.println("Error al modificar nota: " + e.getMessage());
    }
    }
    //************************************
    //                      ELIMINAR NOTA
    //************************************
    
    public void eliminarNota(int idNota) {
    String sql = "DELETE FROM nota WHERE nota.idNota = " + idNota;

    try (java.sql.Connection con = Conexion.getConexion();
         java.sql.Statement st = con.createStatement()) {

        st.executeUpdate(sql);

    } catch (java.sql.SQLException e) {
        System.err.println("Error al eliminar nota: " + e.getMessage());
    }
}
    
    //*******************************************
    //                   Buscar notas con asignaturas para hacer promedios
    //***********************************************
  /* public List<Object[]> obtenerCalificacionesConCreditos() {
    List<Object[]> lista = new ArrayList<>();
    
    String sql = "SELECT cedulaEstudiante, a.idAsignatura, calificacion,  a.cantCreditos,  "
               + "FROM nota "
               + "INNER JOIN asignatura a ON nota.idAsignatura = a.idAsignatura";

    try (Connection con = Conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            int ci = rs.getInt("cedulaEstudiante");
            String idAsignatura = rs.getString("idAsignatura");
            int calificacion = rs.getInt("calificacion");
            int creditos = rs.getInt("cantCreditos");
            
            // Guardamos el arreglo con los datos necesarios para el cálculo
            lista.add(new Object[]{ci, idAsignatura, calificacion,  creditos, });
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener notas con créditos: " + e.getMessage());
        e.printStackTrace();
    }
    
    return lista;
}
   */
   
    }

    //public ListadoProductos devolverProducto() throws Exception {
    //Conexion DB = Conexion.getInstancia();
    //Connection conn = DB.getConnection();

    //String sql = "SELECT nombre, precio FROM productos";
    //Statement st = conn.createStatement();
    //ResultSet rs = st.executeQuery(sql);
    //ListadoProductos Listado = ListadoProductos.getInstancia();
    
    //while (rs.next()){
    //    Producto p = new Producto();

    //    p.setNombre(rs.getString("nombre"));
    //    p.setPrecio(rs.getDouble("precio"));

    //    Listado.agregarProducto(p);
    //}
    
   // conn.close();

    //return Listado;

   // public void IngresarDatosProducto(String nombre, double precio) throws Exception {
   //     Conexion DB = Conexion.getInstancia();
   //     Connection conn = DB.getConnection();
    //    String insertar = "INSERT INTO productos(nombre, precio) VALUES('" + nombre + "', " + precio + ")";
    //    //System.out.println(insertar);
    //    PreparedStatement st = conn.prepareStatement(insertar);
    //    st.executeUpdate();
   //     conn.close();
   // }
    
//}
