package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Admins {
    private static final Admins INSTANCIA = new Admins();
    private final List<Admin> listaAdmins = new ArrayList<>();

    public Admins() { }
    public Admins(List<Admin> elementos) { reemplazar(elementos); }
    public static Admins getInstancia() { return INSTANCIA; }
    public synchronized List<Admin> getListaAdmins() { return List.copyOf(listaAdmins); }
    public synchronized void reemplazar(List<Admin> elementos) {
        Objects.requireNonNull(elementos);
        listaAdmins.clear();
        listaAdmins.addAll(elementos);
    }
    public synchronized void agregarAdmin(Admin elemento) {
        listaAdmins.add(Objects.requireNonNull(elemento));
    }
    public synchronized void eliminarAdmin(Admin elemento) { listaAdmins.remove(elemento); }

    /** Obtiene la entidad autenticada desde la colección cargada en el Splash. */
    public Admin obtenerSesionActual() {
        CargaInicial.exigirCarga();
        String ci = Login.getCedulaActual();
        if (ci != null) {
            for (Admin entidad : getListaAdmins()) {
                if (ci.equals(entidad.getCi())) return entidad;
            }
        }
        throw new IllegalStateException("No hay un administrador con sesión iniciada");
    }
}
