/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modelo.ExtractorImagen;
import vista.Vista;

/**
 *
 * @author Adris
 */
public class ImagenControlador implements ChangeListener {
    private ExtractorImagen ei;
    private Vista vista;
    private Image imgOriginal;
    private Image[] imgEscaladasVisor = new Image[3];
    private Dimension[] imgEscaladasReales = new Dimension[3];
    private int zoom;
    private float factorEscalado;

    public void setExtractorImagen(ExtractorImagen ei){
        this.ei = ei;
    }

    public void setVista(Vista vista){
        this.vista = vista;
    }

    public void setImagenOriginal(Image img){
        this.imgOriginal = img;
        prepararImgEscaladasGuardado();
        prepararImagen();
    }

    public void prepararImagen(){
        if(imgOriginal != null){
            prepararInfo();
            //System.out.println("Imagen Original: altura: " + imgOriginal.getHeight(null) + ", anchura: " + imgOriginal.getWidth(null));
            //Ajusta la imagen al panel y obtiene el factor de escalado
            ajustarImagen(imgOriginal);
            //System.out.println("Factor de escalado: " + factorEscalado);
            //Escala las tres imagenes para usar el zoom
            escalarImagenes();
            //Coloca la imagen en el visor
            vista.colocarImagen(imgEscaladasVisor[zoom]);
        }
    }

    public void prepararInfo(){
        actualizarInfo();
        String infoFecha = "Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        vista.imprimirFecha(infoFecha);
    }

    public void actualizarInfo(){
        String infoDimensiones =    String.format("Dimensiones: %d x %d (px)", (int)imgEscaladasReales[zoom].getWidth(), (int)imgEscaladasReales[zoom].getHeight());
        vista.imprimirInfo(infoDimensiones);
    }

    public void prepararInfoError(){
        String err = "No hay imágenes en el portapapeles";
        vista.mostrarError(err);
    }

    public void ajustarImagen(Image imgOriginal){
        Dimension lblDim = vista.getlblImagenDim();
        if(imgOriginal.getHeight(null) < imgOriginal.getWidth(null)){
            this.imgEscaladasVisor[0] = imgOriginal.getScaledInstance(lblDim.width, -1, Image.SCALE_FAST);
        }else{
            this.imgEscaladasVisor[0] = imgOriginal.getScaledInstance(-1, lblDim.height, Image.SCALE_FAST);
        }
        factorEscalado = (float)this.imgEscaladasVisor[0].getHeight(null) / (float)imgOriginal.getHeight(null);
    }

    public void escalarImagenes(){
        //Escala las 2 imágenes, x2 y x3
        for(int i = 1; i < imgEscaladasVisor.length; i++){
            this.imgEscaladasVisor[i] = imgOriginal.getScaledInstance(   (int)(imgOriginal.getWidth(null) * (i+1) * factorEscalado),
                    (int)(imgOriginal.getHeight(null) * (i+1) * factorEscalado),
                    Image.SCALE_FAST);
        }
    }

    public void prepararImgEscaladasGuardado(){
        for(int i = 0; i < imgEscaladasReales.length; i++){
            int zoom = i + 1;
            imgEscaladasReales[i] = new Dimension(imgOriginal.getWidth(null) * zoom, imgOriginal.getHeight(null) * zoom);
        }
    }

    public BufferedImage escalarImagen(){
        int zoomGuardado = vista.tamanoGuardar();
        Image imagenEscalada = imgOriginal;
        if(zoom != 1){
            imagenEscalada = imgOriginal.getScaledInstance(
                    (int)(imgOriginal.getWidth(null) * zoomGuardado),
                    (int)(imgOriginal.getHeight(null) * zoomGuardado),
                    Image.SCALE_SMOOTH);
        }
        BufferedImage imagenFinal = new BufferedImage(
                imagenEscalada.getWidth(null),
                imagenEscalada.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagenFinal.createGraphics();
        g.drawImage(imagenEscalada, 0, 0, null);
        g.dispose();
        return imagenFinal;
    }

    public void setZoom(int zoom){
        this.zoom = zoom;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider js = (JSlider)e.getSource();
        zoom = js.getValue()-1;
        if(imgOriginal != null){
            vista.colocarImagen(imgEscaladasVisor[zoom]);
            actualizarInfo();
        }
    }

}
