/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.List;

public class Docentes {
    private List<Docente>listaDocentes;
    
    //Constructor para inicializar lista vacia
    public Docentes(){
        this.listaDocentes = new ArrayList<>();
    }

    public Docentes(List<Docente> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

   
     public void agregarDocente(Docente docente) {
        listaDocentes.add(docente);
    }

    public void eliminarDocente(Docente docente) {
        listaDocentes.remove(docente);
    }
    
    //Get y Set para la lista
    public List<Docente> getListaDocentes(){
        return listaDocentes;
    }
    
    public void setListaDocentes(List<Docente> listDocentes){
        this.listaDocentes = listaDocentes;
    }
}

