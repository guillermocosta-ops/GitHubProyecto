
package Logica;

public class Estudiante extends Usuario {
    private String comentario;

    public Estudiante(String ci, String nombre, String contrasena,String comentario) {
        super(ci, nombre, contrasena);
        this.comentario=comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
