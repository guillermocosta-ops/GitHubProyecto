package InterfazGrafica;

import Logica.Admin;
import Logica.Admins;
import Logica.InformeGeneral;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/** Informes generales de administración sobre datos existentes, sin alterar la BD. */
public class generarInformeAdmin extends javax.swing.JFrame {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> ordenador;
    private InformeGeneral informeActual;

    public generarInformeAdmin() {
        initComponents();
        setTitle("SGA - Informes generales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(850, 460));
        setSize(1150, 620);
        setLocationRelativeTo(null);
        prepararVista();
        generarInforme();
    }

    // Los componentes siguientes están definidos en generarInformeAdmin.form.
    // Se pueden mover y editar en la pestaña Design de NetBeans.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        panelSuperior = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        barraSeleccion = new javax.swing.JPanel();
        etiquetaTipo = new javax.swing.JLabel();
        comboTipo = new javax.swing.JComboBox<>();
        botonGenerar = new javax.swing.JButton();
        panelBusqueda = new javax.swing.JPanel();
        etiquetaBuscar = new javax.swing.JLabel();
        campoBuscar = new javax.swing.JTextField();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();
        panelBotones = new javax.swing.JPanel();
        botonExportar = new javax.swing.JButton();
        botonImprimir = new javax.swing.JButton();
        botonVolver = new javax.swing.JButton();
        etiquetaEstado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        panelPrincipal.setLayout(new java.awt.BorderLayout(0, 12));
        panelSuperior.setLayout(new java.awt.BorderLayout(0, 7));
        titulo.setFont(new java.awt.Font("Dialog", 1, 18));
        titulo.setText("Informes generales de administración");
        panelSuperior.add(titulo, java.awt.BorderLayout.NORTH);
        barraSeleccion.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 4));
        etiquetaTipo.setText("Tipo de informe:");
        barraSeleccion.add(etiquetaTipo);
        comboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
            "Estudiantes", "Docentes", "Asignaturas", "Inscripciones", "Calificaciones", "Informes académicos"
        }));
        barraSeleccion.add(comboTipo);
        botonGenerar.setText("Generar / actualizar");
        barraSeleccion.add(botonGenerar);
        panelSuperior.add(barraSeleccion, java.awt.BorderLayout.CENTER);
        panelBusqueda.setLayout(new java.awt.BorderLayout(10, 0));
        etiquetaBuscar.setText("Buscar en el informe:");
        panelBusqueda.add(etiquetaBuscar, java.awt.BorderLayout.WEST);
        panelBusqueda.add(campoBuscar, java.awt.BorderLayout.CENTER);
        panelSuperior.add(panelBusqueda, java.awt.BorderLayout.SOUTH);
        panelPrincipal.add(panelSuperior, java.awt.BorderLayout.NORTH);

        tabla.setModel(new javax.swing.table.DefaultTableModel());
        scrollTabla.setViewportView(tabla);
        panelPrincipal.add(scrollTabla, java.awt.BorderLayout.CENTER);

        panelInferior.setLayout(new java.awt.BorderLayout(0, 8));
        panelBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 3));
        botonExportar.setText("Exportar CSV");
        panelBotones.add(botonExportar);
        botonImprimir.setText("Imprimir");
        panelBotones.add(botonImprimir);
        botonVolver.setText("Volver al menú");
        panelBotones.add(botonVolver);
        panelInferior.add(panelBotones, java.awt.BorderLayout.NORTH);
        etiquetaEstado.setText(" ");
        panelInferior.add(etiquetaEstado, java.awt.BorderLayout.SOUTH);
        panelPrincipal.add(panelInferior, java.awt.BorderLayout.SOUTH);
        getContentPane().add(panelPrincipal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prepararVista() {
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        comboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(
                InformeGeneral.TIPOS.toArray(new String[0])));
        tabla.setRowHeight(27);
        tabla.setFont(new Font("Dialog", Font.PLAIN, 12));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        botonGenerar.addActionListener(evt -> generarInforme());
        comboTipo.addActionListener(evt -> generarInforme());
        botonExportar.addActionListener(evt -> exportarCsv());
        botonImprimir.addActionListener(evt -> imprimir());
        botonVolver.addActionListener(evt -> {
            new interfazAdmin().setVisible(true);
            dispose();
        });
        campoBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent evt) { aplicarFiltro(); }
            @Override public void removeUpdate(DocumentEvent evt) { aplicarFiltro(); }
            @Override public void changedUpdate(DocumentEvent evt) { aplicarFiltro(); }
        });
    }

    private void generarInforme() {
        try {
            // La entidad Admin comprueba que haya una sesión con rol administrador.
            Admin administrador = Admins.getInstancia().obtenerSesionActual();
            String tipo = (String) comboTipo.getSelectedItem();
            if (tipo == null) return;
            InformeGeneral nuevo = administrador.generarInformeGeneral(tipo);
            DefaultTableModel nuevoModelo = new DefaultTableModel(
                    nuevo.getColumnas().toArray(), 0) {
                @Override public boolean isCellEditable(int fila, int columna) { return false; }
            };
            for (List<String> fila : nuevo.getFilas()) {
                nuevoModelo.addRow(fila.toArray(new Object[0]));
            }
            modelo = nuevoModelo;
            informeActual = nuevo;
            tabla.setModel(modelo);
            ordenador = new TableRowSorter<>(modelo);
            tabla.setRowSorter(ordenador);
            ajustarAnchos();
            aplicarFiltro();
        } catch (RuntimeException ex) {
            informeActual = null;
            botonExportar.setEnabled(false);
            botonImprimir.setEnabled(false);
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo generar el informe", JOptionPane.ERROR_MESSAGE);
            etiquetaEstado.setText("No se pudo generar el informe");
        }
    }

    private void ajustarAnchos() {
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            String nombre = tabla.getColumnName(i).toLowerCase();
            int ancho = nombre.contains("contenido") || nombre.contains("comentario") ? 350
                    : nombre.contains("asignatura") || nombre.contains("nombre")
                    || nombre.contains("estudiante") || nombre.contains("docente") ? 210 : 125;
            tabla.getColumnModel().getColumn(i).setPreferredWidth(ancho);
        }
    }

    private void aplicarFiltro() {
        if (ordenador == null || informeActual == null) return;
        String texto = campoBuscar.getText().trim();
        ordenador.setRowFilter(texto.isEmpty() ? null
                : RowFilter.regexFilter("(?iu)" + Pattern.quote(texto)));
        int visibles = tabla.getRowCount();
        etiquetaEstado.setText("Informe: " + informeActual.getTipo()
                + " | Registros: " + visibles + " de " + modelo.getRowCount()
                + " | Generado: " + informeActual.getGeneradoEn().format(FORMATO_FECHA)
                + " | Datos en memoria; no se modifica la base");
        botonExportar.setEnabled(visibles > 0);
        botonImprimir.setEnabled(visibles > 0);
    }

    private void exportarCsv() {
        if (informeActual == null || tabla.getRowCount() == 0) return;
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar informe CSV");
        selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        String nombre = informeActual.getTipo().toLowerCase()
                .replace(' ', '_').replace('á', 'a').replace('ó', 'o')
                .replace('é', 'e').replace('í', 'i').replace('ú', 'u');
        selector.setSelectedFile(new java.io.File("informe_" + nombre + ".csv"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        Path destino = selector.getSelectedFile().toPath();
        if (!destino.getFileName().toString().toLowerCase().endsWith(".csv")) {
            destino = destino.resolveSibling(destino.getFileName() + ".csv");
        }
        if (Files.exists(destino)) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "El archivo ya existe. ¿Querés reemplazarlo?", "Confirmar",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmacion != JOptionPane.YES_OPTION) return;
        }
        try (BufferedWriter escritor = Files.newBufferedWriter(destino,
                StandardCharsets.UTF_8)) {
            escritor.write('\ufeff'); // Excel detecta UTF-8 al abrir el CSV.
            for (int col = 0; col < tabla.getColumnCount(); col++) {
                if (col > 0) escritor.write(';');
                escritor.write(celdaCsv(tabla.getColumnName(col)));
            }
            escritor.newLine();
            // Exporta únicamente las filas actualmente visibles y en su orden.
            for (int fila = 0; fila < tabla.getRowCount(); fila++) {
                for (int col = 0; col < tabla.getColumnCount(); col++) {
                    if (col > 0) escritor.write(';');
                    Object dato = tabla.getValueAt(fila, col);
                    escritor.write(celdaCsv(dato == null ? "" : dato.toString()));
                }
                escritor.newLine();
            }
            JOptionPane.showMessageDialog(this,
                    "Informe exportado correctamente:\n" + destino,
                    "Exportación completada", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo escribir el CSV:\n" + ex.getMessage(),
                    "Error de exportación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** CSV con punto y coma; evita interpretar celdas de usuarios como fórmulas. */
    private static String celdaCsv(String valor) {
        String texto = valor == null ? "" : valor;
        String sinEspacios = texto.stripLeading();
        if (!sinEspacios.isEmpty() && "=+-@".indexOf(sinEspacios.charAt(0)) >= 0) {
            texto = "'" + texto;
        }
        return "\"" + texto.replace("\"", "\"\"") + "\"";
    }

    private void imprimir() {
        if (informeActual == null || tabla.getRowCount() == 0) return;
        try {
            MessageFormat cabecera = new MessageFormat("SGA - " + informeActual.getTipo());
            MessageFormat pie = new MessageFormat("Página {0}");
            tabla.print(javax.swing.JTable.PrintMode.FIT_WIDTH, cabecera, pie,
                    true, null, true, null);
        } catch (java.awt.print.PrinterException | java.awt.HeadlessException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo imprimir: " + ex.getMessage(),
                    "Error de impresión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new generarInformeAdmin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JLabel titulo;
    private javax.swing.JPanel barraSeleccion;
    private javax.swing.JLabel etiquetaTipo;
    private javax.swing.JComboBox<String> comboTipo;
    private javax.swing.JButton botonGenerar;
    private javax.swing.JPanel panelBusqueda;
    private javax.swing.JLabel etiquetaBuscar;
    private javax.swing.JTextField campoBuscar;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JButton botonExportar;
    private javax.swing.JButton botonImprimir;
    private javax.swing.JButton botonVolver;
    private javax.swing.JLabel etiquetaEstado;
    // End of variables declaration//GEN-END:variables
}
