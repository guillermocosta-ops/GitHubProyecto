package Logica;
import java.time.LocalDateTime;
public class InformeAcademico {
    private final int idInforme;
    private final String cedulaEstudiante;
    private final String cedulaDocente;
    private final int idAsignatura;
    private final String contenido;
    private final LocalDateTime fecha;
    public InformeAcademico(int idInforme, String cedulaEstudiante, String cedulaDocente,
                             int idAsignatura, String contenido, LocalDateTime fecha) {
        this.idInforme=idInforme; this.cedulaEstudiante=cedulaEstudiante;
        this.cedulaDocente=cedulaDocente; this.idAsignatura=idAsignatura;
        this.contenido=contenido; this.fecha=fecha;
    }
    public int getIdInforme(){return idInforme;}
    public String getCedulaEstudiante(){return cedulaEstudiante;}
    public String getCedulaDocente(){return cedulaDocente;}
    public int getIdAsignatura(){return idAsignatura;}
    public String getContenido(){return contenido;}
    public LocalDateTime getFecha(){return fecha;}
}
