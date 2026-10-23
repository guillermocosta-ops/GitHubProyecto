package InterfazGrafica;

import Logica.Asignatura;
import Logica.Estudiante;
import Logica.Estudiantes;
import Logica.Inscripcion;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/** Promedio simple de evaluaciones por materia, en modo solo lectura. */
public class verPromediosEstudiante extends JDialog {

    private Estudiante control() {
        return Estudiantes.getInstancia().obtenerSesionActual();
    }

    public verPromediosEstudiante(Frame propietario) {
        super(propietario, "Mis promedios", true);
        initComponents();
        configurarComponentes();
        cargarPromedios();
        setLocationRelativeTo(propietario);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        principal = new javax.swing.JPanel();
        superior = new javax.swing.JPanel();
        etiquetaTitulo = new javax.swing.JLabel();
        etiquetaAlumno = new javax.swing.JLabel();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        inferior = new javax.swing.JPanel();
        info = new javax.swing.JPanel();
        estado = new javax.swing.JLabel();
        etiquetaAyuda = new javax.swing.JLabel();
        derecha = new javax.swing.JPanel();
        cerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        principal.setLayout(new java.awt.BorderLayout(8, 8));
        superior.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaTitulo.setText("Promedios por asignatura");
        etiquetaAlumno.setText(" ");
        inferior.setLayout(new java.awt.BorderLayout(8, 8));
        info.setLayout(new java.awt.BorderLayout(8, 8));
        estado.setText(" ");
        etiquetaAyuda.setText("Promedio simple de las evaluaciones; no es la nota final de cursa.");
        derecha.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        cerrar.setText("Cerrar");
        principal.add(superior, java.awt.BorderLayout.NORTH);
        principal.add(scrollTabla, java.awt.BorderLayout.CENTER);
        principal.add(inferior, java.awt.BorderLayout.SOUTH);
        superior.add(etiquetaTitulo, java.awt.BorderLayout.NORTH);
        superior.add(etiquetaAlumno, java.awt.BorderLayout.SOUTH);
        scrollTabla.setViewportView(tabla);
        inferior.add(info, java.awt.BorderLayout.CENTER);
        inferior.add(derecha, java.awt.BorderLayout.EAST);
        info.add(estado, java.awt.BorderLayout.NORTH);
        info.add(etiquetaAyuda, java.awt.BorderLayout.SOUTH);
        derecha.add(cerrar);
        getContentPane().add(principal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void configurarComponentes() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760, 430);
        setMinimumSize(new java.awt.Dimension(610,330));
        superior.setBorder(new EmptyBorder(14,16,4,16));
        scrollTabla.setBorder(new EmptyBorder(5,16,5,16));
        inferior.setBorder(new EmptyBorder(4,16,12,16));
        etiquetaTitulo.setFont(etiquetaTitulo.getFont().deriveFont(Font.BOLD,19f));
        etiquetaAlumno.setText(control().obtenerMiNombre() + "  |  Cédula: " + control().obtenerMiCedula());
        tabla.setRowHeight(27);
        tabla.setAutoCreateRowSorter(true);
        
        cerrar.addActionListener(e -> dispose());

    }

    private void cargarPromedios() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Asignatura", "Estado", "Evaluaciones", "Promedio"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        List<Inscripcion> inscripciones = control().obtenerMisInscripciones();
        for (Inscripcion inscripcion : inscripciones) {
            int idAsignatura = inscripcion.getIdAsignatura();
            Asignatura asignatura = control().obtenerAsignatura(idAsignatura);
            int cantidad = control().contarMisEvaluaciones(idAsignatura);
            Double promedio = control().calcularMiPromedio(idAsignatura);

            modelo.addRow(new Object[]{
                asignatura.getNombre(),
                inscripcion.getEstado(),
                cantidad,
                promedio == null
                        ? "Sin notas"
                        : String.format(Locale.forLanguageTag("es-UY"), "%.2f", promedio)
            });
        }

        tabla.setModel(modelo);
        estado.setText(inscripciones.isEmpty()
                ? "Todavía no tenés inscripciones registradas."
                : "Asignaturas inscriptas: " + inscripciones.size());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cerrar;
    private javax.swing.JPanel derecha;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaAlumno;
    private javax.swing.JLabel etiquetaAyuda;
    private javax.swing.JLabel etiquetaTitulo;
    private javax.swing.JPanel inferior;
    private javax.swing.JPanel info;
    private javax.swing.JPanel principal;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JPanel superior;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables

}
