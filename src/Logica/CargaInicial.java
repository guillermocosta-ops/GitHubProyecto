package Logica;
import Persistencia.Persistencia;
import java.util.ArrayList;
import java.util.List;

/** Solamente coordina la carga; los datos viven en las colecciones del UML. */
public final class CargaInicial {
    private static volatile boolean cargado = false;
    private CargaInicial() { }
    public static boolean estaCargado() { return cargado; }
    public static void exigirCarga() {
        if (!cargado) throw new IllegalStateException("La carga inicial del SGA no terminó correctamente");
    }
    public static synchronized void cargarTodo() {
        cargado = false;
        Persistencia p = new Persistencia();
        // Las consultas se completan en colecciones temporales antes de publicarlas.
        List<Usuario> usuarios = p.obtenerTodosLosUsuarios();
        Estudiantes estudiantes = p.obtenerEstudiantes();
        Docentes docentes = p.obtenerDocentes();
        Admins admins = p.obtenerAdmins();
        Asignaturas asignaturas = p.obtenerAsignaturas();
        Notas notas = p.obtenerNotas();
        Inscripciones inscripciones = p.obtenerInscripciones();
        Asignaturas_de_cursos cursos = p.obtenerCursos(docentes, asignaturas,
                inscripciones, notas, estudiantes);
        Grupos grupos = p.obtenerGrupos(asignaturas, cursos);
        Informes informes = p.obtenerInformes();
        // La subclase sustituye al Usuario base para evitar datos duplicados.
        List<Usuario> completos = new ArrayList<>();
        for (Usuario u : usuarios) {
            Usuario elegido = u;
            for (Estudiante e : estudiantes.getListaEstudiantes())
                if (e.getCi().equals(u.getCi())) elegido=e;
            for (Docente d : docentes.getListaDocentes())
                if (d.getCi().equals(u.getCi())) elegido=d;
            for (Admin a : admins.getListaAdmins())
                if (a.getCi().equals(u.getCi())) elegido=a;
            completos.add(elegido);
        }
        // Publicar en las colecciones Singleton que ya existen en Logica.
        Usuarios.getInstancia().reemplazar(completos);
        Estudiantes.getInstancia().reemplazar(estudiantes.getListaEstudiantes());
        Docentes.getInstancia().reemplazar(docentes.getListaDocentes());
        Admins.getInstancia().reemplazar(admins.getListaAdmins());
        Asignaturas.getInstancia().reemplazar(asignaturas.getListaAsignatura());
        Notas.getInstancia().reemplazar(notas.getListaNota());
        Inscripciones.getInstancia().reemplazar(inscripciones.getListaInscripciones());
        Asignaturas_de_cursos.getInstancia().reemplazar(cursos.getListaAsignaturas_de_curso());
        Grupos.getInstancia().reemplazar(grupos.getListaGrupos());
        Informes.getInstancia().reemplazar(informes.getListaInformes());
        cargado = true;
    }
    public static void invalidar() { cargado=false; }
}
