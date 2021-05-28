/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Portapapeles;
import vista.Vista;

/**
 *
 * @author Adris
 */
public class PortapapelesControlador implements ActionListener{
    private Portapapeles pp;
    private Vista vista;

    public void setPortapapeles(Portapapeles pp){
        this.pp = pp;
    }

    public void setVista(Vista vista){
        this.vista = vista;
    }

    public void ordenExtraerImagen(){
        this.pp.extraerImagen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Recarga")){
            vista.resetearVisor();
            this.ordenExtraerImagen();
        }

    }



}
