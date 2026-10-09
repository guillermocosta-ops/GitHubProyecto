/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package InterfazGrafica;

import Persistencia.Conexion;
import java.sql.*;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class calificar extends javax.swing.JFrame {



    private int idAsignatura = 1; // Temporal: cambiar por la asignatura seleccionada

    public calificar() {
        initComponents();
        setLocationRelativeTo(null);
        cargarEstudiantes();
    }
   private void cargarEstudiantes() {

    DefaultTableModel modelo = new DefaultTableModel();

    modelo.addColumn("Cédula");
    modelo.addColumn("Nombre");
    modelo.addColumn("Asignatura");
    modelo.addColumn("Nota");

    try {
        Conexion DB = Conexion.getInstancia();
        Connection conn = DB.getConnection();

        
String sql = "SELECT u.cedula, u.nombre, " +
             "a.nombre AS asignatura, " +
             "ROUND(AVG(n.calificacion), 2) AS nota " +
             "FROM cursa c " +
             "INNER JOIN estudiante e " +
             "ON c.cedulaEstudiante = e.cedula " +
             "INNER JOIN usuario u " +
             "ON e.cedula = u.cedula " +
             "INNER JOIN asignatura a " +
             "ON c.idAsignatura = a.idAsignatura " +
             "LEFT JOIN nota n " +
             "ON n.cedulaEstudiante = c.cedulaEstudiante " +
             "AND n.idAsignatura = c.idAsignatura " +
             "WHERE c.idAsignatura = ? " +
             "GROUP BY u.cedula, u.nombre, a.nombre";


        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, idAsignatura);

            try (ResultSet rs = st.executeQuery()) {

                while (rs.next()) {

                    Object[] fila = {
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("asignatura"),
                        rs.getObject("nota")
                    };

                    modelo.addRow(fila);
                }
            }
        }

        jTable1.setModel(modelo);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error al cargar estudiantes: " + e.getMessage());
    }
} 


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup4 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonGroup4.add(jRadioButton1);
        jRadioButton1.setText("Oral");

        buttonGroup4.add(jRadioButton2);
        jRadioButton2.setText("Proyecto");

        buttonGroup4.add(jRadioButton3);
        jRadioButton3.setText("Tarea domiciliaria");

        buttonGroup4.add(jRadioButton4);
        jRadioButton4.setText("Escrito");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jButton2.setText("Atras");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton1)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jButton2))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton3)
                        .addGap(26, 26, 26)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    interfazDocente ventana = new interfazDocente();
    ventana.setLocationRelativeTo(this);
    ventana.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
  

    try {

        // Verificar que haya un estudiante seleccionado
        int fila = jTable1.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un estudiante de la tabla");
            return;
        }

        // Obtener cédula
        String cedulaEstudiante = jTable1.getValueAt(fila, 0).toString();

        // Obtener tipo de evaluación
        String tipo = "";

        if (jRadioButton1.isSelected()) {
            tipo = "Oral";
        } else if (jRadioButton2.isSelected()) {
            tipo = "Proyecto";
        } else if (jRadioButton3.isSelected()) {
            tipo = "Tarea domiciliaria";
        } else if (jRadioButton4.isSelected()) {
            tipo = "Escrito";
        }

        if (tipo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un tipo de evaluación");
            return;
        }

        // Obtener nota
        int nota = Integer.parseInt(jTextField1.getText().trim());

        if (nota < 1 || nota > 12) {
            JOptionPane.showMessageDialog(this,
                    "La nota debe estar entre 1 y 12");
            return;
        }

        // Conexión
        Conexion DB = Conexion.getInstancia();
        Connection conn = DB.getConnection();

        // Guardar la evaluación
        String sql = "INSERT INTO nota " +
                "(cedulaEstudiante, idAsignatura, tipo, calificacion, fecha) " +
                "VALUES (?, ?, ?, ?, CURRENT_DATE())";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, cedulaEstudiante);
            st.setInt(2, idAsignatura);
            st.setString(3, tipo);
            st.setInt(4, nota);

            st.executeUpdate();
        }

        JOptionPane.showMessageDialog(this,
                "Calificación agregada correctamente");

        // Limpiar componentes
        jTextField1.setText("");
        buttonGroup4.clearSelection();

        // Actualizar tabla
        cargarEstudiantes();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Ingrese una nota numérica válida");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
    }

    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(calificar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(calificar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(calificar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(calificar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new calificar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
