/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;


/**
 *
 * @author Adris
 */
public class Portapapeles{
    private ExtractorImagen ei;
    private Clipboard cb;

    public Portapapeles(){
        obtenerClipboard();
    }

    public void setExtractorImagen(ExtractorImagen ei){
        this.ei = ei;
    }

    public void obtenerClipboard(){
        this.cb = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public void extraerImagen(){
        ei.obtenerImagen(cb);
    }

}
