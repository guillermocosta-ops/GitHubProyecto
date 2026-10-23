package Logica;
import java.math.BigDecimal;
public class Inscripcion {
    private final String cedulaEstudiante;
    private final int idAsignatura;
    private final String estado;
    private final BigDecimal nota;
    public Inscripcion(String cedulaEstudiante, int idAsignatura, String estado, BigDecimal nota) {
        this.cedulaEstudiante=cedulaEstudiante; this.idAsignatura=idAsignatura;
        this.estado=estado; this.nota=nota;
    }
    public String getCedulaEstudiante(){return cedulaEstudiante;}
    public int getIdAsignatura(){return idAsignatura;}
    public String getEstado(){return estado;}
    public BigDecimal getNota(){return nota;}
}
