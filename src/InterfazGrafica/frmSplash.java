package InterfazGrafica;



import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class frmSplash extends javax.swing.JFrame implements Runnable{

   
    Thread t;//se crea el hilo en el que corre frmsplash
    
    public frmSplash() {
        initComponents();
        
    }
    @Override
    public void run() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            setLocationRelativeTo(null);
            java.net.URL logo = getClass().getResource("/imagenes/logo_pd.png");
            if (logo != null) jLabel1.setIcon(new javax.swing.ImageIcon(logo));
            setVisible(true);
            javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    Logica.CargaInicial.cargarTodo();
                    return null;
                }
                @Override
                protected void done() {
                    try {
                        get();
                        if (!Logica.CargaInicial.estaCargado())
                            throw new IllegalStateException("Carga incompleta");
                        dispose();
                        new interfazUsuario().setVisible(true);
                    } catch (Exception ex) {
                        Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                        JOptionPane.showMessageDialog(frmSplash.this,
                                "No se pudo iniciar SGA: " + causa.getMessage(),
                                "Error de carga", JOptionPane.ERROR_MESSAGE);
                        dispose();
                        System.exit(1);
                    }
                }
            };
            worker.execute();
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 102));
        setUndecorated(true);

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\guill\\Documents\\NetBeansProjects\\SistemaGestion\\src\\imagenes\\logo_pd.png")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new frmSplash().run());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
