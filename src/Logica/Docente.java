
package Logica;

public class Docente  extends Usuario {
   private String materia;

    public Docente(String ci, String nombre, String contrasena,String materia) {
        super(ci, nombre, contrasena);
        this.materia=materia;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }
   
}
