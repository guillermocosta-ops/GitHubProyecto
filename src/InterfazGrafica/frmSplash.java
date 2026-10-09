package InterfazGrafica;



import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pablo
 */
public class frmSplash extends javax.swing.JFrame implements Runnable{

   
    Thread t;//se crea el hilo en el que corre frmsplash
    
    public frmSplash() {
        initComponents();
        
    }
    @Override
    public void run(){
        
     try{  
        this.setLocationRelativeTo(null);//ubica el formulario en el centro de la pantalla
        this.setVisible(true);//lo hace visible
        t.sleep(4000);//hace que el hilo del programa se detenga 4 segundos
        this.dispose();//cierra el formulario splash
        new interfazUsuario().setVisible(true);//aparece formulario principal
        
    
     }catch(InterruptedException ex){
       Logger.getLogger(frmSplash.class.getName()).log(Level.SEVERE,null,ex);//codigo generado para controlar errores
     }
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
    
    public static void main(String[]args){
        new Thread (new frmSplash()).start();
        System.out.println("asdasd");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
