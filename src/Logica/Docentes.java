package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Docentes {
    private static final Docentes INSTANCIA = new Docentes();
    private final List<Docente> listaDocentes = new ArrayList<>();

    public Docentes() { }
    public Docentes(List<Docente> elementos) { reemplazar(elementos); }
    public static Docentes getInstancia() { return INSTANCIA; }
    public synchronized List<Docente> getListaDocentes() { return List.copyOf(listaDocentes); }
    public synchronized void reemplazar(List<Docente> elementos) {
        Objects.requireNonNull(elementos);
        listaDocentes.clear();
        listaDocentes.addAll(elementos);
    }
    public synchronized void agregarDocente(Docente elemento) {
        listaDocentes.add(Objects.requireNonNull(elemento));
    }
    public synchronized void setListaDocentes(List<Docente> nuevos) { reemplazar(nuevos); }
    public synchronized void eliminarDocente(Docente elemento) { listaDocentes.remove(elemento); }

    /** Obtiene la entidad autenticada desde la colección cargada en el Splash. */
    public Docente obtenerSesionActual() {
        CargaInicial.exigirCarga();
        String ci = Login.getCedulaActual();
        if (ci != null) {
            for (Docente entidad : getListaDocentes()) {
                if (ci.equals(entidad.getCi())) return entidad;
            }
        }
        throw new IllegalStateException("No hay un docente con sesión iniciada");
    }
}
