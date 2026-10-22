
package Logica;

public class Estudiante extends Usuario {
    private String comentario;

    public Estudiante(String ci, String nombre, String contrasena,String comentario) {
        super(ci, nombre, contrasena);
        this.comentario=comentario;
    }
    
    public Estudiante(String ci, String nombre){
        super(ci, nombre);
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    @Override
    // Sobrescribe el toString() de Object para que el JComboBox o las listas 
    // muestren "Cédula - Nombre" en la pantalla en lugar del código interno de Java.
    
    public String toString() {
        return getCi() + " - " + getNombre();
}
}
