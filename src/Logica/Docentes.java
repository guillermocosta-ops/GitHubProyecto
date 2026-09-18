/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.List;
public class Docentes {
    private List<Docente>listaDocentes;

    public Docentes(List<Docente> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

   
     public void agregarDocente(Docente docente) {
        listaDocentes.add(docente);
    }

    public void eliminarDocente(Docente docente) {
        listaDocentes.remove(docente);
    }
}

