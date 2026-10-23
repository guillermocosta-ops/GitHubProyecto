package Logica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Consultas para el estudiante autenticado, exclusivamente desde
 * las colecciones de Logica cargadas por el Splash.
 * No utiliza JDBC ni permite consultar la cedula de otro usuario.
 */
public class ControlEstudiante {

    private Estudiante estudianteActual() {
        CargaInicial.exigirCarga();

        String ci = Login.getCedulaActual();
        if (ci == null || ci.isBlank()) {
            throw new IllegalStateException(
                    "No hay un estudiante con la sesión iniciada.");
        }

        for (Estudiante estudiante :
                Estudiantes.getInstancia().getListaEstudiantes()) {
            if (ci.equals(estudiante.getCi())) {
                return estudiante;
            }
        }

        throw new IllegalStateException(
                "La sesión actual no corresponde a un estudiante.");
    }

    public String obtenerMiCedula() {
        return estudianteActual().getCi();
    }

    public String obtenerMiNombre() {
        return estudianteActual().getNombre();
    }

    /** Devuelve solamente las calificaciones del estudiante autenticado. */
    public List<Nota> obtenerMisNotas() {
        String ci = estudianteActual().getCi();
        List<Nota> resultado = new ArrayList<>();

        for (Nota nota : Notas.getInstancia().getListaNota()) {
            if (ci.equals(nota.getCi())) {
                resultado.add(nota);
            }
        }

        resultado.sort(Comparator
                .comparing((Nota nota) -> nombreAsignatura(nota.getIdAsignatura()))
                .thenComparing(Nota::getFecha)
                .thenComparingInt(Nota::getidNota));

        return List.copyOf(resultado);
    }

    /** Materias en las que figura inscripto el estudiante actual. */
    public List<Inscripcion> obtenerMisInscripciones() {
        String ci = estudianteActual().getCi();
        List<Inscripcion> resultado = new ArrayList<>();

        for (Inscripcion inscripcion :
                Inscripciones.getInstancia().getListaInscripciones()) {
            if (ci.equals(inscripcion.getCedulaEstudiante())) {
                resultado.add(inscripcion);
            }
        }

        resultado.sort(Comparator.comparing(
                inscripcion -> nombreAsignatura(inscripcion.getIdAsignatura())));
        return List.copyOf(resultado);
    }

    /** Busca la asignatura en la coleccion cargada, nunca en MariaDB. */
    public Asignatura obtenerAsignatura(int idAsignatura) {
        estudianteActual();

        for (Asignatura asignatura :
                Asignaturas.getInstancia().getListaAsignatura()) {
            if (asignatura.getId() == idAsignatura) {
                return asignatura;
            }
        }

        throw new IllegalStateException(
                "No se encontro la asignatura con ID " + idAsignatura);
    }

    /** Retorna null cuando todavia no hay evaluaciones para la materia. */
    public Double calcularMiPromedio(int idAsignatura) {
        String ci = estudianteActual().getCi();
        validarInscripcion(ci, idAsignatura);

        int suma = 0;
        int cantidad = 0;
        for (Nota nota : Notas.getInstancia().getListaNota()) {
            if (ci.equals(nota.getCi())
                    && nota.getIdAsignatura() != null
                    && nota.getIdAsignatura() == idAsignatura) {
                suma += nota.getNota();
                cantidad++;
            }
        }

        if (cantidad == 0) {
            return null;
        }
        return Math.round((suma * 100.0) / cantidad) / 100.0;
    }

    public int contarMisEvaluaciones(int idAsignatura) {
        String ci = estudianteActual().getCi();
        validarInscripcion(ci, idAsignatura);

        int cantidad = 0;
        for (Nota nota : Notas.getInstancia().getListaNota()) {
            if (ci.equals(nota.getCi())
                    && nota.getIdAsignatura() != null
                    && nota.getIdAsignatura() == idAsignatura) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private void validarInscripcion(String ci, int idAsignatura) {
        for (Inscripcion inscripcion :
                Inscripciones.getInstancia().getListaInscripciones()) {
            if (ci.equals(inscripcion.getCedulaEstudiante())
                    && inscripcion.getIdAsignatura() == idAsignatura) {
                return;
            }
        }
        throw new IllegalArgumentException(
                "El estudiante no esta inscripto en la asignatura " + idAsignatura);
    }

    private String nombreAsignatura(Integer idAsignatura) {
        if (idAsignatura == null) {
            return "Asignatura sin identificar";
        }
        for (Asignatura asignatura :
                Asignaturas.getInstancia().getListaAsignatura()) {
            if (asignatura.getId() == idAsignatura) {
                return asignatura.getNombre();
            }
        }
        return "Asignatura " + idAsignatura;
    }
}
