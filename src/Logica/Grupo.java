
package Logica;


public class Grupo {
    private int id;
    private Asignaturas_de_cursos asisg; 

    public Grupo(int id, Asignaturas_de_cursos asisg) {
        this.id = id;
        this.asisg = asisg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Asignaturas_de_cursos getAsisg() {
        return asisg;
    }

    public void setAsisg(Asignaturas_de_cursos asisg) {
        this.asisg = asisg;
    }
    
}
