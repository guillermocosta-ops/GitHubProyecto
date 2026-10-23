package InterfazGrafica;
import Logica.Estudiantes;

import Logica.Admin;
import Logica.Admins;
import Logica.Estudiante;
import Logica.Asignatura;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultListCellRenderer;
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
 * Gestion de estudiantes. El diseño existente .form era vacío:
 * se construye el contenido fuera de initComponents() para no modificar
 * el código protegido de NetBeans.
 */
public class gestionarEstudiante extends JFrame {

    private Admin control() {
        return Admins.getInstancia().obtenerSesionActual();
    }


    private DefaultTableModel modelo;












    private String cedulaSeleccionada;

    public gestionarEstudiante() {
        initComponents();
        setTitle("SGA - Gestionar estudiantes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configurarComponentes();
        setSize(960, 570);
        setMinimumSize(new Dimension(770, 450));
        setLocationRelativeTo(null);
        actualizarTabla();
    }

    // NetBeans puede seguir manteniendo gestionarEstudiante.form sin cambios.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        principal = new javax.swing.JPanel();
        encabezado = new javax.swing.JPanel();
        etiquetaTitulo = new javax.swing.JLabel();
        buscadorPanel = new javax.swing.JPanel();
        etiquetaBuscar = new javax.swing.JLabel();
        buscar = new javax.swing.JTextField();
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
        scrollComentario = new javax.swing.JScrollPane();
        comentario = new javax.swing.JTextArea();
        ayuda1 = new javax.swing.JLabel();
        relleno1 = new javax.swing.JLabel();
        zonaInferior = new javax.swing.JPanel();
        botonesPanel = new javax.swing.JPanel();
        nuevo = new javax.swing.JButton();
        agregar = new javax.swing.JButton();
        modificar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        inscribir = new javax.swing.JButton();
        atras = new javax.swing.JButton();
        estado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        principal.setLayout(new java.awt.BorderLayout(8, 8));
        encabezado.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaTitulo.setText("Gestionar estudiantes");
        buscadorPanel.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaBuscar.setText("Buscar por nombre o cédula:");
        formulario.setLayout(new java.awt.GridLayout(5, 2, 7, 7));
        etiqueta1.setText("Cédula");
        etiqueta2.setText("Nombre completo");
        etiqueta3.setText("Contraseña");
        etiqueta4.setText("Comentario");
        comentario.setRows(4);
        comentario.setColumns(20);
        ayuda1.setText("Al modificar, contraseña vacía = no cambiar");
        relleno1.setText(" ");
        zonaInferior.setLayout(new java.awt.BorderLayout(8, 8));
        botonesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        nuevo.setText("Nuevo / Limpiar");
        agregar.setText("Agregar");
        modificar.setText("Modificar");
        eliminar.setText("Eliminar");
        inscribir.setText("Inscribir en asignatura");
        atras.setText("Atrás");
        estado.setText(" ");
        principal.add(encabezado, java.awt.BorderLayout.NORTH);
        principal.add(dividido, java.awt.BorderLayout.CENTER);
        principal.add(zonaInferior, java.awt.BorderLayout.SOUTH);
        encabezado.add(etiquetaTitulo, java.awt.BorderLayout.NORTH);
        encabezado.add(buscadorPanel, java.awt.BorderLayout.SOUTH);
        buscadorPanel.add(etiquetaBuscar, java.awt.BorderLayout.WEST);
        buscadorPanel.add(buscar, java.awt.BorderLayout.CENTER);
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
        formulario.add(scrollComentario);
        formulario.add(ayuda1);
        formulario.add(relleno1);
        scrollComentario.setViewportView(comentario);
        zonaInferior.add(botonesPanel, java.awt.BorderLayout.NORTH);
        zonaInferior.add(estado, java.awt.BorderLayout.SOUTH);
        botonesPanel.add(nuevo);
        botonesPanel.add(agregar);
        botonesPanel.add(modificar);
        botonesPanel.add(eliminar);
        botonesPanel.add(inscribir);
        botonesPanel.add(atras);
        getContentPane().add(principal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void configurarComponentes() {

        modelo = new DefaultTableModel(new String[]{"Cédula", "Nombre", "Comentario"}, 0) {
            @Override public boolean isCellEditable(int fila, int col) { return false; }
        };
        tabla.setModel(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        comentario.setRows(4);
        comentario.setColumns(22);
        comentario.setLineWrap(true);
        comentario.setWrapStyleWord(true);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Estudiantes cargados"));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del estudiante"));
        principal.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        dividido.setResizeWeight(0.55);
        dividido.setDividerLocation(520);
        nuevo.addActionListener(e -> limpiar());
        agregar.addActionListener(e -> agregar());
        modificar.addActionListener(e -> modificar());
        eliminar.addActionListener(e -> eliminar());
        inscribir.addActionListener(e -> inscribir());
        atras.addActionListener(e -> volver());
        buscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarTabla(); }
            public void removeUpdate(DocumentEvent e) { actualizarTabla(); }
            public void changedUpdate(DocumentEvent e) { actualizarTabla(); }
        });

    }

    private void actualizarTabla() {
        if (modelo == null) return;
        try {
            List<Estudiante> estudiantes = control().obtenerEstudiantes();
            String filtro = buscar.getText().trim().toLowerCase();
            modelo.setRowCount(0);
            for (Estudiante e : estudiantes) {
                if (e.getCi().toLowerCase().contains(filtro)
                        || e.getNombre().toLowerCase().contains(filtro)) {
                    modelo.addRow(new Object[]{e.getCi(), e.getNombre(), e.getComentario()});
                }
            }
            estado.setText("Estudiantes visibles: " + modelo.getRowCount());
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        int modeloFila = tabla.convertRowIndexToModel(fila);
        String ci = modelo.getValueAt(modeloFila, 0).toString();
        Estudiante e = control().buscarEstudiante(ci);
        if (e == null) return;
        cedulaSeleccionada = ci;
        cedula.setText(e.getCi());
        cedula.setEditable(false);
        nombre.setText(e.getNombre());
        contrasena.setText(""); // No mostrar credenciales en pantalla
        comentario.setText(e.getComentario() == null ? "" : e.getComentario());
    }

    private void limpiar() {
        cedulaSeleccionada = null;
        tabla.clearSelection();
        cedula.setEditable(true);
        cedula.setText("");
        nombre.setText("");
        contrasena.setText("");
        comentario.setText("");
        cedula.requestFocusInWindow();
    }

    private void agregar() {
        if (cedulaSeleccionada != null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccionaste un estudiante. Presioná Nuevo / Limpiar para agregar otro.");
            return;
        }
        String ci = cedula.getText().trim();
        String nom = nombre.getText().trim();
        String clave = new String(contrasena.getPassword());
        String com = comentario.getText();
        ejecutar("Agregando estudiante...", () ->
                control().agregarEstudiante(ci, nom, clave, com), null);
    }

    private void modificar() {
        if (cedulaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccioná un estudiante de la tabla");
            return;
        }
        String ci = cedulaSeleccionada;
        String nom = nombre.getText().trim();
        String clave = new String(contrasena.getPassword());
        String com = comentario.getText();
        ejecutar("Modificando estudiante...", () ->
                control().modificarEstudiante(ci, nom, clave, com), ci);
    }

    private void eliminar() {
        if (cedulaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccioná un estudiante de la tabla");
            return;
        }
        String ci = cedulaSeleccionada;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al estudiante con cédula " + ci + "?\n"
                + "No se permite borrar estudiantes con información académica.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) return;
        ejecutar("Eliminando estudiante...", () -> control().eliminarEstudiante(ci), null);
    }

    // Inscribir al estudiante seleccionado en una asignatura existente.
    // Las asignaturas se consultan en memoria, no desde esta ventana a JDBC.
    private void inscribir() {
        if (cedulaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero seleccioná un estudiante de la tabla");
            return;
        }

        final String ci = cedulaSeleccionada;
        final List<Asignatura> disponibles;
        try {
            disponibles = control().obtenerAsignaturasDisponibles(ci);
        } catch (RuntimeException ex) {
            mostrarError(ex);
            return;
        }
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Este estudiante ya está inscripto en todas las asignaturas disponibles.");
            return;
        }

        JComboBox<Asignatura> opciones = new JComboBox<>(
                disponibles.toArray(new Asignatura[0]));
        opciones.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> lista, Object valor, int indice,
                    boolean seleccionado, boolean tieneFoco) {
                super.getListCellRendererComponent(lista, valor, indice,
                        seleccionado, tieneFoco);
                if (valor instanceof Asignatura) {
                    Asignatura a = (Asignatura) valor;
                    setText(a.getNombre() + " (ID " + a.getId() + ")");
                }
                return this;
            }
        });

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(new JLabel("Inscribir a " + nombre.getText()
                + " (" + ci + ") en:"), BorderLayout.NORTH);
        panel.add(opciones, BorderLayout.CENTER);

        int respuesta = JOptionPane.showConfirmDialog(this, panel,
                "Inscribir estudiante en asignatura",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta != JOptionPane.OK_OPTION) return;

        Asignatura seleccionada = (Asignatura) opciones.getSelectedItem();
        if (seleccionada == null) return;
        final int id = seleccionada.getId();
        ejecutar("Inscribiendo estudiante...",
                () -> control().inscribirEstudiante(ci, id), ci);
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
                    buscar.setText("");
                    actualizarTabla();
                    limpiar();
                    if (seleccionarDespues != null) seleccionarFila(seleccionarDespues);
                    estado.setText("Operación realizada correctamente");
                    JOptionPane.showMessageDialog(gestionarEstudiante.this,
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
                tabla.setRowSelectionInterval(filaVista, filaVista);
                return;
            }
        }
    }

    private void bloquear(boolean trabajando) {
        nuevo.setEnabled(!trabajando);
        agregar.setEnabled(!trabajando);
        modificar.setEnabled(!trabajando);
        eliminar.setEnabled(!trabajando);
        inscribir.setEnabled(!trabajando);
        atras.setEnabled(!trabajando);
        tabla.setEnabled(!trabajando);
        buscar.setEnabled(!trabajando);
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
        java.awt.EventQueue.invokeLater(() -> new gestionarEstudiante().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregar;
    private javax.swing.JButton atras;
    private javax.swing.JLabel ayuda1;
    private javax.swing.JPanel botonesPanel;
    private javax.swing.JPanel buscadorPanel;
    private javax.swing.JTextField buscar;
    private javax.swing.JTextField cedula;
    private javax.swing.JTextArea comentario;
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
    private javax.swing.JPanel formulario;
    private javax.swing.JButton inscribir;
    private javax.swing.JButton modificar;
    private javax.swing.JTextField nombre;
    private javax.swing.JButton nuevo;
    private javax.swing.JPanel principal;
    private javax.swing.JLabel relleno1;
    private javax.swing.JScrollPane scrollComentario;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    private javax.swing.JPanel zonaInferior;
    // End of variables declaration//GEN-END:variables

}
