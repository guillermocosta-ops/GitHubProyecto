package Logica;
import java.time.LocalDate;
public class Nota {
    private int idNota;
    private int nota;
    private LocalDate fecha;
    private String tipo;
    private String ci;
    private Integer idAsignatura;
    public Nota(int idNota, int nota, LocalDate fecha, String tipo, String ci, int idAsignatura) {
        this.idNota=idNota; this.nota=nota; this.fecha=fecha; this.tipo=tipo;
        this.ci=ci; this.idAsignatura=idAsignatura;
    }
    public Nota(int idNota, int nota, LocalDate fecha, String tipo, String ci) {
        this.idNota=idNota; this.nota=nota; this.fecha=fecha; this.tipo=tipo; this.ci=ci;
    }
    public Nota(int idNota, LocalDate fecha, String tipo, int nota) {
        this.idNota=idNota; this.fecha=fecha; this.tipo=tipo; this.nota=nota;
    }
    public int getidNota(){return idNota;}
    public void setidNota(int idNota){this.idNota=idNota;}
    public int getNota(){return nota;}
    public void setNota(int nota){this.nota=nota;}
    public LocalDate getFecha(){return fecha;}
    public void setFecha(LocalDate fecha){this.fecha=fecha;}
    public String getTipo(){return tipo;}
    public void setTipo(String tipo){this.tipo=tipo;}
    public String getCi(){return ci;}
    public void setCi(String ci){this.ci=ci;}
    public Integer getIdAsignatura(){return idAsignatura;}
    public void setIdAsignatura(Integer idAsignatura){this.idAsignatura=idAsignatura;}
}
