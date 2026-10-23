package Logica;
public class Login {
    private static String cedulaActual;
    public boolean validarLogin(String cedula, String contrasena) {
        CargaInicial.exigirCarga();
        cedulaActual=null;
        if (cedula==null || contrasena==null || cedula.isBlank() || contrasena.isEmpty()) return false;
        for (Usuario u : Usuarios.getInstancia().getListaUsuarios()) {
            if (u.getCi().equals(cedula.trim()) && contrasena.equals(u.getContrasena())) {
                cedulaActual = u.getCi(); return true;
            }
        }
        return false;
    }
    public String obtenerRol(String cedula) {
        CargaInicial.exigirCarga();
        if(cedula==null) return "";
        for(Admin a:Admins.getInstancia().getListaAdmins()) if(a.getCi().equals(cedula)) return "admin";
        for(Docente d:Docentes.getInstancia().getListaDocentes()) if(d.getCi().equals(cedula)) return "docente";
        for(Estudiante e:Estudiantes.getInstancia().getListaEstudiantes()) if(e.getCi().equals(cedula)) return "estudiante";
        return "";
    }
    public static String getCedulaActual() { return cedulaActual; }
}
