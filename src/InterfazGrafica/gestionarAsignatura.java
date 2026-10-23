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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/** Gestión de asignaturas. No ejecuta SQL ni modifica el .form de NetBeans. */
public class gestionarAsignatura extends javax.swing.JFrame {

    private Admin control() {
        return Admins.getInstancia().obtenerSesionActual();
    }

    private DefaultTableModel modelo, modeloDocentes;



    private Integer asignaturaSeleccionada;

    public gestionarAsignatura() {
        initComponents();
        setTitle("SGA - Gestionar asignaturas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configurarComponentes();
        setSize(1070, 650);
        setMinimumSize(new Dimension(850, 540));
        setLocationRelativeTo(null);
        actualizarTabla();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        principal = new javax.swing.JPanel();
        superior = new javax.swing.JPanel();
        encabezado = new javax.swing.JPanel();
        etiquetaTitulo = new javax.swing.JLabel();
        buscadorPanel = new javax.swing.JPanel();
        etiquetaBuscar = new javax.swing.JLabel();
        filtro = new javax.swing.JTextField();
        pie = new javax.swing.JPanel();
        botonesPanel = new javax.swing.JPanel();
        nuevo = new javax.swing.JButton();
        agregar = new javax.swing.JButton();
        modificar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        atras = new javax.swing.JButton();
        estado = new javax.swing.JLabel();
        dividido = new javax.swing.JSplitPane();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        formulario = new javax.swing.JPanel();
        etiqueta1 = new javax.swing.JLabel();
        idActual = new javax.swing.JLabel();
        etiqueta2 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        etiqueta3 = new javax.swing.JLabel();
        creditos = new javax.swing.JTextField();
        etiqueta4 = new javax.swing.JLabel();
        cupo = new javax.swing.JTextField();
        docentesPanel = new javax.swing.JPanel();
        scrollDocentes = new javax.swing.JScrollPane();
        tablaDocentes = new javax.swing.JTable();
        accionesDocentes = new javax.swing.JPanel();
        asignar = new javax.swing.JButton();
        desasignar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        principal.setLayout(new java.awt.BorderLayout(8, 8));
        superior.setLayout(new java.awt.BorderLayout(8, 8));
        encabezado.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaTitulo.setText("Gestionar asignaturas");
        buscadorPanel.setLayout(new java.awt.BorderLayout(8, 8));
        etiquetaBuscar.setText("Buscar por nombre o ID:");
        pie.setLayout(new java.awt.BorderLayout(8, 8));
        botonesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        nuevo.setText("Nuevo / Limpiar");
        agregar.setText("Agregar");
        modificar.setText("Modificar");
        eliminar.setText("Eliminar");
        atras.setText("Atrás");
        estado.setText(" ");
        formulario.setLayout(new java.awt.GridLayout(4, 2, 7, 7));
        etiqueta1.setText("ID:");
        idActual.setText("Automático");
        etiqueta2.setText("Nombre:");
        etiqueta3.setText("Créditos:");
        etiqueta4.setText("Cupo máximo:");
        docentesPanel.setLayout(new java.awt.BorderLayout(8, 8));
        accionesDocentes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        asignar.setText("Asignar docente");
        desasignar.setText("Quitar docente seleccionado");
        principal.add(superior, java.awt.BorderLayout.NORTH);
        principal.add(dividido, java.awt.BorderLayout.CENTER);
        principal.add(docentesPanel, java.awt.BorderLayout.SOUTH);
        superior.add(encabezado, java.awt.BorderLayout.CENTER);
        superior.add(pie, java.awt.BorderLayout.SOUTH);
        encabezado.add(etiquetaTitulo, java.awt.BorderLayout.NORTH);
        encabezado.add(buscadorPanel, java.awt.BorderLayout.SOUTH);
        buscadorPanel.add(etiquetaBuscar, java.awt.BorderLayout.WEST);
        buscadorPanel.add(filtro, java.awt.BorderLayout.CENTER);
        pie.add(botonesPanel, java.awt.BorderLayout.NORTH);
        pie.add(estado, java.awt.BorderLayout.SOUTH);
        botonesPanel.add(nuevo);
        botonesPanel.add(agregar);
        botonesPanel.add(modificar);
        botonesPanel.add(eliminar);
        botonesPanel.add(atras);
        dividido.setLeftComponent(scrollTabla);
        dividido.setRightComponent(formulario);
        scrollTabla.setViewportView(tabla);
        formulario.add(etiqueta1);
        formulario.add(idActual);
        formulario.add(etiqueta2);
        formulario.add(nombre);
        formulario.add(etiqueta3);
        formulario.add(creditos);
        formulario.add(etiqueta4);
        formulario.add(cupo);
        docentesPanel.add(scrollDocentes, java.awt.BorderLayout.CENTER);
        docentesPanel.add(accionesDocentes, java.awt.BorderLayout.SOUTH);
        scrollDocentes.setViewportView(tablaDocentes);
        accionesDocentes.add(asignar);
        accionesDocentes.add(desasignar);
        getContentPane().add(principal, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void configurarComponentes() {

        modelo = new DefaultTableModel(new String[]{"ID", "Asignatura", "Créditos", "Cupo", "Inscriptos", "Docentes"},0) {
            @Override public boolean isCellEditable(int fila,int columna) { return false; }
        };
        tabla.setModel(modelo);
        tabla.setRowHeight(27);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        modeloDocentes = new DefaultTableModel(new String[]{"Cédula", "Nombre"},0) {
            @Override public boolean isCellEditable(int fila,int columna) { return false; }
        };
        tablaDocentes.setModel(modeloDocentes);
        tablaDocentes.setRowHeight(25);
        tablaDocentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        principal.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos de la asignatura"));
        docentesPanel.setBorder(BorderFactory.createTitledBorder("Docentes de la asignatura seleccionada"));
        docentesPanel.setPreferredSize(new Dimension(600,180));
        nombre.setColumns(22);
        creditos.setColumns(8);
        cupo.setColumns(8);
        dividido.setResizeWeight(0.67);
        dividido.setDividerLocation(650);
        nuevo.addActionListener(e -> limpiar());
        agregar.addActionListener(e -> agregar());
        modificar.addActionListener(e -> modificar());
        eliminar.addActionListener(e -> eliminar());
        asignar.addActionListener(e -> asignarDocente());
        desasignar.addActionListener(e -> quitarDocente());
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
            String busqueda = filtro.getText().trim().toLowerCase();
            Integer seleccionado = asignaturaSeleccionada;
            modelo.setRowCount(0);
            for (Asignatura a : control().obtenerAsignaturas()) {
                if (!a.getNombre().toLowerCase().contains(busqueda)
                        && !String.valueOf(a.getId()).contains(busqueda)) continue;
                long inscriptos = Logica.Inscripciones.getInstancia().getListaInscripciones()
                        .stream().filter(i -> i.getIdAsignatura() == a.getId()).count();
                int docentes = control().obtenerDocentesDeAsignatura(a.getId()).size();
                modelo.addRow(new Object[]{a.getId(), a.getNombre(),
                        a.getCantidadCreditos(), a.getCupoMaximo(), inscriptos, docentes});
            }
            if (seleccionado != null) seleccionarFila(seleccionado);
            if (estado != null) estado.setText("Asignaturas visibles: " + modelo.getRowCount());
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        int idx = tabla.convertRowIndexToModel(fila);
        int id = ((Number) modelo.getValueAt(idx, 0)).intValue();
        Asignatura a = control().buscarAsignatura(id);
        if (a == null) return;
        asignaturaSeleccionada = id;
        idActual.setText(String.valueOf(id));
        nombre.setText(a.getNombre());
        creditos.setText(String.valueOf(a.getCantidadCreditos()));
        cupo.setText(String.valueOf(a.getCupoMaximo()));
        actualizarDocentes();
    }

    private void actualizarDocentes() {
        modeloDocentes.setRowCount(0);
        if (asignaturaSeleccionada == null) return;
        for (Docente d : control().obtenerDocentesDeAsignatura(asignaturaSeleccionada)) {
            modeloDocentes.addRow(new Object[]{d.getCi(), d.getNombre()});
        }
    }

    private void limpiar() {
        asignaturaSeleccionada = null;
        tabla.clearSelection();
        idActual.setText("Automático");
        nombre.setText("");
        creditos.setText("");
        cupo.setText("");
        modeloDocentes.setRowCount(0);
        nombre.requestFocusInWindow();
    }

    private int leerNumero(JTextField campo, String etiqueta) {
        try { return Integer.parseInt(campo.getText().trim()); }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ingresá un número entero para " + etiqueta);
        }
    }

    private void agregar() {
        if (asignaturaSeleccionada != null) {
            JOptionPane.showMessageDialog(this, "Presioná Nuevo / Limpiar para crear otra asignatura");
            return;
        }
        try {
            String n = nombre.getText().trim();
            int c = leerNumero(creditos, "créditos");
            int cup = leerNumero(cupo, "cupo");
            ejecutar("Agregando asignatura...", () -> control().agregarAsignatura(n, c, cup), null);
        } catch (RuntimeException ex) { mostrarError(ex); }
    }

    private void modificar() {
        if (asignaturaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná una asignatura");
            return;
        }
        try {
            int id = asignaturaSeleccionada;
            String n = nombre.getText().trim();
            int c = leerNumero(creditos, "créditos");
            int cup = leerNumero(cupo, "cupo");
            ejecutar("Modificando asignatura...", () -> control().modificarAsignatura(id,n,c,cup), id);
        } catch (RuntimeException ex) { mostrarError(ex); }
    }

    private void eliminar() {
        if (asignaturaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná una asignatura");
            return;
        }
        int id = asignaturaSeleccionada;
        if (JOptionPane.showConfirmDialog(this,
                "¿Eliminar la asignatura ID " + id + "? Solo se eliminará si no tiene vínculos académicos.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        ejecutar("Eliminando asignatura...", () -> control().eliminarAsignatura(id), null);
    }

    private void asignarDocente() {
        if (asignaturaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,"Primero seleccioná una asignatura");
            return;
        }
        int id = asignaturaSeleccionada;
        final List<Docente> disponibles;
        try { disponibles = control().obtenerDocentesDisponiblesParaAsignatura(id); }
        catch (RuntimeException ex) { mostrarError(ex); return; }
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,"No quedan docentes disponibles para esta asignatura");
            return;
        }
        JComboBox<Docente> opciones = new JComboBox<>(disponibles.toArray(new Docente[0]));
        opciones.setRenderer(new DefaultListCellRenderer() {
            @Override public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> lista, Object valor, int indice,
                    boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                if (valor instanceof Docente) {
                    Docente d=(Docente) valor;
                    setText(d.getNombre() + " - " + d.getCi());
                }
                return this;
            }
        });
        if (JOptionPane.showConfirmDialog(this, opciones, "Asignar docente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) return;
        Docente elegido = (Docente) opciones.getSelectedItem();
        if (elegido != null) ejecutar("Asignando docente...",
                () -> control().asignarDocenteAsignatura(id, elegido.getCi()), id);
    }

    private void quitarDocente() {
        if (asignaturaSeleccionada == null || tablaDocentes.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this,"Seleccioná una asignatura y un docente asignado");
            return;
        }
        int fila = tablaDocentes.convertRowIndexToModel(tablaDocentes.getSelectedRow());
        String ci = modeloDocentes.getValueAt(fila,0).toString();
        int id = asignaturaSeleccionada;
        if (JOptionPane.showConfirmDialog(this,
                "¿Quitar al docente " + ci + " de esta asignatura?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        ejecutar("Quitando docente...", () -> control().desasignarDocenteAsignatura(id,ci), id);
    }

    private void ejecutar(String mensaje, Runnable accion, Integer seleccionarDespues) {
        bloquear(true);
        estado.setText(mensaje);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() {
                accion.run();
                return null;
            }
            @Override protected void done() {
                bloquear(false);
                try {
                    get();
                    actualizarTabla();
                    if (seleccionarDespues == null) limpiar();
                    else seleccionarFila(seleccionarDespues);
                    estado.setText("Operación realizada correctamente");
                    JOptionPane.showMessageDialog(gestionarAsignatura.this,
                            "Operación realizada correctamente");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    mostrarError(ex);
                } catch (ExecutionException ex) {
                    mostrarError(ex.getCause() == null ? ex : ex.getCause());
                }
            }
        }.execute();
    }

    private void seleccionarFila(int id) {
        for (int i=0; i<modelo.getRowCount(); i++) {
            if (((Number)modelo.getValueAt(i,0)).intValue()==id) {
                int vista=tabla.convertRowIndexToView(i);
                if (vista >= 0) tabla.setRowSelectionInterval(vista,vista);
                cargarSeleccion();
                return;
            }
        }
    }

    private void bloquear(boolean ocupada) {
        for (JButton b : new JButton[]{nuevo,agregar,modificar,eliminar,asignar,desasignar,atras}) {
            b.setEnabled(!ocupada);
        }
        tabla.setEnabled(!ocupada);
        tablaDocentes.setEnabled(!ocupada);
        filtro.setEnabled(!ocupada);
    }

    private void mostrarError(Throwable ex) {
        if (estado != null) estado.setText("Ocurrió un error");
        JOptionPane.showMessageDialog(this,
                ex.getMessage()==null ? ex.toString() : ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void volver() {
        new interfazAdmin().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new gestionarAsignatura().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel accionesDocentes;
    private javax.swing.JButton agregar;
    private javax.swing.JButton asignar;
    private javax.swing.JButton atras;
    private javax.swing.JPanel botonesPanel;
    private javax.swing.JPanel buscadorPanel;
    private javax.swing.JTextField creditos;
    private javax.swing.JTextField cupo;
    private javax.swing.JButton desasignar;
    private javax.swing.JSplitPane dividido;
    private javax.swing.JPanel docentesPanel;
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
    private javax.swing.JLabel idActual;
    private javax.swing.JButton modificar;
    private javax.swing.JTextField nombre;
    private javax.swing.JButton nuevo;
    private javax.swing.JPanel pie;
    private javax.swing.JPanel principal;
    private javax.swing.JScrollPane scrollDocentes;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JPanel superior;
    private javax.swing.JTable tabla;
    private javax.swing.JTable tablaDocentes;
    // End of variables declaration//GEN-END:variables

}
