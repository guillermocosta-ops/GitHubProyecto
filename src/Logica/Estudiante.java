
package Logica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Estudiante extends Usuario {
    private String comentario;

    public Estudiante(String ci, String nombre, String contrasena,String comentario) {
        super(ci, nombre, contrasena);
        this.comentario=comentario;
    }
    
    public Estudiante(String ci, String nombre){
        super(ci, nombre);
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    @Override
    // Sobrescribe el toString() de Object para que el JComboBox o las listas 
    // muestren "Cédula - Nombre" en la pantalla en lugar del código interno de Java.
    
    public String toString() {
        return getCi() + " - " + getNombre();
}

    private Estudiante estudianteActual() {
        CargaInicial.exigirCarga();
        String sesion = Login.getCedulaActual();
        if (sesion == null || !sesion.equals(getCi()) ||
                Estudiantes.getInstancia().getListaEstudiantes().stream()
                        .noneMatch(e -> e.getCi().equals(getCi()))) {
            throw new IllegalStateException(
                    "La operación requiere una sesión de este estudiante");
        }
        return this;
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
