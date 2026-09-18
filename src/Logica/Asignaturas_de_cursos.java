/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.List;
public class Asignaturas_de_cursos {
      private List<Asignatura_de_curso>listaAsignaturas_de_curso;

    public Asignaturas_de_cursos(List<Asignatura_de_curso> listaAsignaturas_de_curso) {
        this.listaAsignaturas_de_curso = listaAsignaturas_de_curso;
    }
       public void agregarAsignatura_de_curso(Asignatura_de_curso asignatura_de_curso) {
        listaAsignaturas_de_curso.add(asignatura_de_curso);
    }

    public void eliminarAsignatura_de_curso(Asignatura_de_curso asignatura_de_curso) {
        listaAsignaturas_de_curso.remove(asignatura_de_curso);
    }
}
