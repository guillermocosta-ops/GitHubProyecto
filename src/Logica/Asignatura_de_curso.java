
package Logica;

import java.util.List;


public class Asignatura_de_curso {
    private Asignatura asignatura;
    private Docente docente;
    private final List<Estudiante> listaEstudiantes;
    private final Notas notas;
    private String estado; 

    public Asignatura_de_curso(Asignatura asignatura, Docente docente, List<Estudiante> listaEstudiantes, Notas notas, String estado) {
        this.asignatura = asignatura;
        this.docente = docente;
        this.listaEstudiantes = listaEstudiantes;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
