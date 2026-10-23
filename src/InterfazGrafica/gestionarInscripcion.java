package InterfazGrafica;

import Logica.Asignatura;
import Logica.Admin;
import Logica.Admins;
import Logica.Estudiante;
import Logica.Inscripcion;
import Logica.CargaInicial;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 * Pantalla de inscripción: consulta datos desde las colecciones de Logica.
 * El único acceso a MariaDB se realiza a través de Admin y Persistencia.
 */
public class gestionarInscripcion extends javax.swing.JFrame {

    private Admin control() {
        return Admins.getInstancia().obtenerSesionActual();
    }

    private DefaultTableModel modelo;



    private boolean trabajando = false;

    public gestionarInscripcion() {
        initComponents();
        setTitle("SGA - Gestionar inscripciones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configurarComponentes();
        setSize(1050, 590);
        setMinimumSize(new Dimension(800, 450));
        setLocationRelativeTo(null);
        cargarTabla();
    }

    // Bloque autogenerado original del archivo .form. No modificar a mano.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        principal = new javax.swing.JPanel();
        cabecera = new javax.swing.JPanel();
        etiquetaTitulo = new javax.swing.JLabel();
        buscadorPanel = new javax.swing.JPanel();
        etiquetaBuscar = new javax.swing.JLabel();
        filtro = new javax.swing.JTextField();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        inferior = new javax.swing.JPanel();
        botonesPanel = new javax.swing.JPanel();
        inscribir = new javax.swing.JButton();
        desinscribir = new javax.swing.JButton();
        actualizar = new javax.swing.JButton();
        atras = new javax.swing.JButton();
        estado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        principal.setLayout(new java.awt.BorderLayout(8, 8));
        cabecera.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaTitulo.setText("Gestionar inscripciones");
        buscadorPanel.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaBuscar.setText("Buscar por cédula, estudiante o asignatura:");
        inferior.setLayout(new java.awt.BorderLayout(8, 8));
        botonesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        inscribir.setText("Inscribir estudiante");
        desinscribir.setText("Quitar inscripción seleccionada");
        actualizar.setText("Actualizar tabla");
        atras.setText("Atrás");
        estado.setText(" ");
        principal.add(cabecera, java.awt.BorderLayout.NORTH);
        principal.add(scrollTabla, java.awt.BorderLayout.CENTER);
        principal.add(inferior, java.awt.BorderLayout.SOUTH);
        cabecera.add(etiquetaTitulo, java.awt.BorderLayout.NORTH);
        cabecera.add(buscadorPanel, java.awt.BorderLayout.SOUTH);
        buscadorPanel.add(etiquetaBuscar, java.awt.BorderLayout.WEST);
        buscadorPanel.add(filtro, java.awt.BorderLayout.CENTER);
        scrollTabla.setViewportView(tabla);
        inferior.add(botonesPanel, java.awt.BorderLayout.NORTH);
        inferior.add(estado, java.awt.BorderLayout.SOUTH);
        botonesPanel.add(inscribir);
        botonesPanel.add(desinscribir);
        botonesPanel.add(actualizar);
        botonesPanel.add(atras);
        getContentPane().add(principal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void configurarComponentes() {

        modelo = new DefaultTableModel(new Object[]{"Cédula", "Estudiante", "ID asignatura", "Asignatura", "Estado", "Nota final"}, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla.setModel(modelo);
        tabla.setRowHeight(27);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        principal.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        inscribir.addActionListener(e -> solicitarInscripcion());
        desinscribir.addActionListener(e -> quitarInscripcionSeleccionada());
        actualizar.addActionListener(e -> cargarTabla());
        atras.addActionListener(e -> volver());
        filtro.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { cargarTabla(); }
            @Override public void removeUpdate(DocumentEvent e) { cargarTabla(); }
            @Override public void changedUpdate(DocumentEvent e) { cargarTabla(); }
        });

    }

    private void cargarTabla() {
        if (trabajando) return;
        String buscar = filtro.getText().trim().toLowerCase();
        modelo.setRowCount(0);
        try {
            List<Inscripcion> inscripciones = control().obtenerInscripciones();
            for (Inscripcion ins : inscripciones) {
                Estudiante estudiante = control().buscarEstudiante(
                        ins.getCedulaEstudiante());
                Asignatura asignatura = control().buscarAsignatura(
                        ins.getIdAsignatura());
                String nombreEst = estudiante == null ? "(Sin estudiante)"
                        : estudiante.getNombre();
                String nombreAsig = asignatura == null ? "(Sin asignatura)"
                        : asignatura.getNombre();
                String todo = (ins.getCedulaEstudiante() + " " + nombreEst
                        + " " + ins.getIdAsignatura() + " " + nombreAsig
                        + " " + ins.getEstado()).toLowerCase();
                if (!todo.contains(buscar)) continue;
                modelo.addRow(new Object[]{
                        ins.getCedulaEstudiante(), nombreEst,
                        ins.getIdAsignatura(), nombreAsig,
                        ins.getEstado(),
                        ins.getNota() == null ? "Sin nota final"
                                : ins.getNota()});
            }
            estado.setText("Inscripciones mostradas: " + modelo.getRowCount());
        } catch (RuntimeException ex) {
            estado.setText("No fue posible cargar las inscripciones");
            mostrarError(ex);
        }
    }

    private void solicitarInscripcion() {
        try {
            List<Estudiante> alumnos = control().obtenerEstudiantes();
            if (alumnos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay estudiantes registrados");
                return;
            }

            JComboBox<Estudiante> comboEstudiante =
                    new JComboBox<>(alumnos.toArray(new Estudiante[0]));
            JComboBox<Asignatura> comboAsignatura = new JComboBox<>();
            comboAsignatura.setRenderer((lista, valor, indice,
                    seleccionado, enfocado) -> {
                JLabel etiqueta = (JLabel) new DefaultListCellRenderer()
                        .getListCellRendererComponent(lista, valor,
                                indice, seleccionado, enfocado);
                if (valor != null) {
                    etiqueta.setText(valor.getId() + " - " + valor.getNombre());
                }
                return etiqueta;
            });

            Runnable recargarAsignaturas = () -> {
                comboAsignatura.removeAllItems();
                Estudiante est = (Estudiante) comboEstudiante.getSelectedItem();
                if (est != null) {
                    for (Asignatura a : control().obtenerAsignaturasDisponibles(
                            est.getCi())) {
                        comboAsignatura.addItem(a);
                    }
                }
            };
            comboEstudiante.addActionListener(e -> recargarAsignaturas.run());
            recargarAsignaturas.run();

            JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
            panel.add(new JLabel("Estudiante:"));
            panel.add(comboEstudiante);
            panel.add(new JLabel("Asignatura disponible:"));
            panel.add(comboAsignatura);

            int opcion = JOptionPane.showConfirmDialog(this, panel,
                    "Inscribir estudiante", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (opcion != JOptionPane.OK_OPTION) return;

            Estudiante est = (Estudiante) comboEstudiante.getSelectedItem();
            Asignatura asig = (Asignatura) comboAsignatura.getSelectedItem();
            if (est == null || asig == null) {
                JOptionPane.showMessageDialog(this,
                        "No hay asignaturas disponibles para este estudiante");
                return;
            }
            final String ci = est.getCi();
            final int idAsignatura = asig.getId();
            ejecutarOperacion(() -> control().inscribirEstudiante(ci, idAsignatura),
                    "Inscripción registrada correctamente");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void quitarInscripcionSeleccionada() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná una inscripción en la tabla");
            return;
        }
        int fila = tabla.convertRowIndexToModel(filaVista);
        String cedula = modelo.getValueAt(fila, 0).toString();
        int idAsignatura = ((Number) modelo.getValueAt(fila, 2)).intValue();
        String nombre = modelo.getValueAt(fila, 1).toString();
        String materia = modelo.getValueAt(fila, 3).toString();
        int confirma = JOptionPane.showConfirmDialog(this,
                "¿Quitar la inscripción de " + nombre + " (" + cedula
                + ") a " + materia + "?\n\n"
                + "No se podrá quitar si tiene calificaciones o informes "
                + "asociados a esa materia.",
                "Confirmar desinscripción", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirma != JOptionPane.YES_OPTION) return;
        ejecutarOperacion(() -> control().desinscribirEstudiante(
                cedula, idAsignatura), "Inscripción eliminada correctamente");
    }

    private void ejecutarOperacion(Runnable operacion, String mensajeExito) {
        trabajando = true;
        inscribir.setEnabled(false);
        desinscribir.setEnabled(false);
        actualizar.setEnabled(false);
        estado.setText("Guardando en MariaDB...");
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() {
                operacion.run();
                return null;
            }

            @Override protected void done() {
                trabajando = false;
                if (!CargaInicial.estaCargado()) {
                    inscribir.setEnabled(false);
                    desinscribir.setEnabled(false);
                    actualizar.setEnabled(false);
                } else {
                    inscribir.setEnabled(true);
                    desinscribir.setEnabled(true);
                    actualizar.setEnabled(true);
                }
                try {
                    get();
                    cargarTabla();
                    JOptionPane.showMessageDialog(gestionarInscripcion.this,
                            mensajeExito);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    mostrarError(ex);
                } catch (ExecutionException ex) {
                    mostrarError(ex.getCause() == null ? ex : ex.getCause());
                }
            }
        }.execute();
    }

    private void volver() {
        new interfazAdmin().setVisible(true);
        dispose();
    }

    private void mostrarError(Throwable ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                "No se pudo completar la operación", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() ->
                new gestionarInscripcion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JButton atras;
    private javax.swing.JPanel botonesPanel;
    private javax.swing.JPanel buscadorPanel;
    private javax.swing.JPanel cabecera;
    private javax.swing.JButton desinscribir;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaBuscar;
    private javax.swing.JLabel etiquetaTitulo;
    private javax.swing.JTextField filtro;
    private javax.swing.JPanel inferior;
    private javax.swing.JButton inscribir;
    private javax.swing.JPanel principal;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables

}
