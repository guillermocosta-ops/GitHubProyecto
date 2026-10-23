package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Estudiantes {
    private static final Estudiantes INSTANCIA = new Estudiantes();
    private final List<Estudiante> listaEstudiantes = new ArrayList<>();

    public Estudiantes() { }
    public Estudiantes(List<Estudiante> elementos) { reemplazar(elementos); }
    public static Estudiantes getInstancia() { return INSTANCIA; }
    public synchronized List<Estudiante> getListaEstudiantes() { return List.copyOf(listaEstudiantes); }
    public synchronized void reemplazar(List<Estudiante> elementos) {
        Objects.requireNonNull(elementos);
        listaEstudiantes.clear();
        listaEstudiantes.addAll(elementos);
    }
    public synchronized void agregarEstudiante(Estudiante elemento) {
        listaEstudiantes.add(Objects.requireNonNull(elemento));
    }
    public synchronized void setListaEstudiantes(List<Estudiante> nuevos) { reemplazar(nuevos); }
    public synchronized void eliminarEstudiante(Estudiante elemento) { listaEstudiantes.remove(elemento); }

    /** Obtiene la entidad autenticada desde la colección cargada en el Splash. */
    public Estudiante obtenerSesionActual() {
        CargaInicial.exigirCarga();
        String ci = Login.getCedulaActual();
        if (ci != null) {
            for (Estudiante entidad : getListaEstudiantes()) {
                if (ci.equals(entidad.getCi())) return entidad;
            }
        }
        throw new IllegalStateException("No hay un estudiante con sesión iniciada");
    }
}
