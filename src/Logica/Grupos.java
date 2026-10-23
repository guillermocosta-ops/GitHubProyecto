package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Grupos {
    private static final Grupos INSTANCIA = new Grupos();
    private final List<Grupo> listaGrupos = new ArrayList<>();

    public Grupos() { }
    public Grupos(List<Grupo> elementos) { reemplazar(elementos); }
    public static Grupos getInstancia() { return INSTANCIA; }
    public synchronized List<Grupo> getListaGrupos() { return List.copyOf(listaGrupos); }
    public synchronized void reemplazar(List<Grupo> elementos) {
        Objects.requireNonNull(elementos);
        listaGrupos.clear();
        listaGrupos.addAll(elementos);
    }
    public synchronized void agregarGrupo(Grupo elemento) {
        listaGrupos.add(Objects.requireNonNull(elemento));
    }
    public synchronized void eliminarGrupo(Grupo elemento) { listaGrupos.remove(elemento); }
}
