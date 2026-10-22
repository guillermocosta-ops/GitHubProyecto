/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.List;

public class Estudiantes {
    private List<Estudiante>listaEstudiantes;

    //construtor sin parametros para inicializar una lista vacia
    public Estudiantes(){
        this.listaEstudiantes = new ArrayList<>();
    }
    public Estudiantes(List<Estudiante> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }
     public void agregarEstudiante(Estudiante estudiante) {
        listaEstudiantes.add(estudiante);
    }

    public void eliminarEstudiante(Estudiante estudiante) {
        listaEstudiantes.remove(estudiante);
    }
    
    //Get y set para que la interfaz pueda operar
    public List<Estudiante> getListaEstudiantes(){
        return listaEstudiantes;
    }
     public void setListaEstudiantes(List<Estudiante> listaEstudiantes){
         this.listaEstudiantes = listaEstudiantes;
     }
}
