package Logica;

import Persistencia.Persistencia;
import java.util.ArrayList;
import java.util.List;

/** Operaciones docentes sobre objetos cargados durante el Splash. */
public class ControlDocente {

    private final Persistencia persistencia = new Persistencia();

    public Estudiantes obtenerEstudiantes() {
        CargaInicial.exigirCarga();
        return Estudiantes.getInstancia();
    }

    public List<Asignatura> obtenerMisAsignaturas() {
        CargaInicial.exigirCarga();

        String ci = Login.getCedulaActual();
        List<Asignatura> resultado = new ArrayList<>();

        if (ci == null) {
            return List.of();
        }

        for (Asignatura_de_curso curso :
                Asignaturas_de_cursos.getInstancia()
                        .getListaAsignaturas_de_curso()) {

            if (curso.getDocente().getCi().equals(ci)) {
                resultado.add(curso.getAsignatura());
            }
        }
        return List.copyOf(resultado);
    }

    public List<Estudiante> obtenerEstudiantesDeAsignatura(int idAsignatura) {
        CargaInicial.exigirCarga();

        List<Estudiante> lista = new ArrayList<>();
        for (Inscripcion inscripcion :
                Inscripciones.getInstancia().getListaInscripciones()) {

            if (inscripcion.getIdAsignatura() == idAsignatura) {
                for (Estudiante estudiante :
                        Estudiantes.getInstancia().getListaEstudiantes()) {
                    if (estudiante.getCi().equals(
                            inscripcion.getCedulaEstudiante())) {
                        lista.add(estudiante);
                    }
                }
            }
        }
        return List.copyOf(lista);
    }

    public List<Nota> buscarCalificaciones(String cedula) {
        CargaInicial.exigirCarga();

        List<Nota> lista = new ArrayList<>();
        for (Nota nota : Notas.getInstancia().getListaNota()) {
            if (cedula == null || cedula.isBlank()
                    || nota.getCi().equals(cedula.trim())) {
                lista.add(nota);
            }
        }
        return List.copyOf(lista);
    }

    public synchronized int ingresarDatosNota(
            String ci, int asignatura, String tipo, int calificacion) {

        CargaInicial.exigirCarga();
        validarNota(calificacion);

        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Seleccione el tipo de evaluación");
        }

        boolean inscrito = false;
        for (Inscripcion inscripcion :
                Inscripciones.getInstancia().getListaInscripciones()) {
            if (inscripcion.getCedulaEstudiante().equals(ci)
                    && inscripcion.getIdAsignatura() == asignatura) {
                inscrito = true;
            }
        }
        if (!inscrito) {
            throw new IllegalArgumentException(
                    "El estudiante no cursa esa asignatura");
        }

