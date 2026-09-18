/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.List;

public class Notas {
     private  List<Nota> listaNota;

    public Notas(List<Nota> listaNota) {
        this.listaNota = listaNota;
    }
   public List<Nota> getListaNota() {
        return List.copyOf(listaNota);
    }

    public void agregarNota(Nota nota) {
        listaNota.add(nota);
    }

    public void modificarNota(Nota notaModificada) {
        for (int i = 0; i < listaNota.size(); i++) {
            Nota notaActual = listaNota.get(i);

            boolean mismaCedula =
                notaActual.getCi() == notaModificada.getCi();

            boolean mismoTipo =
                notaActual.getTipo().equalsIgnoreCase(
                    notaModificada.getTipo()
                );

            if (mismaCedula && mismoTipo) {
                listaNota.set(i, notaModificada);
                return;
            }
        }

        agregarNota(notaModificada);
    }

    public void eliminarNota(Nota nota) {
        listaNota.remove(nota);
    }
}   

