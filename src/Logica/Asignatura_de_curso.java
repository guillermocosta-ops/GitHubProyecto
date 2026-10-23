
package Logica;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


public class Asignatura_de_curso {
    private Asignatura asignatura;
    private Docente docente;
    private  List<Estudiante> listaEstudiantes;
    private  Notas notas;
    private String estado; 

    public Asignatura_de_curso(Asignatura asignatura, Docente docente, List<Estudiante> listaEstudiantes, Notas notas, String estado) {
        this.asignatura = asignatura;
        this.docente = docente;
        this.listaEstudiantes = new ArrayList<>(listaEstudiantes);
        this.notas = notas;
        this.estado = estado;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public List<Estudiante> getListaEstudiantes() { return List.copyOf(listaEstudiantes); }

    public Notas getNotas() { return notas; }

    // Conserva el mismo objeto de curso referenciado por Grupos y otras clases.
    public synchronized void agregarEstudiante(Estudiante estudiante) {
        Objects.requireNonNull(estudiante, "estudiante");
        for (Estudiante existente : listaEstudiantes) {
            if (existente.getCi().equals(estudiante.getCi())) {
                return; // Ya figura en este curso
            }
        }
        listaEstudiantes.add(estudiante);
    }

    /** Quita el estudiante de este curso sin reemplazar el objeto compartido. */
    public synchronized void quitarEstudiante(String cedula) {
        if (cedula == null) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        listaEstudiantes.removeIf(e -> cedula.equals(e.getCi()));
    }

    /** Promedio simple de las evaluaciones de este curso (null si no tiene notas). */
    public Double calcularPromedioSimpleEstudiante(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        int suma = 0;
        int cantidad = 0;
        for (Nota nota : notas.getListaNota()) {
            if (cedula.equals(nota.getCi()) && nota.getIdAsignatura() != null
                    && nota.getIdAsignatura() == asignatura.getId()) {
                suma += nota.getNota();
                cantidad++;
            }
        }
        return cantidad == 0 ? null
                : Math.round(suma * 100.0 / cantidad) / 100.0;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
