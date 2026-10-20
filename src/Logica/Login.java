/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Persistencia.Persistencia;
/**
 *
 * @author default
 */
public class Login {
    private Persistencia persistencia = new Persistencia();

    public boolean validarLogin(String cedula, String contrasena) {
        // Si no escribió nada, retorna nulo directamente
        if (cedula.trim().isEmpty() || contrasena.trim().isEmpty()) {
            return false;
        }
        // Si escribió algo, consulta la base de datos
        return persistencia.validarUsuario(cedula.trim(), contrasena.trim());
    }
    
    public String obtenerRol(String cedula) {
        return persistencia.obtenerRol(cedula);
    }
}
