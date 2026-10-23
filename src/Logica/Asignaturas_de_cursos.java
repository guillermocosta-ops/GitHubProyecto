package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Asignaturas_de_cursos {
    private static final Asignaturas_de_cursos INSTANCIA = new Asignaturas_de_cursos();
    private final List<Asignatura_de_curso> listaAsignaturas_de_curso = new ArrayList<>();

    public Asignaturas_de_cursos() { }
    public Asignaturas_de_cursos(List<Asignatura_de_curso> elementos) { reemplazar(elementos); }
    public static Asignaturas_de_cursos getInstancia() { return INSTANCIA; }
    public synchronized List<Asignatura_de_curso> getListaAsignaturas_de_curso() { return List.copyOf(listaAsignaturas_de_curso); }
    public synchronized void reemplazar(List<Asignatura_de_curso> elementos) {
        Objects.requireNonNull(elementos);
        listaAsignaturas_de_curso.clear();
        listaAsignaturas_de_curso.addAll(elementos);
    }
    public synchronized void agregarAsignatura_de_curso(Asignatura_de_curso elemento) {
        listaAsignaturas_de_curso.add(Objects.requireNonNull(elemento));
    }
    public synchronized void eliminarAsignatura_de_curso(Asignatura_de_curso elemento) { listaAsignaturas_de_curso.remove(elemento); }
}
