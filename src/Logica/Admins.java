
package Logica;
import java.util.ArrayList;
import java.util.List;
public class Admins {
       private final List<Admin> listaAdmins;

    public Admins() {
        listaAdmins = new ArrayList<>();
    }

    public List<Admin> getListaAdmins() {
        return List.copyOf(listaAdmins);
    }

    public void agregarAdmin(Admin admin) {
        listaAdmins.add(admin);
    }

    public void eliminarAdmin(Admin admin) {
        listaAdmins.remove(admin);
    }
}
