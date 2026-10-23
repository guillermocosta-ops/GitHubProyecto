package InterfazGrafica;

import Logica.Asignatura;
import Logica.Estudiante;
import Logica.Estudiantes;
import Logica.Nota;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/** Ventana de solo lectura. No necesita un archivo .form. */
public class verNotasEstudiante extends JDialog {

    private Estudiante control() {
        return Estudiantes.getInstancia().obtenerSesionActual();
    }

    public verNotasEstudiante(Frame propietario) {
        super(propietario, "Mis calificaciones", true);
        initComponents();
        configurarComponentes();
        cargarNotas();
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
        estado = new javax.swing.JLabel();
        derecha = new javax.swing.JPanel();
        cerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        principal.setLayout(new java.awt.BorderLayout(8, 8));
        superior.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaTitulo.setText("Mis calificaciones");
        etiquetaAlumno.setText(" ");
        inferior.setLayout(new java.awt.BorderLayout(8, 8));
        estado.setText(" ");
        derecha.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        cerrar.setText("Cerrar");
        principal.add(superior, java.awt.BorderLayout.NORTH);
        principal.add(scrollTabla, java.awt.BorderLayout.CENTER);
        principal.add(inferior, java.awt.BorderLayout.SOUTH);
        superior.add(etiquetaTitulo, java.awt.BorderLayout.NORTH);
        superior.add(etiquetaAlumno, java.awt.BorderLayout.SOUTH);
        scrollTabla.setViewportView(tabla);
        inferior.add(estado, java.awt.BorderLayout.CENTER);
        inferior.add(derecha, java.awt.BorderLayout.EAST);
        derecha.add(cerrar);
        getContentPane().add(principal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void configurarComponentes() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760, 440);
        setMinimumSize(new java.awt.Dimension(610,330));
        superior.setBorder(new EmptyBorder(14,16,4,16));
        scrollTabla.setBorder(new EmptyBorder(5,16,5,16));
        inferior.setBorder(new EmptyBorder(4,16,12,16));
        etiquetaTitulo.setFont(etiquetaTitulo.getFont().deriveFont(Font.BOLD,19f));
        etiquetaAlumno.setText(control().obtenerMiNombre() + "  |  Cédula: " + control().obtenerMiCedula());
        tabla.setRowHeight(27);
        tabla.setAutoCreateRowSorter(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cerrar.addActionListener(e -> dispose());

    }

    private void cargarNotas() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Asignatura", "Tipo", "Calificación", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columna) {
                return columna == 2 ? Integer.class : String.class;
            }
        };

        List<Nota> notas = control().obtenerMisNotas();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Nota nota : notas) {
            Integer id = nota.getIdAsignatura();
            String nombre = id == null
                    ? "Sin asignatura"
                    : control().obtenerAsignatura(id).getNombre();

            modelo.addRow(new Object[]{
                nombre,
                nota.getTipo(),
                nota.getNota(),
                nota.getFecha() == null ? "" : nota.getFecha().format(formatoFecha)
            });
        }

        tabla.setModel(modelo);
        estado.setText(notas.isEmpty()
                ? "Todavía no tenés calificaciones registradas."
                : "Total de evaluaciones: " + notas.size());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cerrar;
    private javax.swing.JPanel derecha;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaAlumno;
    private javax.swing.JLabel etiquetaTitulo;
    private javax.swing.JPanel inferior;
    private javax.swing.JPanel principal;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JPanel superior;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables

}
