package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuarios {
    private static final Usuarios INSTANCIA = new Usuarios();
    private final List<Usuario> listaUsuarios = new ArrayList<>();

    public Usuarios() { }
    public Usuarios(List<Usuario> elementos) { reemplazar(elementos); }
    public static Usuarios getInstancia() { return INSTANCIA; }
    public synchronized List<Usuario> getListaUsuarios() { return List.copyOf(listaUsuarios); }
    public synchronized void reemplazar(List<Usuario> elementos) {
        Objects.requireNonNull(elementos);
        listaUsuarios.clear();
        listaUsuarios.addAll(elementos);
    }
    public synchronized void agregarUsuario(Usuario elemento) {
        listaUsuarios.add(Objects.requireNonNull(elemento));
    }
    public synchronized void eliminarUsuario(Usuario elemento) { listaUsuarios.remove(elemento); }
}
