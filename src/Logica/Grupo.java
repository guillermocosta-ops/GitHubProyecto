package Logica;
import java.util.ArrayList;
import java.util.List;
public class Grupo {
    private int id;
    private String nombre;
    private Asignaturas_de_cursos asisg;
    private List<Asignatura> asignaturas = new ArrayList<>();
    public Grupo(int id, Asignaturas_de_cursos asisg) { this.id=id; this.asisg=asisg; }
    public Grupo(int id, String nombre) { this.id=id; this.nombre=nombre; this.asisg=new Asignaturas_de_cursos(); }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public Asignaturas_de_cursos getAsisg(){return asisg;}
    public void setAsisg(Asignaturas_de_cursos a){this.asisg=a;}
    public List<Asignatura> getAsignaturas(){return List.copyOf(asignaturas);}
    public void agregarAsignatura(Asignatura a){if(!asignaturas.contains(a)) asignaturas.add(a);}
}
