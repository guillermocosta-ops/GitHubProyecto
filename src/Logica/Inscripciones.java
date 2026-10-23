package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inscripciones {
    private static final Inscripciones INSTANCIA = new Inscripciones();
    private final List<Inscripcion> listaInscripciones = new ArrayList<>();

    public Inscripciones() { }
    public Inscripciones(List<Inscripcion> elementos) { reemplazar(elementos); }
    public static Inscripciones getInstancia() { return INSTANCIA; }
    public synchronized List<Inscripcion> getListaInscripciones() { return List.copyOf(listaInscripciones); }
    public synchronized void reemplazar(List<Inscripcion> elementos) {
        Objects.requireNonNull(elementos);
        listaInscripciones.clear();
        listaInscripciones.addAll(elementos);
    }
    public synchronized void agregarInscripcion(Inscripcion elemento) {
        listaInscripciones.add(Objects.requireNonNull(elemento));
    }
    public synchronized void eliminarInscripcion(Inscripcion elemento) { listaInscripciones.remove(elemento); }
}
