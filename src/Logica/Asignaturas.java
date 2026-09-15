
package Logica;
import java.util.ArrayList;
import java.util.List;
public class Asignaturas {
    private final List<Asignatura> listaAsignatura;

    public Asignaturas(List<Asignatura> listaAsignatura) {
        this.listaAsignatura = listaAsignatura;
    }

    public List<Asignatura> getListaAsignatura() {
        return listaAsignatura;
    }
 
    public void agregarAsignatura(Asignatura asignatura) {
        listaAsignatura.add(asignatura);
    }

    public void eliminarAsignatura(Asignatura asignatura) {
        listaAsignatura.remove(asignatura);
    }
}
