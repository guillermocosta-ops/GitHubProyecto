package InterfazGrafica;
import Logica.Docentes;

import Logica.Asignatura;
import Logica.Admin;
import Logica.Admins;
import Logica.Docente;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 * Gestionar docentes. Se conserva el .form vacío de NetBeans;
 * la pantalla se construye fuera de initComponents().
 * Las consultas de listado se hacen sobre colecciones de Logica.
 */
public class gestionarDocente extends javax.swing.JFrame {

    private Admin control() {
        return Admins.getInstancia().obtenerSesionActual();
    }


    private DefaultTableModel modelo;











    private String cedulaSeleccionada;

    public gestionarDocente() {
        initComponents();
        setTitle("SGA - Gestionar docentes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configurarComponentes();
        setSize(980, 530);
        setMinimumSize(new Dimension(790, 430));
        setLocationRelativeTo(null);
        actualizarTabla();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        principal = new javax.swing.JPanel();
        encabezado = new javax.swing.JPanel();
        etiquetaTitulo = new javax.swing.JLabel();
        buscadorPanel = new javax.swing.JPanel();
        etiquetaBuscar = new javax.swing.JLabel();
        filtro = new javax.swing.JTextField();
        dividido = new javax.swing.JSplitPane();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        formulario = new javax.swing.JPanel();
        etiqueta1 = new javax.swing.JLabel();
        cedula = new javax.swing.JTextField();
        etiqueta2 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        etiqueta3 = new javax.swing.JLabel();
        contrasena = new javax.swing.JPasswordField();
        etiqueta4 = new javax.swing.JLabel();
        scrollAsignaturas = new javax.swing.JScrollPane();
        asignaturas = new javax.swing.JTextArea();
        ayuda1 = new javax.swing.JLabel();
        relleno1 = new javax.swing.JLabel();
        ayuda2 = new javax.swing.JLabel();
        relleno2 = new javax.swing.JLabel();
        pie = new javax.swing.JPanel();
        botonesPanel = new javax.swing.JPanel();
        nuevo = new javax.swing.JButton();
        agregar = new javax.swing.JButton();
        modificar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        atras = new javax.swing.JButton();
        estado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        principal.setLayout(new java.awt.BorderLayout(8, 8));
        encabezado.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaTitulo.setText("Gestionar docentes");
        buscadorPanel.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaBuscar.setText("Buscar por nombre o cédula:");
        formulario.setLayout(new java.awt.GridLayout(6, 2, 7, 7));
        etiqueta1.setText("Cédula");
        etiqueta2.setText("Nombre completo");
        etiqueta3.setText("Contraseña");
        etiqueta4.setText("Asignaturas que dicta");
        asignaturas.setRows(4);
        asignaturas.setColumns(20);
        ayuda1.setText("Al modificar: contraseña vacía = conservar la anterior");
        relleno1.setText(" ");
        ayuda2.setText("Las asignaturas se administran en Gestión de asignaturas");
        relleno2.setText(" ");
        pie.setLayout(new java.awt.BorderLayout(8, 8));
        botonesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        nuevo.setText("Nuevo / Limpiar");
        agregar.setText("Agregar");
        modificar.setText("Modificar");
        eliminar.setText("Eliminar");
        atras.setText("Atrás");
        estado.setText(" ");
        principal.add(encabezado, java.awt.BorderLayout.NORTH);
        principal.add(dividido, java.awt.BorderLayout.CENTER);
        principal.add(pie, java.awt.BorderLayout.SOUTH);
        encabezado.add(etiquetaTitulo, java.awt.BorderLayout.NORTH);
        encabezado.add(buscadorPanel, java.awt.BorderLayout.SOUTH);
        buscadorPanel.add(etiquetaBuscar, java.awt.BorderLayout.WEST);
        buscadorPanel.add(filtro, java.awt.BorderLayout.CENTER);
        dividido.setLeftComponent(scrollTabla);
        dividido.setRightComponent(formulario);
        scrollTabla.setViewportView(tabla);
        formulario.add(etiqueta1);
        formulario.add(cedula);
        formulario.add(etiqueta2);
        formulario.add(nombre);
        formulario.add(etiqueta3);
        formulario.add(contrasena);
        formulario.add(etiqueta4);
        formulario.add(scrollAsignaturas);
        formulario.add(ayuda1);
        formulario.add(relleno1);
        formulario.add(ayuda2);
        formulario.add(relleno2);
        scrollAsignaturas.setViewportView(asignaturas);
        pie.add(botonesPanel, java.awt.BorderLayout.NORTH);
        pie.add(estado, java.awt.BorderLayout.SOUTH);
        botonesPanel.add(nuevo);
        botonesPanel.add(agregar);
        botonesPanel.add(modificar);
        botonesPanel.add(eliminar);
        botonesPanel.add(atras);
        getContentPane().add(principal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void configurarComponentes() {

        modelo = new DefaultTableModel(new String[]{"Cédula", "Nombre", "Asignaturas"}, 0) {
            @Override public boolean isCellEditable(int f, int c) { return false; }
        };
        tabla.setModel(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        asignaturas.setRows(4);
        asignaturas.setColumns(19);
        asignaturas.setLineWrap(true);
        asignaturas.setWrapStyleWord(true);
        asignaturas.setEditable(false);
        asignaturas.setBackground(formulario.getBackground());
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Docentes registrados"));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del docente"));
        principal.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        dividido.setResizeWeight(0.54);
        dividido.setDividerLocation(520);
        nuevo.addActionListener(e -> limpiar());
        agregar.addActionListener(e -> agregar());
        modificar.addActionListener(e -> modificar());
        eliminar.addActionListener(e -> eliminar());
        atras.addActionListener(e -> volver());
        filtro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarTabla(); }
            public void removeUpdate(DocumentEvent e) { actualizarTabla(); }
            public void changedUpdate(DocumentEvent e) { actualizarTabla(); }
        });

    }

    private void actualizarTabla() {
        if (modelo == null || filtro == null) return;
        try {
            List<Docente> docentes = control().obtenerDocentes();
            String texto = filtro.getText().trim().toLowerCase();
            modelo.setRowCount(0);
            for (Docente docente : docentes) {
                if (docente.getCi().toLowerCase().contains(texto)
                        || docente.getNombre().toLowerCase().contains(texto)) {
                    String nombres = control().obtenerAsignaturasDocente(docente.getCi())
                            .stream().map(Asignatura::getNombre)
                            .collect(Collectors.joining(", "));
                    modelo.addRow(new Object[]{
                            docente.getCi(), docente.getNombre(),
                            nombres.isBlank() ? "Sin asignaturas" : nombres
                    });
                }
            }
            estado.setText("Docentes visibles: " + modelo.getRowCount());
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        int indice = tabla.convertRowIndexToModel(fila);
        String ci = modelo.getValueAt(indice, 0).toString();
        Docente docente = control().buscarDocente(ci);
        if (docente == null) return;
        cedulaSeleccionada = ci;
        cedula.setText(docente.getCi());
        cedula.setEditable(false);
        nombre.setText(docente.getNombre());
        contrasena.setText("");
        String nombres = control().obtenerAsignaturasDocente(ci).stream()
                .map(Asignatura::getNombre).collect(Collectors.joining("\n"));
        asignaturas.setText(nombres.isBlank() ? "Sin asignaturas asignadas" : nombres);
    }

    private void limpiar() {
        cedulaSeleccionada = null;
        tabla.clearSelection();
        cedula.setEditable(true);
        cedula.setText("");
        nombre.setText("");
        contrasena.setText("");
        asignaturas.setText("");
        cedula.requestFocusInWindow();
    }

    private void agregar() {
        if (cedulaSeleccionada != null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccionaste un docente. Presioná Nuevo / Limpiar para agregar otro.");
            return;
        }
        String ci = cedula.getText().trim();
        String nom = nombre.getText().trim();
        String clave = new String(contrasena.getPassword());
        ejecutar("Agregando docente...", () -> control().agregarDocente(ci, nom, clave), null);
    }

    private void modificar() {
        if (cedulaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccioná un docente de la tabla");
            return;
        }
        String ci = cedulaSeleccionada;
        String nom = nombre.getText().trim();
        String clave = new String(contrasena.getPassword());
        ejecutar("Modificando docente...", () -> control().modificarDocente(ci, nom, clave), ci);
    }

    private void eliminar() {
        if (cedulaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccioná un docente de la tabla");
            return;
        }
        String ci = cedulaSeleccionada;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al docente con cédula " + ci + "?\n"
                + "No se permite borrar docentes con asignaturas asignadas o informes.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) return;
        ejecutar("Eliminando docente...", () -> control().eliminarDocente(ci), null);
    }

    private void ejecutar(String mensaje, Runnable operacion, String seleccionarDespues) {
        bloquear(true);
        estado.setText(mensaje);
        SwingWorker<Void, Void> tarea = new SwingWorker<>() {
            @Override protected Void doInBackground() {
                operacion.run();
                return null;
            }

            @Override protected void done() {
                bloquear(false);
                try {
                    get();
                    filtro.setText("");
                    actualizarTabla();
                    limpiar();
                    if (seleccionarDespues != null) seleccionarFila(seleccionarDespues);
                    estado.setText("Operación realizada correctamente");
                    JOptionPane.showMessageDialog(gestionarDocente.this,
                            "Operación realizada correctamente");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    mostrarError(ex);
                } catch (ExecutionException ex) {
                    mostrarError(ex.getCause() == null ? ex : ex.getCause());
                }
            }
        };
        tarea.execute();
    }

    private void seleccionarFila(String ci) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (ci.equals(modelo.getValueAt(i, 0))) {
                int filaVista = tabla.convertRowIndexToView(i);
                if (filaVista >= 0) tabla.setRowSelectionInterval(filaVista, filaVista);
                return;
            }
        }
    }

    private void bloquear(boolean trabajando) {
        nuevo.setEnabled(!trabajando);
        agregar.setEnabled(!trabajando);
        modificar.setEnabled(!trabajando);
        eliminar.setEnabled(!trabajando);
        atras.setEnabled(!trabajando);
        tabla.setEnabled(!trabajando);
    }

    private void mostrarError(Throwable ex) {
        estado.setText("Ocurrió un error");
        JOptionPane.showMessageDialog(this,
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void volver() {
        new interfazAdmin().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new gestionarDocente().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregar;
    private javax.swing.JTextArea asignaturas;
    private javax.swing.JButton atras;
    private javax.swing.JLabel ayuda1;
    private javax.swing.JLabel ayuda2;
    private javax.swing.JPanel botonesPanel;
    private javax.swing.JPanel buscadorPanel;
    private javax.swing.JTextField cedula;
    private javax.swing.JPasswordField contrasena;
    private javax.swing.JSplitPane dividido;
    private javax.swing.JButton eliminar;
    private javax.swing.JPanel encabezado;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiqueta1;
    private javax.swing.JLabel etiqueta2;
    private javax.swing.JLabel etiqueta3;
    private javax.swing.JLabel etiqueta4;
    private javax.swing.JLabel etiquetaBuscar;
    private javax.swing.JLabel etiquetaTitulo;
    private javax.swing.JTextField filtro;
    private javax.swing.JPanel formulario;
    private javax.swing.JButton modificar;
    private javax.swing.JTextField nombre;
    private javax.swing.JButton nuevo;
    private javax.swing.JPanel pie;
    private javax.swing.JPanel principal;
    private javax.swing.JLabel relleno1;
    private javax.swing.JLabel relleno2;
    private javax.swing.JScrollPane scrollAsignaturas;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables

}
