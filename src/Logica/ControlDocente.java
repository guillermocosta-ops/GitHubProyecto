/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;
import Persistencia.Persistencia;
import java.util.List;


public class ControlDocente {
    private Persistencia persistencia;
    
    public ControlDocente() {
        this.persistencia = new Persistencia();
    }
    
    // Pido a Persistencia la coleccion de estudiantes con el metodo ubicado allí
    public Estudiantes obtenerEstudiantes(){
        return persistencia.obtenerEstudiantes();
    }
    
    /*public double calcularPromedioSimpleEstudiante(int cedula) {
    List<Object[]> datos = persistencia.obtenerCalificacionesConCreditos();
    double suma = 0;
    int contador = 0;

    for (Object[] fila : datos) {
        int ci = (int) fila[0]; // Cédula en posición 0
        if (ci == cedula) {
            suma += (int) fila[2]; // Calificación en posición 2
            contador++;
        }
    }
    return (contador == 0) ? 0.0 : suma / contador;
}*/
}
