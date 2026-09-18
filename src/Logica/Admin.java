
package Logica;


public class Admin extends Usuario {
    private String cargo;

    public Admin(String ci, String nombre, String contrasena,String cargo) {
        super(ci, nombre, contrasena);
        this.cargo=cargo;
    }
//xd
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}
