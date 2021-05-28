/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import modelo.ExtractorImagen;
import modelo.Portapapeles;
import vista.Vista;

/**
 *
 * @author Adris
 */
public class Main {
    public static void main(String[] args) {
        //MODELO
        ExtractorImagen ei = new ExtractorImagen();
        Portapapeles pp = new Portapapeles();

        //VISTA
        Vista vista = new Vista();

        //CONTROLADOR
        PortapapelesControlador pc = new PortapapelesControlador();
        ImagenControlador ic = new ImagenControlador();
        GestorArchivo ga = new GestorArchivo();

        //Configuración
        ei.setImagenControlador(ic);
        pp.setExtractorImagen(ei);
        vista.setPortapapelesControlador(pc);
        vista.setImagenControlador(ic);
        vista.setGestorArchivo(ga);
        pc.setPortapapeles(pp);
        pc.setVista(vista);
        ic.setExtractorImagen(ei);
        ic.setVista(vista);
        ga.setVista(vista);
        ga.setExtractorImagen(ei);
        ga.setImagenControlador(ic);

        //INICIO
        vista.iniciar();
    }

}
