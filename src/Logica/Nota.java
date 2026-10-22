/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.time.LocalDate;

/**
 *
 * @author guill
 */
public class Nota {
    private int idNota;
    private int nota;
    private LocalDate fecha;
    private String tipo;
    private int ci;

    public Nota(int idNota, int nota, LocalDate fecha, String tipo, int ci) {
        this.idNota = idNota;
        this.nota = nota;
        this.fecha = fecha;
        this.tipo = tipo;
        this.ci = ci;
    }
    public Nota(int idNota, LocalDate fecha, String tipo, int nota) {
        this.idNota = idNota;
        this.fecha = fecha;
        this.tipo = tipo;
        this.nota = nota;
    }
    
    
    public int getidNota(){
        return idNota;
    }
    
    public void setidNota(int idNota){
        this.idNota = idNota;
    }
    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

}