        int id = persistencia.ingresarDatosNota(
                ci, asignatura, tipo, calificacion);
        sincronizarNotas();
        return id;
    }

    public synchronized void modificarNota(int id, int nueva) {
        CargaInicial.exigirCarga();
        validarNota(nueva);

        if (Notas.getInstancia().buscarPorId(id) == null) {
            throw new IllegalArgumentException("La nota no existe");
        }
        persistencia.modificarNota(id, nueva);
        sincronizarNotas();
    }

    public synchronized void eliminarNota(int id) {
        CargaInicial.exigirCarga();

        if (Notas.getInstancia().buscarPorId(id) == null) {
            throw new IllegalArgumentException("La nota no existe");
        }
        persistencia.eliminarNota(id);
        sincronizarNotas();
    }

    public Double calcularPromedioSimpleEstudiante(
            String cedula, int idAsignatura) {

        CargaInicial.exigirCarga();
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }

        int cantidad = 0;
        int suma = 0;
        for (Nota nota : Notas.getInstancia().getListaNota()) {
            if (cedula.equals(nota.getCi())
                    && nota.getIdAsignatura() != null
                    && nota.getIdAsignatura() == idAsignatura) {
                suma += nota.getNota();
                cantidad++;
            }
        }
        if (cantidad == 0) {
            return null;
        }
        return Math.round(suma * 100.0 / cantidad) / 100.0;
    }

    // ========================================
    // INFORMES ACADEMICOS
    // ========================================

    /** Devuelve el estudiante solo si está inscripto en la asignatura del docente. */
    public Estudiante buscarEstudianteParaInforme(
            String cedula, int idAsignatura) {

        CargaInicial.exigirCarga();

        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("Ingresá la cédula del estudiante");
        }

        String cedulaBuscada = cedula.trim();
        String ciDocente = Login.getCedulaActual();
        if (ciDocente == null || ciDocente.isBlank()) {
            throw new IllegalStateException("No hay un docente con sesión iniciada");
        }

        boolean dictaLaAsignatura = false;
        for (Asignatura_de_curso curso :
                Asignaturas_de_cursos.getInstancia()
                        .getListaAsignaturas_de_curso()) {
            if (curso.getDocente().getCi().equals(ciDocente)
                    && curso.getAsignatura().getId() == idAsignatura) {
                dictaLaAsignatura = true;
                break;
            }
        }
        if (!dictaLaAsignatura) {
            throw new IllegalArgumentException(
                    "La asignatura no corresponde al docente actual");
        }

        boolean inscripto = false;
        for (Inscripcion inscripcion :
                Inscripciones.getInstancia().getListaInscripciones()) {
            if (inscripcion.getCedulaEstudiante().equals(cedulaBuscada)
                    && inscripcion.getIdAsignatura() == idAsignatura) {
                inscripto = true;
                break;
            }
        }
        if (!inscripto) {
            throw new IllegalArgumentException(
                    "El estudiante no está inscripto en esa asignatura");
        }

        for (Estudiante estudiante :
                Estudiantes.getInstancia().getListaEstudiantes()) {
            if (estudiante.getCi().equals(cedulaBuscada)) {
                return estudiante;
            }
        }

        throw new IllegalStateException(
                "La inscripción existe pero no se cargó el estudiante");
    }

    /** Primero confirma el INSERT y luego agrega el mismo objeto a Informes. */
    public InformeAcademico generarInforme(
            String cedulaEstudiante, int idAsignatura, String contenido) {

        CargaInicial.exigirCarga();
        buscarEstudianteParaInforme(cedulaEstudiante, idAsignatura);

        if (contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException("Escribí el contenido del informe");
        }

        String ciDocente = Login.getCedulaActual();
        InformeAcademico guardado = persistencia.ingresarInforme(
                cedulaEstudiante.trim(), ciDocente, idAsignatura,
                contenido.trim());

        try {
            Informes.getInstancia().agregarInformeAcademico(guardado);
        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "El informe quedó guardado en MariaDB, pero no se pudo "
                    + "actualizar la lista en memoria. Reiniciá SGA.", ex);
        }
        return guardado;
    }

    // ========================================
    // CONSULTAR INFORMES DESDE MEMORIA
    // ========================================

    /** Muestra únicamente los informes escritos por el docente actual.
     * cedula vacía = todos los estudiantes; idAsignatura null = todas.
     */
    public List<InformeAcademico> buscarMisInformes(
            String cedula, Integer idAsignatura) {

        CargaInicial.exigirCarga();
        String docenteActual = Login.getCedulaActual();
        if (docenteActual == null || docenteActual.isBlank()) {
            throw new IllegalStateException("No hay un docente con sesión iniciada");
        }

        String filtroCi = cedula == null ? "" : cedula.trim();
        List<InformeAcademico> resultado = new ArrayList<>();
        for (InformeAcademico informe :
                Informes.getInstancia().getListaInformes()) {

            if (!docenteActual.equals(informe.getCedulaDocente())) {
                continue;
            }
            if (!filtroCi.isEmpty()
                    && !filtroCi.equals(informe.getCedulaEstudiante())) {
                continue;
            }
            if (idAsignatura != null
                    && idAsignatura.intValue() != informe.getIdAsignatura()) {
                continue;
            }
            resultado.add(informe);
        }

        resultado.sort(java.util.Comparator
                .comparing(InformeAcademico::getFecha)
                .thenComparingInt(InformeAcademico::getIdInforme)
                .reversed());
        return List.copyOf(resultado);
    }

    public String obtenerNombreEstudiante(String cedula) {
        CargaInicial.exigirCarga();
        for (Estudiante estudiante :
                Estudiantes.getInstancia().getListaEstudiantes()) {
            if (estudiante.getCi().equals(cedula)) {
                return estudiante.getNombre();
            }
        }
        return "(Estudiante no encontrado)";
    }

    public String obtenerNombreAsignatura(int idAsignatura) {
        CargaInicial.exigirCarga();
        for (Asignatura asignatura :
                Asignaturas.getInstancia().getListaAsignatura()) {
            if (asignatura.getId() == idAsignatura) {
                return asignatura.getNombre();
            }
        }
        return "(Asignatura no encontrada)";
    }

    private void validarNota(int nota) {
        if (nota < 1 || nota > 12) {
            throw new IllegalArgumentException(
                    "La nota debe estar entre 1 y 12");
        }
    }

    private void sincronizarNotas() {
        try {
            Notas notasActualizadas = persistencia.obtenerNotas();
            Notas.getInstancia().reemplazar(notasActualizadas.getListaNota());

            for (Asignatura_de_curso curso :
                    Asignaturas_de_cursos.getInstancia()
                            .getListaAsignaturas_de_curso()) {
                List<Nota> notasCurso = new ArrayList<>();
                for (Nota nota : notasActualizadas.getListaNota()) {
                    if (nota.getIdAsignatura() != null
                            && nota.getIdAsignatura()
                            == curso.getAsignatura().getId()) {
                        notasCurso.add(nota);
                    }
                }
                curso.getNotas().reemplazar(notasCurso);
            }

        } catch (RuntimeException ex) {
            CargaInicial.invalidar();
            throw new IllegalStateException(
                    "La operación pudo haberse guardado en MariaDB, pero "
                    + "falló la actualización de memoria. Reiniciá SGA.", ex);
        }
    }
}
