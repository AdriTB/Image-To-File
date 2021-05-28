/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.ImagenControlador;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JOptionPane;


/**
 *
 * @author Adris
 */
public class ExtractorImagen{
    private ImagenControlador ic;
    private Image imagen;
    private Transferable tr;

    public void setImagenControlador(ImagenControlador ic){
        this.ic = ic;
    }

    public Image getImagen(){
        return this.imagen;
    }

    public void obtenerImagen(Clipboard cb){
        try{
            tr = cb.getContents(null);
            if(tr == null){
                imagen = null;
            }
            if(tr.isDataFlavorSupported(DataFlavor.imageFlavor)){
                imagen = (Image) tr.getTransferData(DataFlavor.imageFlavor);
            }

        }catch(Exception ex){
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null ,
                    "Error al obtener imagen.\n Inténtalo de nuevo."
            );
        }
        if(imagen != null){
            ic.setImagenOriginal(imagen);
        }else{
            ic.prepararInfoError();
        }
    }
}
