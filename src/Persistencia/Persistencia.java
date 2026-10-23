package Persistencia;

import Logica.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Persistencia {

    private IllegalStateException fallo(String accion, SQLException ex) {
        return new IllegalStateException("Error al " + accion + ": " + ex.getMessage(), ex);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT cedula, nombre, contrasena FROM usuario";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Usuario(rs.getString("cedula"),
                    rs.getString("nombre"), rs.getString("contrasena")));
            return lista;
        } catch (SQLException ex) { throw fallo("cargar usuarios", ex); }
    }

    public Estudiantes obtenerEstudiantes() {
        Estudiantes lista = new Estudiantes();
        String sql = "SELECT u.cedula,u.nombre,u.contrasena,e.comentario_AC "
                + "FROM estudiante e JOIN usuario u ON u.cedula=e.cedula";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.agregarEstudiante(new Estudiante(
                    rs.getString("cedula"), rs.getString("nombre"),
                    rs.getString("contrasena"), rs.getString("comentario_AC")));
            return lista;
        } catch (SQLException ex) { throw fallo("cargar estudiantes", ex); }
    }

    public Docentes obtenerDocentes() {
        Docentes lista = new Docentes();
        String sql = "SELECT u.cedula,u.nombre,u.contrasena "
                + "FROM docente d JOIN usuario u ON u.cedula=d.cedula";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.agregarDocente(new Docente(
                    rs.getString("cedula"), rs.getString("nombre"),
                    rs.getString("contrasena"), null));
            return lista;
        } catch (SQLException ex) { throw fallo("cargar docentes", ex); }
    }

    public Admins obtenerAdmins() {
        Admins lista = new Admins();
        String sql = "SELECT u.cedula,u.nombre,u.contrasena,a.cargo "
                + "FROM admin a JOIN usuario u ON u.cedula=a.cedula";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.agregarAdmin(new Admin(
                    rs.getString("cedula"), rs.getString("nombre"),
                    rs.getString("contrasena"), rs.getString("cargo")));
            return lista;
        } catch (SQLException ex) { throw fallo("cargar administradores", ex); }
    }

    public Asignaturas obtenerAsignaturas() {
        Asignaturas lista = new Asignaturas();
        String sql = "SELECT idAsignatura,nombre,CantCreditos,CupoMax FROM asignatura";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.agregarAsignatura(new Asignatura(
                    rs.getInt("idAsignatura"), rs.getString("nombre"),
                    rs.getInt("CantCreditos"), rs.getInt("CupoMax"), '\0'));
            return lista;
        } catch (SQLException ex) { throw fallo("cargar asignaturas", ex); }
    }

    public List<Nota> buscarCalificaciones(String cedula) {
        List<Nota> lista = new ArrayList<>();
        boolean todas = cedula == null || cedula.isBlank();
        String sql = "SELECT idNota,cedulaEstudiante,idAsignatura,tipo,calificacion,fecha FROM nota"
                + (todas ? " ORDER BY idNota" : " WHERE cedulaEstudiante=? ORDER BY idNota");
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (!todas) ps.setString(1, cedula.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Nota(
                        rs.getInt("idNota"), rs.getInt("calificacion"),
                        rs.getDate("fecha").toLocalDate(), rs.getString("tipo"),
                        rs.getString("cedulaEstudiante"), rs.getInt("idAsignatura")));
            }
            return lista;
        } catch (SQLException ex) { throw fallo("buscar calificaciones", ex); }
    }

    public Notas obtenerNotas() { return new Notas(buscarCalificaciones("")); }

    public Inscripciones obtenerInscripciones() {
        Inscripciones lista = new Inscripciones();
        String sql = "SELECT cedulaEstudiante,idAsignatura,estado,nota FROM cursa";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.agregarInscripcion(new Inscripcion(
                    rs.getString("cedulaEstudiante"), rs.getInt("idAsignatura"),
                    rs.getString("estado"), rs.getBigDecimal("nota")));
            return lista;
        } catch (SQLException ex) { throw fallo("cargar inscripciones", ex); }
    }

    // Tabla dicta: cada relación se representa como Asignatura_de_curso.
    public Asignaturas_de_cursos obtenerCursos(Docentes docentes, Asignaturas asignaturas,
                                                Inscripciones inscripciones, Notas notas,
                                                Estudiantes estudiantes) {
        Asignaturas_de_cursos lista = new Asignaturas_de_cursos();
        String sql = "SELECT cedulaDocente,idAsignatura FROM dicta";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String ci = rs.getString("cedulaDocente");
                int id = rs.getInt("idAsignatura");
                Docente docente = null;
                Asignatura asignatura = null;
                for (Docente d : docentes.getListaDocentes()) if (d.getCi().equals(ci)) docente = d;
                for (Asignatura a : asignaturas.getListaAsignatura()) if (a.getId()==id) asignatura = a;
                if (docente == null || asignatura == null)
                    throw new IllegalStateException("Relación dicta inconsistente: " + ci + " / " + id);
                List<Estudiante> alumnos = new ArrayList<>();
                for (Inscripcion ins : inscripciones.getListaInscripciones()) {
                    if (ins.getIdAsignatura() == id) {
                        for (Estudiante e : estudiantes.getListaEstudiantes())
                            if (e.getCi().equals(ins.getCedulaEstudiante())) alumnos.add(e);
                    }
                }
                List<Nota> evaluaciones = new ArrayList<>();
                for (Nota n : notas.getListaNota()) if (n.getIdAsignatura() == id)
                    evaluaciones.add(n);
                lista.agregarAsignatura_de_curso(new Asignatura_de_curso(
                        asignatura, docente, alumnos, new Notas(evaluaciones), null));
            }
            return lista;
        } catch (SQLException ex) { throw fallo("cargar asignaturas dictadas", ex); }
    }

    // Tablas grupo y pertenece: conserva objetos Grupo y sus asignaturas.
    public Grupos obtenerGrupos(Asignaturas asignaturas, Asignaturas_de_cursos cursos) {
        Grupos grupos = new Grupos();
        String sql = "SELECT idGrupo,nombre FROM grupo";
        String relacion = "SELECT idGrupo,idAsignatura FROM pertenece";
        try (Connection c = Conexion.getConexion()) {
            try (PreparedStatement ps = c.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) grupos.agregarGrupo(
                        new Grupo(rs.getInt("idGrupo"), rs.getString("nombre")));
            }
            try (PreparedStatement ps = c.prepareStatement(relacion);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idGrupo = rs.getInt("idGrupo");
                    int idAsignatura = rs.getInt("idAsignatura");
                    Grupo grupo = null;
                    Asignatura asignatura = null;
                    for (Grupo g : grupos.getListaGrupos()) if (g.getId()==idGrupo) grupo=g;
                    for (Asignatura a : asignaturas.getListaAsignatura())
                        if (a.getId()==idAsignatura) asignatura=a;
                    if (grupo==null || asignatura==null)
                        throw new IllegalStateException("Relación pertenece inconsistente");
                    grupo.agregarAsignatura(asignatura);
                    for (Asignatura_de_curso curso : cursos.getListaAsignaturas_de_curso())
                        if (curso.getAsignatura().getId()==idAsignatura)
                            grupo.getAsisg().agregarAsignatura_de_curso(curso);
                }
            }
            return grupos;
        } catch (SQLException ex) { throw fallo("cargar grupos y pertenencias", ex); }
    }

    public Informes obtenerInformes() {
        Informes lista = new Informes();
        String sql = "SELECT idInforme,cedulaEstudiante,cedulaDocente,idAsignatura,contenido,fecha FROM informe";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.agregarInformeAcademico(new InformeAcademico(
                    rs.getInt("idInforme"), rs.getString("cedulaEstudiante"),
                    rs.getString("cedulaDocente"), rs.getInt("idAsignatura"),
                    rs.getString("contenido"), rs.getTimestamp("fecha").toLocalDateTime()));
            return lista;
        } catch (SQLException ex) { throw fallo("cargar informes", ex); }
    }

    // ========================================
    // GUARDAR INFORME ACADEMICO
    // ========================================

    public InformeAcademico ingresarInforme(
            String cedulaEstudiante, String cedulaDocente,
            int idAsignatura, String contenido) {

        String insertar = "INSERT INTO informe "
                + "(cedulaEstudiante, cedulaDocente, idAsignatura, contenido) "
                + "VALUES (?, ?, ?, ?)";
        String consultar = "SELECT idInforme, cedulaEstudiante, cedulaDocente, "
                + "idAsignatura, contenido, fecha FROM informe WHERE idInforme = ?";

        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);

            try {
                int idGenerado;

                try (PreparedStatement ps = con.prepareStatement(
                        insertar, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, cedulaEstudiante);
                    ps.setString(2, cedulaDocente);
                    ps.setInt(3, idAsignatura);
                    ps.setString(4, contenido);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se guardó el informe académico");
                    }

                    try (ResultSet claves = ps.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("MariaDB no devolvió el ID del informe");
                        }
                        idGenerado = claves.getInt(1);
                    }
                }

                InformeAcademico informeGuardado;
                try (PreparedStatement ps = con.prepareStatement(consultar)) {
                    ps.setInt(1, idGenerado);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("No se pudo recuperar el informe insertado");
                        }
                        informeGuardado = new InformeAcademico(
                                rs.getInt("idInforme"),
                                rs.getString("cedulaEstudiante"),
                                rs.getString("cedulaDocente"),
                                rs.getInt("idAsignatura"),
                                rs.getString("contenido"),
                                rs.getTimestamp("fecha").toLocalDateTime()
                        );
                    }
                }

                con.commit();
                return informeGuardado;

            } catch (SQLException | RuntimeException ex) {
                try {
                    con.rollback();
                } catch (SQLException errorRollback) {
                    ex.addSuppressed(errorRollback);
                }
                throw ex;
            }

        } catch (SQLException ex) {
            throw fallo("guardar el informe académico", ex);
        }
    }

    // Estos métodos siguen disponibles para el código antiguo.
    public boolean validarUsuario(String cedula, String contrasena) {
        String sql = "SELECT 1 FROM usuario WHERE cedula=? AND contrasena=?";
        try (Connection c=Conexion.getConexion(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,cedula); ps.setString(2,contrasena);
            try(ResultSet rs=ps.executeQuery()){return rs.next();}
        } catch(SQLException ex){throw fallo("validar usuario",ex);}
    }
    public String obtenerRol(String cedula) {
        String sql="SELECT CASE WHEN a.cedula IS NOT NULL THEN 'admin' "
                + "WHEN d.cedula IS NOT NULL THEN 'docente' "
                + "WHEN e.cedula IS NOT NULL THEN 'estudiante' ELSE '' END AS rol "
                + "FROM usuario u LEFT JOIN admin a ON a.cedula=u.cedula "
                + "LEFT JOIN docente d ON d.cedula=u.cedula "
                + "LEFT JOIN estudiante e ON e.cedula=u.cedula WHERE u.cedula=?";
        try(Connection c=Conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,cedula);
            try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getString("rol"):"";}
        } catch(SQLException ex){throw fallo("obtener rol",ex);}
    }

    public int ingresarDatosNota(String ci, int idAsignatura, String tipo, int nota) {
        String sql="INSERT INTO nota (cedulaEstudiante,idAsignatura,tipo,calificacion,fecha) "
                + "VALUES (?,?,?,?,CURRENT_DATE())";
        try(Connection c=Conexion.getConexion()) {
            c.setAutoCommit(false);
            try(PreparedStatement ps=c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,ci); ps.setInt(2,idAsignatura);
                ps.setString(3,tipo); ps.setInt(4,nota);
                if(ps.executeUpdate()!=1) throw new SQLException("No se insertó la nota");
                int id;
                try(ResultSet rs=ps.getGeneratedKeys()) {
                    if(!rs.next()) throw new SQLException("No se recuperó el idNota generado");
                    id=rs.getInt(1);
                }
                c.commit(); return id;
            } catch(SQLException | RuntimeException ex) {
                try{c.rollback();}catch(SQLException rb){ex.addSuppressed(rb);}
                throw ex;
            }
        } catch(SQLException ex){throw fallo("ingresar nota",ex);}
    }
    public void modificarNota(int idNota, int nuevaNota) {
        String sql="UPDATE nota SET calificacion=? WHERE idNota=?";
        try(Connection c=Conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setInt(1,nuevaNota);ps.setInt(2,idNota);
            if(ps.executeUpdate()!=1) throw new IllegalStateException("La nota no existe o no cambió");
        } catch(SQLException ex){throw fallo("modificar nota",ex);}
    }
    public void eliminarNota(int idNota) {
        String sql="DELETE FROM nota WHERE idNota=?";
        try(Connection c=Conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setInt(1,idNota);
            if(ps.executeUpdate()!=1) throw new IllegalStateException("No se encontró la nota");
        } catch(SQLException ex){throw fallo("eliminar nota",ex);}
    }
    // ==========================================
    // ADMINISTRACION DE ESTUDIANTES (usuario + estudiante)
    // ==========================================

    /** Inserta ambas tablas en una sola transaccion. */
    public void agregarEstudiante(Estudiante estudiante) {
        String insertarUsuario = "INSERT INTO usuario (cedula,nombre,contrasena) VALUES (?,?,?)";
        String insertarEstudiante = "INSERT INTO estudiante (cedula,comentario_AC) VALUES (?,?)";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(insertarUsuario)) {
                    ps.setString(1, estudiante.getCi());
                    ps.setString(2, estudiante.getNombre());
                    ps.setString(3, estudiante.getContrasena());
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se insertó el usuario");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(insertarEstudiante)) {
                    ps.setString(1, estudiante.getCi());
                    ps.setString(2, estudiante.getComentario());
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se insertó el estudiante");
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException otro) { ex.addSuppressed(otro); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("agregar estudiante", ex);
        }
    }

    /** Conserva la cedula: es PK y tambien FK en otras tablas. */
    public void modificarEstudiante(String cedula, String nombre,
                                   String contrasena, String comentario) {
        String verificar = "SELECT u.cedula FROM usuario u INNER JOIN estudiante e "
                + "ON e.cedula=u.cedula WHERE u.cedula=? FOR UPDATE";
        String actualizarUsuario = "UPDATE usuario SET nombre=?, contrasena=? WHERE cedula=?";
        String actualizarEstudiante = "UPDATE estudiante SET comentario_AC=? WHERE cedula=?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(verificar)) {
                    ps.setString(1, cedula);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalStateException("No existe el estudiante con cédula " + cedula);
                        }
                    }
                }
                // Un UPDATE puede reportar 0 si los valores no cambiaron.
                try (PreparedStatement ps = con.prepareStatement(actualizarUsuario)) {
                    ps.setString(1, nombre);
                    ps.setString(2, contrasena);
                    ps.setString(3, cedula);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(actualizarEstudiante)) {
                    ps.setString(1, comentario);
                    ps.setString(2, cedula);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException otro) { ex.addSuppressed(otro); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("modificar estudiante", ex);
        }
    }

    /**
     * La eliminacion se bloquea si existen antecedentes academicos.
     * Si no hay referencias, borrar usuario elimina estudiante por ON DELETE CASCADE.
     */
    public void eliminarEstudiante(String cedula) {
        String verificar = "SELECT u.cedula FROM usuario u INNER JOIN estudiante e "
                + "ON e.cedula=u.cedula WHERE u.cedula=? FOR UPDATE";
        String eliminar = "DELETE FROM usuario WHERE cedula=?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(verificar)) {
                    ps.setString(1, cedula);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalStateException("El estudiante no existe");
                    }
                }
                if (existeReferencia(con, "SELECT 1 FROM cursa WHERE cedulaEstudiante=? LIMIT 1", cedula)
                        || existeReferencia(con, "SELECT 1 FROM nota WHERE cedulaEstudiante=? LIMIT 1", cedula)
                        || existeReferencia(con, "SELECT 1 FROM informe WHERE cedulaEstudiante=? LIMIT 1", cedula)) {
                    throw new IllegalStateException("No se puede eliminar: tiene inscripciones, notas o informes");
                }
                // No borrar de usuario si la cedula tambien tiene otro rol.
                if (existeReferencia(con, "SELECT 1 FROM docente WHERE cedula=? LIMIT 1", cedula)
                        || existeReferencia(con, "SELECT 1 FROM admin WHERE cedula=? LIMIT 1", cedula)) {
                    throw new IllegalStateException("No se puede eliminar: la cédula tiene otro rol");
                }
                try (PreparedStatement ps = con.prepareStatement(eliminar)) {
                    ps.setString(1, cedula);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se eliminó el usuario del estudiante");
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException otro) { ex.addSuppressed(otro); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("eliminar estudiante", ex);
        }
    }

    private boolean existeReferencia(Connection con, String sql, String cedula)
            throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }


    // =============================================================
    // INSCRIBIR ESTUDIANTE EN ASIGNATURA (tabla cursa)
    // =============================================================

    /**
     * El bloqueo de la asignatura serializa las inscripciones concurrentes
     * para validar el CupoMax con los datos actuales de MariaDB.
     * Usa la PK (cedulaEstudiante, idAsignatura) para impedir duplicados.
     */
    public void inscribirEstudiante(String cedulaEstudiante, int idAsignatura) {
        String bloquearAsignatura =
                "SELECT CupoMax FROM asignatura WHERE idAsignatura=? FOR UPDATE";
        String comprobarEstudiante =
                "SELECT 1 FROM estudiante WHERE cedula=?";
        String comprobarInscripcion =
                "SELECT 1 FROM cursa WHERE cedulaEstudiante=? AND idAsignatura=?";
        String contarInscripciones =
                "SELECT COUNT(*) FROM cursa WHERE idAsignatura=?";
        String insertar =
                "INSERT INTO cursa (cedulaEstudiante,idAsignatura,estado,nota) "
                + "VALUES (?,?,'Cursando',NULL)";

        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                int cupo;
                try (PreparedStatement ps = con.prepareStatement(bloquearAsignatura)) {
                    ps.setInt(1, idAsignatura);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException("La asignatura no existe");
                        }
                        cupo = rs.getInt("CupoMax");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(comprobarEstudiante)) {
                    ps.setString(1, cedulaEstudiante);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException("El estudiante no existe");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(comprobarInscripcion)) {
                    ps.setString(1, cedulaEstudiante);
                    ps.setInt(2, idAsignatura);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new IllegalArgumentException(
                                    "El estudiante ya está inscripto en esta asignatura");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(contarInscripciones)) {
                    ps.setInt(1, idAsignatura);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("No se pudo comprobar el cupo");
                        if (rs.getInt(1) >= cupo) {
                            throw new IllegalStateException(
                                    "No quedan cupos disponibles para la asignatura");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(insertar)) {
                    ps.setString(1, cedulaEstudiante);
                    ps.setInt(2, idAsignatura);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se registró la inscripción");
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("inscribir estudiante en asignatura", ex);
        }
    }

    // =============================================================
    // QUITAR INSCRIPCION (tabla cursa)
    // =============================================================

    /**
     * No borra información académica. Como nota e informe no referencian
     * directamente cursa con FK, comprobamos ambas tablas explícitamente.
     * Toda la operación se ejecuta en una sola transacción.
     */
    public void desinscribirEstudiante(String cedulaEstudiante, int idAsignatura) {
        String bloqueo = "SELECT cedulaEstudiante FROM cursa "
                + "WHERE cedulaEstudiante=? AND idAsignatura=? FOR UPDATE";
        String notas = "SELECT 1 FROM nota WHERE cedulaEstudiante=? "
                + "AND idAsignatura=? LIMIT 1";
        String informes = "SELECT 1 FROM informe WHERE cedulaEstudiante=? "
                + "AND idAsignatura=? LIMIT 1";
        String eliminar = "DELETE FROM cursa "
                + "WHERE cedulaEstudiante=? AND idAsignatura=?";

        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(bloqueo)) {
                    ps.setString(1, cedulaEstudiante);
                    ps.setInt(2, idAsignatura);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException(
                                    "La inscripción no existe en MariaDB");
                        }
                    }
                }
                for (String consulta : new String[]{notas, informes}) {
                    try (PreparedStatement ps = con.prepareStatement(consulta)) {
                        ps.setString(1, cedulaEstudiante);
                        ps.setInt(2, idAsignatura);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                throw new IllegalStateException(
                                        "No se puede quitar la inscripción: "
                                        + "existen notas o informes académicos asociados");
                            }
                        }
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(eliminar)) {
                    ps.setString(1, cedulaEstudiante);
                    ps.setInt(2, idAsignatura);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo quitar la inscripción");
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("quitar inscripción", ex);
        }
    }

    // =============================================================
    // ADMINISTRACION DE DOCENTES (usuario + docente)
    // =============================================================

    /** Inserta la fila en usuario y docente en una única transacción. */
    public void agregarDocente(Docente docente) {
        String insertarUsuario = "INSERT INTO usuario (cedula,nombre,contrasena) VALUES (?,?,?)";
        String insertarDocente = "INSERT INTO docente (cedula) VALUES (?)";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(insertarUsuario)) {
                    ps.setString(1, docente.getCi());
                    ps.setString(2, docente.getNombre());
                    ps.setString(3, docente.getContrasena());
                    if (ps.executeUpdate() != 1) throw new SQLException("No se insertó el usuario");
                }
                try (PreparedStatement ps = con.prepareStatement(insertarDocente)) {
                    ps.setString(1, docente.getCi());
                    if (ps.executeUpdate() != 1) throw new SQLException("No se insertó el docente");
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("agregar docente", ex);
        }
    }

    /** Mantiene la cédula, que es clave primaria y clave foránea. */
    public void modificarDocente(String cedula, String nombre, String contrasena) {
        String verificar = "SELECT u.cedula FROM usuario u INNER JOIN docente d "
                + "ON d.cedula=u.cedula WHERE u.cedula=? FOR UPDATE";
        String actualizar = "UPDATE usuario SET nombre=?, contrasena=? WHERE cedula=?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(verificar)) {
                    ps.setString(1, cedula);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalStateException("No existe el docente con cédula " + cedula);
                    }
                }
                // Si los valores no cambian, UPDATE puede devolver cero filas afectadas.
                try (PreparedStatement ps = con.prepareStatement(actualizar)) {
                    ps.setString(1, nombre);
                    ps.setString(2, contrasena);
                    ps.setString(3, cedula);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("modificar docente", ex);
        }
    }

    /** Evita las eliminaciones en cascada de asignaciones e informes. */
    public void eliminarDocente(String cedula) {
        String verificar = "SELECT u.cedula FROM usuario u INNER JOIN docente d "
                + "ON d.cedula=u.cedula WHERE u.cedula=? FOR UPDATE";
        String eliminarUsuario = "DELETE FROM usuario WHERE cedula=?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(verificar)) {
                    ps.setString(1, cedula);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalStateException("El docente no existe");
                    }
                }
                if (existeReferencia(con, "SELECT 1 FROM dicta WHERE cedulaDocente=? LIMIT 1", cedula)
                        || existeReferencia(con, "SELECT 1 FROM informe WHERE cedulaDocente=? LIMIT 1", cedula)) {
                    throw new IllegalStateException("No se puede eliminar: el docente dicta asignaturas o tiene informes");
                }
                // Si una cédula también posee otro rol, no eliminar ese Usuario compartido.
                if (existeReferencia(con, "SELECT 1 FROM estudiante WHERE cedula=? LIMIT 1", cedula)
                        || existeReferencia(con, "SELECT 1 FROM admin WHERE cedula=? LIMIT 1", cedula)) {
                    throw new IllegalStateException("No se puede eliminar: la cédula tiene otro rol");
                }
                try (PreparedStatement ps = con.prepareStatement(eliminarUsuario)) {
                    ps.setString(1, cedula);
                    if (ps.executeUpdate() != 1) throw new SQLException("No se eliminó el usuario del docente");
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("eliminar docente", ex);
        }
    }


    // =============================================================
    // ADMINISTRACION DE ASIGNATURAS
    // =============================================================

    public int agregarAsignatura(String nombre, int creditos, int cupoMaximo) {
        String sql = "INSERT INTO asignatura (nombre,CantCreditos,CupoMax) VALUES (?,?,?)";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement ps = con.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, nombre);
                    ps.setInt(2, creditos);
                    ps.setInt(3, cupoMaximo);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se insertó la asignatura");
                    }
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("No se recuperó el ID de la asignatura");
                        }
                        id = rs.getInt(1);
                    }
                }
                con.commit();
                return id;
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("agregar asignatura", ex);
        }
    }

    public void modificarAsignatura(int id, String nombre, int creditos, int cupoMaximo) {
        String bloqueo = "SELECT idAsignatura FROM asignatura WHERE idAsignatura=? FOR UPDATE";
        String contar = "SELECT COUNT(*) FROM cursa WHERE idAsignatura=?";
        String sql = "UPDATE asignatura SET nombre=?,CantCreditos=?,CupoMax=? WHERE idAsignatura=?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(bloqueo)) {
                    ps.setInt(1,id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("La asignatura ya no existe");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(contar)) {
                    ps.setInt(1,id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("No se pudieron contar inscripciones");
                        if (cupoMaximo < rs.getInt(1)) {
                            throw new IllegalStateException(
                                    "El cupo no puede ser menor a la cantidad de inscriptos");
                        }
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1,nombre);
                    ps.setInt(2,creditos);
                    ps.setInt(3,cupoMaximo);
                    ps.setInt(4,id);
                    ps.executeUpdate(); // Puede devolver 0 si no hubo cambios.
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("modificar asignatura", ex);
        }
    }

    public void eliminarAsignatura(int id) {
        String bloqueo = "SELECT idAsignatura FROM asignatura WHERE idAsignatura=? FOR UPDATE";
        String[] referencias = {
            "SELECT 1 FROM cursa WHERE idAsignatura=? LIMIT 1",
            "SELECT 1 FROM dicta WHERE idAsignatura=? LIMIT 1",
            "SELECT 1 FROM pertenece WHERE idAsignatura=? LIMIT 1",
            "SELECT 1 FROM nota WHERE idAsignatura=? LIMIT 1",
            "SELECT 1 FROM informe WHERE idAsignatura=? LIMIT 1"
        };
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(bloqueo)) {
                    ps.setInt(1,id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("La asignatura ya no existe");
                    }
                }
                for (String consulta : referencias) {
                    try (PreparedStatement ps = con.prepareStatement(consulta)) {
                        ps.setInt(1,id);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                throw new IllegalStateException(
                                        "No se puede eliminar: la asignatura tiene inscripciones, docentes, grupos, notas o informes asociados");
                            }
                        }
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM asignatura WHERE idAsignatura=?")) {
                    ps.setInt(1,id);
                    if (ps.executeUpdate() != 1) throw new SQLException("No se eliminó la asignatura");
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("eliminar asignatura", ex);
        }
    }

    // =============================================================
    // ADMINISTRACION DE RELACIONES DOCENTE - ASIGNATURA (dicta)
    // =============================================================

    public void asignarDocenteAsignatura(String cedulaDocente, int idAsignatura) {
        String sql = "INSERT INTO dicta (cedulaDocente,idAsignatura) VALUES (?,?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,cedulaDocente);
            ps.setInt(2,idAsignatura);
            if (ps.executeUpdate() != 1) throw new SQLException("No se registró la asignación docente");
        } catch (SQLException ex) {
            throw fallo("asignar docente a asignatura", ex);
        }
    }

    public void desasignarDocenteAsignatura(String cedulaDocente, int idAsignatura) {
        String sql = "DELETE FROM dicta WHERE cedulaDocente=? AND idAsignatura=?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                // Informes referencia de forma compuesta a (cedulaDocente, idAsignatura).
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT 1 FROM informe WHERE cedulaDocente=? AND idAsignatura=? LIMIT 1")) {
                    ps.setString(1,cedulaDocente);
                    ps.setInt(2,idAsignatura);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) throw new IllegalStateException(
                                "No se puede quitar: el docente tiene informes de esa asignatura");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1,cedulaDocente);
                    ps.setInt(2,idAsignatura);
                    if (ps.executeUpdate() != 1) {
                        throw new IllegalArgumentException("No existe esa asignación docente");
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rb) { ex.addSuppressed(rb); }
                throw ex;
            }
        } catch (SQLException ex) {
            throw fallo("quitar docente de asignatura", ex);
        }
    }

}
