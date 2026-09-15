
package Logica;


public class Usuario {
    private String ci;
    private String nombre;
    private String contrasena;

    public Usuario(String ci, String nombre, String contrasena) {
        this.ci = ci;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }



}
