
package InterfazGrafica;

import Logica.Nota;
import Logica.Docente;
import Logica.Docentes;
import Logica.Estudiante;
import Logica.Estudiantes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class gestionarCalificaciones extends javax.swing.JFrame {

    
    public gestionarCalificaciones() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestionar calificaciones");

        cargarCalificaciones("");
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            seleccionarNotaParaModificar();
        }
    }
});
    }

  
private void seleccionarNotaParaModificar() {

    int fila = jTable1.getSelectedRow();

    if (fila == -1) {
        return;
    }

    int filaModelo = jTable1.convertRowIndexToModel(fila);

    Object nota = jTable1.getModel()
            .getValueAt(filaModelo, 5);

    if (nota != null) {
        jTextField2.setText(nota.toString());
        jTextField2.requestFocusInWindow();
        jTextField2.selectAll();
    }
}

    // =====================================
    // CARGAR CALIFICACIONES
    // =====================================

    private void cargarCalificaciones(String cedula) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Cédula", "Nombre", "Asignatura", "Tipo", "Nota", "Fecha"},0) {
            @Override public boolean isCellEditable(int f, int c) { return false; }
        };
        try {
            Docente control = Docentes.getInstancia().obtenerSesionActual();
            for (Nota nota : control.buscarCalificaciones(cedula)) {
                String nombre = "";
                String materia = "";
                for (Estudiante est : Logica.Estudiantes.getInstancia().getListaEstudiantes())
                    if (est.getCi().equals(nota.getCi())) { nombre=est.getNombre(); break; }
                for (Logica.Asignatura a : Logica.Asignaturas.getInstancia().getListaAsignatura())
                    if (nota.getIdAsignatura() != null && a.getId()==nota.getIdAsignatura()) {
                        materia=a.getNombre(); break;
                    }
                modelo.addRow(new Object[]{nota.getidNota(), nota.getCi(), nombre,
                        materia, nota.getTipo(), nota.getNota(), nota.getFecha()});
            }
            jTable1.setModel(modelo);
            jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));
            jTable1.setRowHeight(28);
            jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTable1.setAutoCreateRowSorter(true);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar notas: " + ex.getMessage());
        }
    }

    // =====================================
    // BUSCAR POR CÉDULA
    // =====================================

    private void buscarCalificaciones() {

        String cedula = jTextField1.getText().trim();

        cargarCalificaciones(cedula);
    }

    // =====================================
    // OBTENER ID DE LA NOTA SELECCIONADA
    // =====================================

    private int obtenerIdNotaSeleccionada() {

        int fila = jTable1.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione una calificación de la tabla");
            return -1;
        }

        int filaModelo = jTable1.convertRowIndexToModel(fila);

        return ((Number) jTable1.getModel()
                .getValueAt(filaModelo, 0)).intValue();
    }

    // =====================================
    // MODIFICAR CALIFICACIÓN
    // =====================================

    private void modificarCalificacion() {

        int idNota = obtenerIdNotaSeleccionada();

        if (idNota == -1) {
            return;
        }

        String texto = jTextField2.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese la nueva calificación");
            return;
        }

        try {
            int nuevaNota = Integer.parseInt(texto);

            if (nuevaNota < 1 || nuevaNota > 12) {
                JOptionPane.showMessageDialog(this,
                    "La nota debe estar entre 1 y 12");
                return;
            }

            Docentes.getInstancia().obtenerSesionActual().modificarNota(idNota, nuevaNota);

            JOptionPane.showMessageDialog(this,
                "Calificación modificada correctamente");

            jTextField2.setText("");

            cargarCalificaciones(
                jTextField1.getText().trim()
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Ingrese una nota numérica válida");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al modificar calificación: "
                + e.getMessage());

            e.printStackTrace();
        }
    }

    // =====================================
    // ELIMINAR CALIFICACIÓN
    // =====================================

    private void eliminarCalificacion() {

        int idNota = obtenerIdNotaSeleccionada();

        if (idNota == -1) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar esta calificación?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Docentes.getInstancia().obtenerSesionActual().eliminarNota(idNota);

            JOptionPane.showMessageDialog(this,
                "Calificación eliminada correctamente");

            cargarCalificaciones(
                jTextField1.getText().trim()
            );

            jTextField2.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar calificación: "
                + e.getMessage());

            e.printStackTrace();
        }
    }
    
    private void cargarTablaEstudiantes(){
        //Solamente vamos a mostrar y traer cedula y nombre
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[] {"Cédula", "Nombre"}, 0)
        {
            @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
         };
        try {
        Docente control = Docentes.getInstancia().obtenerSesionActual();
        Estudiantes coleccion = control.obtenerEstudiantes();

        // Recorremos la colección recibida desde la capa lógica
        // (Asegúrate de ajustar 'getListaEstudiantes()' al nombre real de tu getter en la clase Estudiantes)
        for (Estudiante est : coleccion.getListaEstudiantes()) { 
            Object[] fila = {
                est.getCi(),     
                est.getNombre()
            };
            modelo.addRow(fila);
        }

        jTable1.setModel(modelo);
        jTable1.setRowHeight(28);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + e.getMessage());
        e.printStackTrace();
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Atras");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("Cedula");

        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton3.setText("eliminar");
        jButton3.setActionCommand("eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Modificar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(jButton2)))
                .addContainerGap(599, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(61, 61, 61)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4))
                        .addGap(234, 234, 234))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    interfazDocente ventana = new interfazDocente();
    ventana.setLocationRelativeTo(this);
    ventana.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       eliminarCalificacion();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
     modificarCalificacion();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         buscarCalificaciones();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(gestionarCalificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gestionarCalificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gestionarCalificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gestionarCalificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gestionarCalificaciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
