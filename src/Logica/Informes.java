package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Informes {
    private static final Informes INSTANCIA = new Informes();
    private final List<InformeAcademico> listaInformes = new ArrayList<>();

    public Informes() { }
    public Informes(List<InformeAcademico> elementos) { reemplazar(elementos); }
    public static Informes getInstancia() { return INSTANCIA; }
    public synchronized List<InformeAcademico> getListaInformes() { return List.copyOf(listaInformes); }
    public synchronized void reemplazar(List<InformeAcademico> elementos) {
        Objects.requireNonNull(elementos);
        listaInformes.clear();
        listaInformes.addAll(elementos);
    }
    public synchronized void agregarInformeAcademico(InformeAcademico elemento) {
        listaInformes.add(Objects.requireNonNull(elemento));
    }
    public synchronized void eliminarInformeAcademico(InformeAcademico elemento) { listaInformes.remove(elemento); }
}
