package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Asignaturas {
    private static final Asignaturas INSTANCIA = new Asignaturas();
    private final List<Asignatura> listaAsignatura = new ArrayList<>();

    public Asignaturas() { }
    public Asignaturas(List<Asignatura> elementos) { reemplazar(elementos); }
    public static Asignaturas getInstancia() { return INSTANCIA; }
    public synchronized List<Asignatura> getListaAsignatura() { return List.copyOf(listaAsignatura); }
    public synchronized void reemplazar(List<Asignatura> elementos) {
        Objects.requireNonNull(elementos);
        listaAsignatura.clear();
        listaAsignatura.addAll(elementos);
    }
    public synchronized void agregarAsignatura(Asignatura elemento) {
        listaAsignatura.add(Objects.requireNonNull(elemento));
    }
    public synchronized void eliminarAsignatura(Asignatura elemento) { listaAsignatura.remove(elemento); }
}
