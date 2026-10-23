package Logica;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Notas {
    private static final Notas INSTANCIA = new Notas();
    private final List<Nota> listaNota = new ArrayList<>();
    public Notas() { }
    public Notas(List<Nota> elementos) { reemplazar(elementos); }
    public static Notas getInstancia() { return INSTANCIA; }
    public synchronized List<Nota> getListaNota() { return List.copyOf(listaNota); }
    public synchronized void reemplazar(List<Nota> nuevos) {
        Objects.requireNonNull(nuevos);
        listaNota.clear();
        for (Nota n : nuevos) agregarNota(n);
    }
    public synchronized void agregarNota(Nota nota) {
        Objects.requireNonNull(nota);
        if (buscarPorId(nota.getidNota()) != null) throw new IllegalArgumentException("ID de nota duplicado");
        listaNota.add(nota);
    }
    public synchronized Nota buscarPorId(int id) {
        for (Nota n : listaNota) if (n.getidNota()==id) return n;
        return null;
    }
    public synchronized void modificarNota(Nota nueva) {
        for (int i=0; i<listaNota.size(); i++) {
            if (listaNota.get(i).getidNota()==nueva.getidNota()) { listaNota.set(i,nueva); return; }
        }
        throw new IllegalArgumentException("La nota no existe");
    }
    public synchronized void eliminarNota(Nota n) { eliminarNota(n.getidNota()); }
    public synchronized void eliminarNota(int id) {
        if (!listaNota.removeIf(n -> n.getidNota()==id)) throw new IllegalArgumentException("La nota no existe");
    }
}
