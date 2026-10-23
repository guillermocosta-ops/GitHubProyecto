package InterfazGrafica;
import Logica.Estudiante;

import Logica.Asignatura;
import Logica.Docente;
import Logica.Docentes;
import Logica.InformeAcademico;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/** Consulta de informes ya cargados durante frmSplash.
 * No ejecuta JDBC ni modifica los datos. */
public class verInformes extends JFrame {

    private Docente control() {
        return Docentes.getInstancia().obtenerSesionActual();
    }
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] {"ID", "Fecha", "Cédula", "Estudiante", "Asignatura"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final List<Asignatura> opcionesAsignaturas = new ArrayList<>();
    private List<InformeAcademico> informesMostrados = List.of();
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public verInformes() {
        initComponents();
        configurarComponentes();
        cargarOpcionesAsignaturas();
        cargarInformes();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        principal = new javax.swing.JPanel();
        filtros = new javax.swing.JPanel();
        etiquetaMateria = new javax.swing.JLabel();
        comboAsignaturas = new javax.swing.JComboBox();
        etiquetaCedula = new javax.swing.JLabel();
        campoCedula = new javax.swing.JTextField();
        botonBuscar = new javax.swing.JButton();
        botonLimpiar = new javax.swing.JButton();
        botonVolver = new javax.swing.JButton();
        centro = new javax.swing.JPanel();
        listado = new javax.swing.JPanel();
        etiquetaListado = new javax.swing.JLabel();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        detalle = new javax.swing.JPanel();
        scrollInforme = new javax.swing.JScrollPane();
        areaInforme = new javax.swing.JTextArea();
        etiquetaEstado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        principal.setLayout(new java.awt.BorderLayout(8, 8));
        filtros.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        etiquetaMateria.setText("Asignatura:");
        etiquetaCedula.setText("Cédula del estudiante:");
        botonBuscar.setText("Buscar");
        botonLimpiar.setText("Mostrar todos");
        botonVolver.setText("Atrás");
        centro.setLayout(new java.awt.GridLayout(2, 1, 7, 7));
        listado.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaListado.setText("Informes guardados por el docente:");
        detalle.setLayout(new java.awt.BorderLayout(8, 8));
        areaInforme.setRows(4);
        areaInforme.setColumns(20);
        etiquetaEstado.setText(" ");
        principal.add(filtros, java.awt.BorderLayout.NORTH);
        principal.add(centro, java.awt.BorderLayout.CENTER);
        principal.add(etiquetaEstado, java.awt.BorderLayout.SOUTH);
        filtros.add(etiquetaMateria);
        filtros.add(comboAsignaturas);
        filtros.add(etiquetaCedula);
        filtros.add(campoCedula);
        filtros.add(botonBuscar);
        filtros.add(botonLimpiar);
        filtros.add(botonVolver);
        centro.add(listado);
        centro.add(detalle);
        listado.add(etiquetaListado, java.awt.BorderLayout.NORTH);
        listado.add(scrollTabla, java.awt.BorderLayout.CENTER);
        scrollTabla.setViewportView(tabla);
        detalle.add(scrollInforme, java.awt.BorderLayout.CENTER);
        scrollInforme.setViewportView(areaInforme);
        getContentPane().add(principal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void configurarComponentes() {

        setTitle("SGA - Ver informes académicos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(970, 640);
        setLocationRelativeTo(null);
        principal.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        detalle.setBorder(BorderFactory.createTitledBorder("Contenido del informe"));
        campoCedula.setColumns(15);
        comboAsignaturas.setModel(new javax.swing.DefaultComboBoxModel<>());
        tabla.setModel(modelo);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) mostrarInformeSeleccionado();
        });
        areaInforme.setRows(8);
        areaInforme.setColumns(65);
        areaInforme.setEditable(false);
        areaInforme.setLineWrap(true);
        areaInforme.setWrapStyleWord(true);
        areaInforme.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        areaInforme.setText("Seleccioná un informe para ver su contenido.");
        botonBuscar.addActionListener(e -> cargarInformes());
        campoCedula.addActionListener(e -> cargarInformes());
        comboAsignaturas.addActionListener(e -> cargarInformes());
        botonLimpiar.addActionListener(e -> {
            campoCedula.setText("");
            comboAsignaturas.setSelectedIndex(0);
            cargarInformes();
        });
        botonVolver.addActionListener(e -> {
            new interfazDocente().setVisible(true);
            dispose();
        });

    }

    private void cargarOpcionesAsignaturas() {
        try {
            opcionesAsignaturas.addAll(control().obtenerMisAsignaturas());
            comboAsignaturas.addItem("Todas mis asignaturas");
            for (Asignatura asignatura : opcionesAsignaturas) {
                comboAsignaturas.addItem(asignatura.getNombre());
            }
        } catch (RuntimeException ex) {
            mostrarError("No se pudieron cargar las asignaturas", ex);
        }
    }

    private void cargarInformes() {
        try {
            int opcion = comboAsignaturas.getSelectedIndex();
            Integer idAsignatura = (opcion <= 0)
                    ? null : opcionesAsignaturas.get(opcion - 1).getId();

            informesMostrados = control().buscarMisInformes(
                    campoCedula.getText().trim(), idAsignatura);

            modelo.setRowCount(0);
            for (InformeAcademico informe : informesMostrados) {
                modelo.addRow(new Object[] {
                    informe.getIdInforme(),
                    informe.getFecha().format(FORMATO),
                    informe.getCedulaEstudiante(),
                    control().obtenerNombreEstudiante(informe.getCedulaEstudiante()),
                    control().obtenerNombreAsignatura(informe.getIdAsignatura())
                });
            }
            areaInforme.setText("Seleccioná un informe para ver su contenido.");
            etiquetaEstado.setText("Informes encontrados: "
                    + informesMostrados.size());
        } catch (RuntimeException ex) {
            informesMostrados = List.of();
            modelo.setRowCount(0);
            areaInforme.setText("");
            etiquetaEstado.setText("No se pudieron cargar los informes.");
            mostrarError("Error al consultar informes", ex);
        }
    }

    private void mostrarInformeSeleccionado() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) {
            areaInforme.setText("Seleccioná un informe para ver su contenido.");
            return;
        }
        // El usuario puede ordenar la JTable: convertir a índice del modelo.
        int filaModelo = tabla.convertRowIndexToModel(filaVista);
        if (filaModelo >= informesMostrados.size()) {
            return;
        }
        InformeAcademico informe = informesMostrados.get(filaModelo);
        areaInforme.setText(informe.getContenido());
        areaInforme.setCaretPosition(0);
    }

    private void mostrarError(String contexto, Throwable ex) {
        JOptionPane.showMessageDialog(this,
                contexto + ": " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new verInformes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaInforme;
    private javax.swing.JButton botonBuscar;
    private javax.swing.JButton botonLimpiar;
    private javax.swing.JButton botonVolver;
    private javax.swing.JTextField campoCedula;
    private javax.swing.JPanel centro;
    private javax.swing.JComboBox comboAsignaturas;
    private javax.swing.JPanel detalle;
    private javax.swing.JLabel etiquetaCedula;
    private javax.swing.JLabel etiquetaEstado;
    private javax.swing.JLabel etiquetaListado;
    private javax.swing.JLabel etiquetaMateria;
    private javax.swing.JPanel filtros;
    private javax.swing.JPanel listado;
    private javax.swing.JPanel principal;
    private javax.swing.JScrollPane scrollInforme;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables

}
