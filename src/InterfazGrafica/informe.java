/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package InterfazGrafica;

import Logica.Asignatura;
import Logica.Docente;
import Logica.Docentes;
import Logica.Estudiante;
import Logica.InformeAcademico;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author guill
 */
public class informe extends javax.swing.JFrame {

    private Docente control() {
        return Docentes.getInstancia().obtenerSesionActual();
    }
    private final List<Asignatura> asignaturasDocente = new ArrayList<>();
    private boolean guardando = false;

    /**
     * Creates new form informe
     */
    public informe() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Generar informe académico");
        jLabel2.setText("Informe académico");
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        cargarAsignaturasDesdeMemoria();

        // Si se cambia la materia, debe buscarse de nuevo el alumno.
        choice1.addItemListener(evt -> jLabel2.setText("Informe académico"));
    }

    private void cargarAsignaturasDesdeMemoria() {
        try {
            asignaturasDocente.clear();
            choice1.removeAll();
            asignaturasDocente.addAll(control().obtenerMisAsignaturas());

            for (Asignatura asignatura : asignaturasDocente) {
                choice1.add(asignatura.getNombre());
            }
            if (asignaturasDocente.isEmpty()) {
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "Este docente no tiene asignaturas asignadas.",
                        "Sin asignaturas", JOptionPane.WARNING_MESSAGE);
            }
        } catch (RuntimeException ex) {
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            mostrarError("No se pudieron cargar las materias", ex);
        }
    }

    private Asignatura asignaturaElegida() {
        int indice = choice1.getSelectedIndex();
        if (indice < 0 || indice >= asignaturasDocente.size()) {
            throw new IllegalArgumentException("Seleccioná una materia");
        }
        return asignaturasDocente.get(indice);
    }

    private void buscarEstudiante() {
        try {
            Asignatura materia = asignaturaElegida();
            Estudiante estudiante = control().buscarEstudianteParaInforme(
                    jTextField1.getText().trim(), materia.getId());
            jLabel2.setText("Alumno: " + estudiante.getNombre());
            jLabel2.setToolTipText(estudiante.getNombre());
        } catch (RuntimeException ex) {
            jLabel2.setText("Informe académico");
            mostrarError("No se pudo encontrar al estudiante", ex);
        }
    }

    private void guardarInforme() {
        if (guardando) {
            return;
        }

        final Asignatura materia;
        final String cedula = jTextField1.getText().trim();
        final String contenido = jTextArea1.getText().trim();

        try {
            materia = asignaturaElegida();
            Estudiante estudiante = control().buscarEstudianteParaInforme(
                    cedula, materia.getId());
            if (contenido.isEmpty()) {
                throw new IllegalArgumentException("Escribí el informe antes de guardar");
            }
            jLabel2.setText("Alumno: " + estudiante.getNombre());
        } catch (RuntimeException ex) {
            mostrarError("No se puede guardar el informe", ex);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Guardar informe de " + cedula + " en "
                + materia.getNombre() + "?",
                "Confirmar informe", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        guardando = true;
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);

        new SwingWorker<InformeAcademico, Void>() {
            @Override
            protected InformeAcademico doInBackground() {
                // JDBC se ejecuta fuera del hilo visual de Swing.
                return control().generarInforme(
                        cedula, materia.getId(), contenido);
            }

            @Override
            protected void done() {
                guardando = false;
                jButton1.setEnabled(true);
                jButton2.setEnabled(true);
                jButton3.setEnabled(true);

                try {
                    InformeAcademico creado = get();
                    JOptionPane.showMessageDialog(informe.this,
                            "Informe guardado correctamente. ID: "
                            + creado.getIdInforme());
                    jTextArea1.setText("");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    mostrarError("Se interrumpió el guardado", ex);
                } catch (java.util.concurrent.ExecutionException ex) {
                    Throwable causa = ex.getCause() != null
                            ? ex.getCause() : ex;
                    mostrarError("No se pudo guardar el informe", causa);
                }
            }
        }.execute();
    }

    private void mostrarError(String contexto, Throwable ex) {
        JOptionPane.showMessageDialog(this,
                contexto + ": " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        choice1 = new java.awt.Choice();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Cedula alumno");

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Informe");

        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Informe:");

        jLabel4.setText("Materia docente");

        jButton3.setText("Atras");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(58, 58, 58))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addGap(54, 54, 54)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(choice1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(58, 58, 58)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(choice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        guardarInforme();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        buscarEstudiante();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        buscarEstudiante();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    interfazDocente ventana = new interfazDocente();
    ventana.setLocationRelativeTo(this);
    ventana.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(informe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(informe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(informe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(informe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new informe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Choice choice1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
