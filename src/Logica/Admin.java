
package Logica;

import Persistencia.Persistencia;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;


public class Admin extends Usuario {
    private String cargo;

    public Admin(String ci, String nombre, String contrasena,String cargo) {
        super(ci, nombre, contrasena);
        this.cargo=cargo;
    }
//xd
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }


    private final Persistencia persistencia = new Persistencia();

    private void exigirAdmin() {
        CargaInicial.exigirCarga();
        String ci = Login.getCedulaActual();
        if (ci == null || !ci.equals(getCi()) || Admins.getInstancia().getListaAdmins().stream()
                .noneMatch(a -> a.getCi().equals(ci))) {
            throw new IllegalStateException("La operación requiere una sesión de administrador");
        }
    }

    public List<Estudiante> obtenerEstudiantes() {
        exigirAdmin();
        List<Estudiante> lista = new ArrayList<>(
                Estudiantes.getInstancia().getListaEstudiantes());
        lista.sort(Comparator.comparing(Estudiante::getNombre,
                String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(lista);
    }

    public Estudiante buscarEstudiante(String cedula) {
        exigirAdmin();
        if (cedula == null) return null;
        for (Estudiante e : Estudiantes.getInstancia().getListaEstudiantes()) {
            if (e.getCi().equals(cedula.trim())) return e;
        }
        return null;
    }

    public synchronized void agregarEstudiante(String cedula, String nombre,
                                                String contrasena, String comentario) {
        exigirAdmin();
        validarDatos(cedula, nombre, contrasena);
        cedula = cedula.trim();
        nombre = nombre.trim();

        for (Usuario u : Usuarios.getInstancia().getListaUsuarios()) {
            if (u.getCi().equals(cedula)) {
                throw new IllegalArgumentException("La cédula ya pertenece a un usuario");
            }
        }

        Estudiante nuevo = new Estudiante(cedula, nombre, contrasena,
                comentario == null ? null : comentario.trim());
        persistencia.agregarEstudiante(nuevo);
        try {
            Usuarios.getInstancia().agregarUsuario(nuevo);
            Estudiantes.getInstancia().agregarEstudiante(nuevo);
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "Se guardó en MariaDB pero falló la actualización de memoria. Reiniciá SGA.", ex);
        }
    }

    /** En edición, una contraseña vacía significa conservar la existente. */
    public synchronized void modificarEstudiante(String cedula, String nombre,
                                                 String nuevaContrasena, String comentario) {
        exigirAdmin();
        if (cedula == null) throw new IllegalArgumentException("Falta la cédula");
        Estudiante actual = buscarEstudiante(cedula);
        if (actual == null) throw new IllegalArgumentException("El estudiante no existe");
        String contrasena = (nuevaContrasena == null || nuevaContrasena.isEmpty())
                ? actual.getContrasena() : nuevaContrasena;
        validarDatos(cedula, nombre, contrasena);
        nombre = nombre.trim();
        String nuevoComentario = comentario == null ? null : comentario.trim();

        persistencia.modificarEstudiante(cedula, nombre, contrasena, nuevoComentario);
        try {
            // Usuarios y Estudiantes comparten la misma instancia de Estudiante.
            actual.setNombre(nombre);
            actual.setContrasena(contrasena);
            actual.setComentario(nuevoComentario);
            // Mantiene coherencia incluso si Usuarios tuviera otra referencia.
            for (Usuario u : Usuarios.getInstancia().getListaUsuarios()) {
                if (u.getCi().equals(cedula) && u != actual) {
                    u.setNombre(nombre);
                    u.setContrasena(contrasena);
                }
            }
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "Se modificó MariaDB pero falló la actualización de memoria. Reiniciá SGA.", ex);
        }
    }

    public synchronized void eliminarEstudiante(String cedula) {
        exigirAdmin();
        Estudiante actual = buscarEstudiante(cedula);
        if (actual == null) throw new IllegalArgumentException("El estudiante no existe");
        // No permitir que desaparezca información académica.
        boolean tieneInscripciones = Inscripciones.getInstancia().getListaInscripciones()
                .stream().anyMatch(i -> i.getCedulaEstudiante().equals(cedula));
        boolean tieneNotas = Notas.getInstancia().getListaNota()
                .stream().anyMatch(n -> n.getCi().equals(cedula));
        boolean tieneInformes = Informes.getInstancia().getListaInformes()
                .stream().anyMatch(i -> i.getCedulaEstudiante().equals(cedula));
        if (tieneInscripciones || tieneNotas || tieneInformes) {
            throw new IllegalStateException("No se puede eliminar: tiene inscripciones, notas o informes. "
                    + "Primero hay que resolver esos registros académicos.");
        }

        persistencia.eliminarEstudiante(cedula);
        try {
            Estudiantes.getInstancia().eliminarEstudiante(actual);
            for (Usuario u : Usuarios.getInstancia().getListaUsuarios()) {
                if (u.getCi().equals(cedula)) {
                    Usuarios.getInstancia().eliminarUsuario(u);
                    break;
                }
            }
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "Se eliminó en MariaDB pero falló la actualización de memoria. Reiniciá SGA.", ex);
        }
    }

    private void validarDatos(String ci, String nombre, String contrasena) {
        if (ci == null || ci.isBlank() || ci.trim().length() > 20) {
            throw new IllegalArgumentException("La cédula es obligatoria (hasta 20 caracteres)");
        }
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre es obligatorio (hasta 100 caracteres)");
        }
        if (contrasena == null || contrasena.isEmpty() || contrasena.length() > 255) {
            throw new IllegalArgumentException("La contraseña es obligatoria (hasta 255 caracteres)");
        }
    }


    // =============================================================
    // INSCRIPCIONES DE ESTUDIANTES
    // =============================================================

    /** Asignaturas que todavía no cursa el estudiante seleccionado. */
    public List<Asignatura> obtenerAsignaturasDisponibles(String cedula) {
        exigirAdmin();
        if (buscarEstudiante(cedula) == null) {
            throw new IllegalArgumentException("Primero seleccioná un estudiante existente");
        }
        List<Asignatura> disponibles = new ArrayList<>();
        for (Asignatura a : Asignaturas.getInstancia().getListaAsignatura()) {
            boolean yaInscripto = Inscripciones.getInstancia().getListaInscripciones()
                    .stream().anyMatch(i -> i.getCedulaEstudiante().equals(cedula)
                            && i.getIdAsignatura() == a.getId());
            if (!yaInscripto) disponibles.add(a);
        }
        disponibles.sort(Comparator.comparing(Asignatura::getNombre,
                String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(disponibles);
    }

    /**
     * MariaDB confirma primero; las colecciones se actualizan después.
     * Se modifica el objeto Asignatura_de_curso existente, evitando
     * referencias desactualizadas desde Grupos.
     */
    public synchronized void inscribirEstudiante(String cedula, int idAsignatura) {
        exigirAdmin();
        if (cedula == null) throw new IllegalArgumentException("Falta la cédula");
        cedula = cedula.trim();
        Estudiante estudiante = buscarEstudiante(cedula);
        if (estudiante == null) throw new IllegalArgumentException("Estudiante inexistente");

        boolean existeAsignatura = Asignaturas.getInstancia().getListaAsignatura()
                .stream().anyMatch(a -> a.getId() == idAsignatura);
        if (!existeAsignatura) throw new IllegalArgumentException("Asignatura inexistente");

        for (Inscripcion i : Inscripciones.getInstancia().getListaInscripciones()) {
            if (i.getCedulaEstudiante().equals(cedula)
                    && i.getIdAsignatura() == idAsignatura) {
                throw new IllegalArgumentException(
                        "El estudiante ya está inscripto en esa asignatura");
            }
        }

        // SQL en Persistencia: transacción, duplicados y CupoMax.
        persistencia.inscribirEstudiante(cedula, idAsignatura);

        try {
            Inscripciones.getInstancia().agregarInscripcion(
                    new Inscripcion(cedula, idAsignatura, "Cursando", null));

            for (Asignatura_de_curso curso : Asignaturas_de_cursos.getInstancia()
                    .getListaAsignaturas_de_curso()) {
                if (curso.getAsignatura().getId() == idAsignatura) {
                    curso.agregarEstudiante(estudiante);
                }
            }
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "Inscripción guardada en MariaDB, pero falló la actualización "
                    + "en memoria. Reiniciá SGA.", ex);
        }
    }

    // =============================================================
    // CONSULTAR Y QUITAR INSCRIPCIONES
    // =============================================================

    /** Consulta desde la colección cargada por el Splash; no ejecuta SQL. */
    public List<Inscripcion> obtenerInscripciones() {
        exigirAdmin();
        List<Inscripcion> lista = new ArrayList<>(
                Inscripciones.getInstancia().getListaInscripciones());
        lista.sort(Comparator.comparing(Inscripcion::getCedulaEstudiante)
                .thenComparingInt(Inscripcion::getIdAsignatura));
        return List.copyOf(lista);
    }

    /**
     * La eliminación se permite únicamente cuando no se perderían
     * referencias académicas de esa pareja estudiante/asignatura.
     * Se confirma primero en MariaDB y después se actualizan las listas.
     */
    public synchronized void desinscribirEstudiante(
            String cedula, int idAsignatura) {
        exigirAdmin();
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("Seleccioná un estudiante");
        }
        cedula = cedula.trim();
        Inscripcion existente = null;
        for (Inscripcion inscripcion :
                Inscripciones.getInstancia().getListaInscripciones()) {
            if (inscripcion.getCedulaEstudiante().equals(cedula)
                    && inscripcion.getIdAsignatura() == idAsignatura) {
                existente = inscripcion;
                break;
            }
        }
        if (existente == null) {
            throw new IllegalArgumentException("La inscripción ya no existe en memoria");
        }
        final String ci = cedula;
        if (Notas.getInstancia().getListaNota().stream()
                .anyMatch(n -> ci.equals(n.getCi())
                        && n.getIdAsignatura() != null
                        && n.getIdAsignatura() == idAsignatura)) {
            throw new IllegalStateException(
                    "No se puede quitar: el estudiante tiene calificaciones de esa asignatura");
        }
        if (Informes.getInstancia().getListaInformes().stream()
                .anyMatch(i -> ci.equals(i.getCedulaEstudiante())
                        && i.getIdAsignatura() == idAsignatura)) {
            throw new IllegalStateException(
                    "No se puede quitar: el estudiante tiene informes de esa asignatura");
        }

        persistencia.desinscribirEstudiante(ci, idAsignatura);

        try {
            Inscripciones.getInstancia().eliminarInscripcion(existente);
            for (Asignatura_de_curso curso :
                    Asignaturas_de_cursos.getInstancia()
                            .getListaAsignaturas_de_curso()) {
                if (curso.getAsignatura().getId() == idAsignatura) {
                    curso.quitarEstudiante(ci);
                }
            }
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "La inscripción se quitó en MariaDB pero falló la "
                    + "actualización en memoria. Reiniciá SGA.", ex);
        }
    }

    // =============================================================
    // ADMINISTRAR DOCENTES - SOBRE LAS COLECCIONES YA CARGADAS
    // =============================================================

    public List<Docente> obtenerDocentes() {
        exigirAdmin();
        List<Docente> lista = new ArrayList<>(Docentes.getInstancia().getListaDocentes());
        lista.sort(Comparator.comparing(Docente::getNombre, String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(lista);
    }

    public Docente buscarDocente(String cedula) {
        exigirAdmin();
        if (cedula == null) return null;
        for (Docente docente : Docentes.getInstancia().getListaDocentes()) {
            if (docente.getCi().equals(cedula.trim())) return docente;
        }
        return null;
    }

    /** Lista asignaturas vinculadas por la tabla dicta, cargadas durante el Splash. */
    public List<Asignatura> obtenerAsignaturasDocente(String cedula) {
        exigirAdmin();
        List<Asignatura> resultado = new ArrayList<>();
        if (cedula == null) return List.copyOf(resultado);
        for (Asignatura_de_curso curso : Asignaturas_de_cursos.getInstancia()
                .getListaAsignaturas_de_curso()) {
            if (curso.getDocente() != null && cedula.equals(curso.getDocente().getCi())
                    && curso.getAsignatura() != null) {
                if (resultado.stream().noneMatch(a -> a.getId() == curso.getAsignatura().getId())) {
                    resultado.add(curso.getAsignatura());
                }
            }
        }
        return List.copyOf(resultado);
    }

    public synchronized void agregarDocente(String cedula, String nombre, String contrasena) {
        exigirAdmin();
        validarDatos(cedula, nombre, contrasena);
        cedula = cedula.trim();
        nombre = nombre.trim();

        for (Usuario usuario : Usuarios.getInstancia().getListaUsuarios()) {
            if (usuario.getCi().equals(cedula)) {
                throw new IllegalArgumentException("La cédula ya pertenece a un usuario");
            }
        }
        // La materia se obtiene a través de dicta, no es columna de docente.
        Docente nuevo = new Docente(cedula, nombre, contrasena, null);
        persistencia.agregarDocente(nuevo);
        try {
            Usuarios.getInstancia().agregarUsuario(nuevo);
            Docentes.getInstancia().agregarDocente(nuevo);
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "El docente se guardó en MariaDB, pero falló la actualización de memoria. Reiniciá SGA.", ex);
        }
    }

    /** Contraseña vacía en modificación significa conservar la existente. */
    public synchronized void modificarDocente(String cedula, String nombre, String nuevaContrasena) {
        exigirAdmin();
        Docente actual = buscarDocente(cedula);
        if (actual == null) throw new IllegalArgumentException("El docente no existe");
        String contrasena = (nuevaContrasena == null || nuevaContrasena.isEmpty())
                ? actual.getContrasena() : nuevaContrasena;
        validarDatos(cedula, nombre, contrasena);
        nombre = nombre.trim();

        persistencia.modificarDocente(cedula, nombre, contrasena);
        try {
            actual.setNombre(nombre);
            actual.setContrasena(contrasena);
            for (Usuario usuario : Usuarios.getInstancia().getListaUsuarios()) {
                if (usuario.getCi().equals(cedula) && usuario != actual) {
                    usuario.setNombre(nombre);
                    usuario.setContrasena(contrasena);
                }
            }
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "La modificación se guardó en MariaDB, pero falló la actualización de memoria. Reiniciá SGA.", ex);
        }
    }

    public synchronized void eliminarDocente(String cedula) {
        exigirAdmin();
        Docente actual = buscarDocente(cedula);
        if (actual == null) throw new IllegalArgumentException("El docente no existe");

        // Evitar perder asignaciones y los informes que elaboró.
        if (!obtenerAsignaturasDocente(cedula).isEmpty()
                || Informes.getInstancia().getListaInformes().stream()
                    .anyMatch(i -> cedula.equals(i.getCedulaDocente()))) {
            throw new IllegalStateException(
                    "No se puede eliminar: el docente dicta asignaturas o tiene informes. "
                    + "Primero resolvé esos vínculos.");
        }

        persistencia.eliminarDocente(cedula);
        try {
            Docentes.getInstancia().eliminarDocente(actual);
            for (Usuario usuario : Usuarios.getInstancia().getListaUsuarios()) {
                if (usuario.getCi().equals(cedula)) {
                    Usuarios.getInstancia().eliminarUsuario(usuario);
                    break;
                }
            }
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "El docente se eliminó en MariaDB, pero falló la actualización de memoria. Reiniciá SGA.", ex);
        }
    }


    // =============================================================
    // ASIGNATURAS (desde la colección Asignaturas)
    // =============================================================

    public List<Asignatura> obtenerAsignaturas() {
        exigirAdmin();
        List<Asignatura> lista = new ArrayList<>(Asignaturas.getInstancia().getListaAsignatura());
        lista.sort(Comparator.comparing(Asignatura::getNombre, String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(lista);
    }

    public Asignatura buscarAsignatura(int idAsignatura) {
        exigirAdmin();
        for (Asignatura a : Asignaturas.getInstancia().getListaAsignatura()) {
            if (a.getId() == idAsignatura) return a;
        }
        return null;
    }

    private void validarAsignatura(String nombre, int creditos, int cupo) {
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 100) {
            throw new IllegalArgumentException("Nombre de asignatura obligatorio (máximo 100 caracteres)");
        }
        if (creditos <= 0) throw new IllegalArgumentException("Los créditos deben ser mayores a cero");
        if (cupo <= 0) throw new IllegalArgumentException("El cupo debe ser mayor a cero");
    }

    public synchronized int agregarAsignatura(String nombre, int creditos, int cupo) {
        exigirAdmin();
        validarAsignatura(nombre, creditos, cupo);
        String limpio = nombre.trim();
        int id = persistencia.agregarAsignatura(limpio, creditos, cupo);
        try {
            // La tabla asignatura NO tiene columna estado. No se inventa su valor.
            Asignaturas.getInstancia().agregarAsignatura(
                    new Asignatura(id, limpio, creditos, cupo, '\0'));
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException("La asignatura se guardó, pero falló la memoria. Reiniciá SGA.", ex);
        }
        return id;
    }

    public synchronized void modificarAsignatura(int id, String nombre, int creditos, int cupo) {
        exigirAdmin();
        validarAsignatura(nombre, creditos, cupo);
        Asignatura actual = buscarAsignatura(id);
        if (actual == null) throw new IllegalArgumentException("La asignatura no existe");
        long inscriptos = Inscripciones.getInstancia().getListaInscripciones().stream()
                .filter(i -> i.getIdAsignatura() == id).count();
        if (cupo < inscriptos) {
            throw new IllegalArgumentException("El cupo no puede ser inferior a los estudiantes inscriptos (" + inscriptos + ")");
        }
        String limpio = nombre.trim();
        persistencia.modificarAsignatura(id, limpio, creditos, cupo);
        try {
            // Mutar el MISMO objeto: lo usan los cursos, grupos y ventanas.
            actual.setNombre(limpio);
            actual.setCantidadCreditos(creditos);
            actual.setCupoMaximo(cupo);
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException("Se modificó MariaDB pero falló la memoria. Reiniciá SGA.", ex);
        }
    }

    public synchronized void eliminarAsignatura(int id) {
        exigirAdmin();
        Asignatura actual = buscarAsignatura(id);
        if (actual == null) throw new IllegalArgumentException("La asignatura no existe");
        boolean inscripciones = Inscripciones.getInstancia().getListaInscripciones().stream()
                .anyMatch(i -> i.getIdAsignatura() == id);
        boolean cursos = Asignaturas_de_cursos.getInstancia().getListaAsignaturas_de_curso().stream()
                .anyMatch(c -> c.getAsignatura().getId() == id);
        boolean grupos = Grupos.getInstancia().getListaGrupos().stream()
                .anyMatch(g -> g.getAsignaturas().stream().anyMatch(a -> a.getId() == id));
        boolean notas = Notas.getInstancia().getListaNota().stream()
                .anyMatch(n -> n.getIdAsignatura() != null && n.getIdAsignatura() == id);
        boolean informes = Informes.getInstancia().getListaInformes().stream()
                .anyMatch(i -> i.getIdAsignatura() == id);
        if (inscripciones || cursos || grupos || notas || informes) {
            throw new IllegalStateException(
                    "No se puede eliminar: tiene inscripciones, docentes, grupos, notas o informes asociados");
        }
        persistencia.eliminarAsignatura(id);
        try {
            Asignaturas.getInstancia().eliminarAsignatura(actual);
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException("Se eliminó de MariaDB pero falló la memoria. Reiniciá SGA.", ex);
        }
    }

    // =============================================================
    // DOCENTES ASIGNADOS A MATERIAS
    // =============================================================

    public List<Docente> obtenerDocentesDeAsignatura(int idAsignatura) {
        exigirAdmin();
        List<Docente> resultado = new ArrayList<>();
        for (Asignatura_de_curso curso : Asignaturas_de_cursos.getInstancia()
                .getListaAsignaturas_de_curso()) {
            if (curso.getAsignatura().getId() == idAsignatura) {
                resultado.add(curso.getDocente());
            }
        }
        return List.copyOf(resultado);
    }

    public List<Docente> obtenerDocentesDisponiblesParaAsignatura(int idAsignatura) {
        exigirAdmin();
        if (buscarAsignatura(idAsignatura) == null) {
            throw new IllegalArgumentException("La asignatura no existe");
        }
        List<Docente> asignados = obtenerDocentesDeAsignatura(idAsignatura);
        List<Docente> resultado = new ArrayList<>();
        for (Docente docente : Docentes.getInstancia().getListaDocentes()) {
            boolean registrado = asignados.stream().anyMatch(d -> d.getCi().equals(docente.getCi()));
            if (!registrado) resultado.add(docente);
        }
        resultado.sort(Comparator.comparing(Docente::getNombre, String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(resultado);
    }

    public synchronized void asignarDocenteAsignatura(int idAsignatura, String cedulaDocente) {
        exigirAdmin();
        Asignatura asignatura = buscarAsignatura(idAsignatura);
        Docente docente = buscarDocente(cedulaDocente);
        if (asignatura == null || docente == null) {
            throw new IllegalArgumentException("La asignatura o el docente no existe");
        }
        boolean yaDicta = obtenerDocentesDeAsignatura(idAsignatura).stream()
                .anyMatch(d -> d.getCi().equals(cedulaDocente));
        if (yaDicta) throw new IllegalArgumentException("Este docente ya dicta esa asignatura");

        // Preparar el objeto completo antes del INSERT.
        List<Estudiante> alumnos = new ArrayList<>();
        for (Inscripcion ins : Inscripciones.getInstancia().getListaInscripciones()) {
            if (ins.getIdAsignatura() == idAsignatura) {
                for (Estudiante e : Estudiantes.getInstancia().getListaEstudiantes()) {
                    if (e.getCi().equals(ins.getCedulaEstudiante())) alumnos.add(e);
                }
            }
        }
        List<Nota> notas = new ArrayList<>();
        for (Nota n : Notas.getInstancia().getListaNota()) {
            if (n.getIdAsignatura() != null && n.getIdAsignatura() == idAsignatura) notas.add(n);
        }
        Asignatura_de_curso curso = new Asignatura_de_curso(
                asignatura, docente, alumnos, new Notas(notas), null);

        persistencia.asignarDocenteAsignatura(cedulaDocente, idAsignatura);
        try {
            Asignaturas_de_cursos.getInstancia().agregarAsignatura_de_curso(curso);
            // Mantener los objetos Grupo, reutilizando el curso creado.
            for (Grupo grupo : Grupos.getInstancia().getListaGrupos()) {
                if (grupo.getAsignaturas().stream().anyMatch(a -> a.getId() == idAsignatura)) {
                    grupo.getAsisg().agregarAsignatura_de_curso(curso);
                }
            }
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException("Se asignó el docente en MariaDB pero falló la memoria. Reiniciá SGA.", ex);
        }
    }

    public synchronized void desasignarDocenteAsignatura(int idAsignatura, String cedulaDocente) {
        exigirAdmin();
        Asignatura_de_curso existente = null;
        for (Asignatura_de_curso c : Asignaturas_de_cursos.getInstancia()
                .getListaAsignaturas_de_curso()) {
            if (c.getAsignatura().getId() == idAsignatura && c.getDocente().getCi().equals(cedulaDocente)) {
                existente = c;
                break;
            }
        }
        if (existente == null) throw new IllegalArgumentException("No existe esa asignación docente");
        final Asignatura_de_curso curso = existente;
        if (Informes.getInstancia().getListaInformes().stream()
                .anyMatch(i -> i.getIdAsignatura() == idAsignatura
                        && i.getCedulaDocente().equals(cedulaDocente))) {
            throw new IllegalStateException("No se puede quitar: el docente tiene informes de esa asignatura");
        }
        persistencia.desasignarDocenteAsignatura(cedulaDocente, idAsignatura);
        try {
            for (Grupo grupo : Grupos.getInstancia().getListaGrupos()) {
                grupo.getAsisg().eliminarAsignatura_de_curso(curso);
            }
            Asignaturas_de_cursos.getInstancia().eliminarAsignatura_de_curso(curso);
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException("Se quitó la asignación en MariaDB pero falló la memoria. Reiniciá SGA.", ex);
        }
    }

    // =============================================================
    // INFORMES GENERALES DEL ADMINISTRADOR (SOLO LECTURA)
    // Fuente: colecciones cargadas por el Splash, no modifica MariaDB.
    // =============================================================

    public InformeGeneral generarInformeGeneral(String tipo) {
        exigirAdmin();
        if (!InformeGeneral.TIPOS.contains(tipo)) {
            throw new IllegalArgumentException("Tipo de informe no válido: " + tipo);
        }

        List<List<String>> filas = new ArrayList<>();
        List<String> encabezados;

        // Índices provenientes de las colecciones que ya se cargaron desde la BD.
        java.util.Map<String, String> alumnos = new java.util.HashMap<>();
        for (Estudiante e : Estudiantes.getInstancia().getListaEstudiantes()) {
            alumnos.put(e.getCi(), e.getNombre());
        }
        java.util.Map<String, String> profesores = new java.util.HashMap<>();
        for (Docente d : Docentes.getInstancia().getListaDocentes()) {
            profesores.put(d.getCi(), d.getNombre());
        }
        java.util.Map<Integer, String> materias = new java.util.HashMap<>();
        for (Asignatura a : Asignaturas.getInstancia().getListaAsignatura()) {
            materias.put(a.getId(), a.getNombre());
        }

        switch (tipo) {
            case InformeGeneral.ESTUDIANTES -> {
                encabezados = List.of("Cédula", "Nombre", "Inscripciones", "Comentario AC");
                for (Estudiante e : obtenerEstudiantes()) {
                    long cantidad = Inscripciones.getInstancia().getListaInscripciones().stream()
                            .filter(i -> e.getCi().equals(i.getCedulaEstudiante())).count();
                    filas.add(List.of(e.getCi(), e.getNombre(), Long.toString(cantidad),
                            texto(e.getComentario())));
                }
            }
            case InformeGeneral.DOCENTES -> {
                encabezados = List.of("Cédula", "Nombre", "Asignaturas asignadas");
                for (Docente d : obtenerDocentes()) {
                    int cantidad = obtenerAsignaturasDocente(d.getCi()).size();
                    filas.add(List.of(d.getCi(), d.getNombre(), Integer.toString(cantidad)));
                }
            }
            case InformeGeneral.ASIGNATURAS -> {
                encabezados = List.of("ID", "Asignatura", "Créditos", "Cupo máximo",
                        "Inscriptos", "Docentes");
                for (Asignatura a : obtenerAsignaturas()) {
                    long inscriptos = Inscripciones.getInstancia().getListaInscripciones().stream()
                            .filter(i -> i.getIdAsignatura() == a.getId()).count();
                    int docentes = obtenerDocentesDeAsignatura(a.getId()).size();
                    filas.add(List.of(Integer.toString(a.getId()), a.getNombre(),
                            Integer.toString(a.getCantidadCreditos()),
                            Integer.toString(a.getCupoMaximo()), Long.toString(inscriptos),
                            Integer.toString(docentes)));
                }
            }
            case InformeGeneral.INSCRIPCIONES -> {
                encabezados = List.of("Cédula", "Estudiante", "ID asignatura", "Asignatura",
                        "Estado", "Nota final");
                List<Inscripcion> inscripciones = new ArrayList<>(obtenerInscripciones());
                inscripciones.sort(Comparator
                        .comparing((Inscripcion i) -> alumnos.getOrDefault(i.getCedulaEstudiante(), ""),
                                String.CASE_INSENSITIVE_ORDER)
                        .thenComparingInt(Inscripcion::getIdAsignatura));
                for (Inscripcion i : inscripciones) {
                    filas.add(List.of(i.getCedulaEstudiante(),
                            alumnos.getOrDefault(i.getCedulaEstudiante(), "(Sin registro)"),
                            Integer.toString(i.getIdAsignatura()),
                            materias.getOrDefault(i.getIdAsignatura(), "(Sin registro)"),
                            texto(i.getEstado()),
                            i.getNota() == null ? "Sin nota final" : i.getNota().toPlainString()));
                }
            }
            case InformeGeneral.CALIFICACIONES -> {
                encabezados = List.of("ID nota", "Cédula", "Estudiante", "Asignatura",
                        "Tipo", "Calificación", "Fecha");
                List<Nota> notas = new ArrayList<>(Notas.getInstancia().getListaNota());
                notas.sort(Comparator.comparingInt(Nota::getidNota));
                for (Nota n : notas) {
                    Integer id = n.getIdAsignatura();
                    filas.add(List.of(Integer.toString(n.getidNota()), texto(n.getCi()),
                            alumnos.getOrDefault(n.getCi(), "(Sin registro)"),
                            id == null ? "(Sin asignatura)"
                                    : materias.getOrDefault(id, "(ID " + id + ")"),
                            texto(n.getTipo()), Integer.toString(n.getNota()),
                            n.getFecha() == null ? "" : n.getFecha().toString()));
                }
            }
            case InformeGeneral.INFORMES_ACADEMICOS -> {
                encabezados = List.of("ID", "Estudiante", "Docente", "Asignatura",
                        "Fecha", "Contenido");
                List<InformeAcademico> informes = new ArrayList<>(
                        Informes.getInstancia().getListaInformes());
                informes.sort(Comparator.comparingInt(InformeAcademico::getIdInforme));
                for (InformeAcademico i : informes) {
                    filas.add(List.of(Integer.toString(i.getIdInforme()),
                            alumnos.getOrDefault(i.getCedulaEstudiante(), i.getCedulaEstudiante()),
                            profesores.getOrDefault(i.getCedulaDocente(), i.getCedulaDocente()),
                            materias.getOrDefault(i.getIdAsignatura(),
                                    "(ID " + i.getIdAsignatura() + ")"),
                            i.getFecha() == null ? "" : i.getFecha().toString(),
                            texto(i.getContenido())));
                }
            }
            default -> throw new IllegalArgumentException("Tipo de informe no reconocido");
        }
        return new InformeGeneral(tipo, encabezados, filas);
    }

    private static String texto(String dato) {
        return dato == null ? "" : dato;
    }

}
