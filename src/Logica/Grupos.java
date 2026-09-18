/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.List;

public class Grupos {
    private final List<Grupo> listaGrupos;

    public Grupos() {
        listaGrupos = new ArrayList<>();
    }

    public List<Grupo> getListaGrupos() {
        return List.copyOf(listaGrupos);
    }

    public void agregarGrupo(Grupo grupo) {
        listaGrupos.add(grupo);
    }

    public void eliminarGrupo(Grupo grupo) {
        listaGrupos.remove(grupo);
    }
}