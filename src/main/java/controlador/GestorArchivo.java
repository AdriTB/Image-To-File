/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;


import modelo.ExtractorImagen;
import vista.Vista;
/**
 *
 * @author Adris
 */
public class GestorArchivo implements ActionListener{
    private Vista vista;
    private ExtractorImagen ei;
    private ImagenControlador ic;

    private String ruta;
    final public static String barra = System.getProperty("file.separator");

    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public void setExtractorImagen(ExtractorImagen ei) {
        this.ei = ei;
    }

    public void setImagenControlador(ImagenControlador ic) {this.ic = ic;}


    public void guardarImagen(String ruta){
        File archivo = new File(ruta);
        archivo = comprobarRenombrarArchivo(archivo);
        if(archivo != null){
            BufferedImage imgFinal =  ic.escalarImagen();
            try {
                if(!archivo.getPath().endsWith(".png")){
                    archivo = new File(archivo.getPath() + ".png");
                }
                ImageIO.write(imgFinal, "png", archivo);
                mostrarVentanaInfo("Recorte guardado en: " + archivo.getPath());
            }catch(IOException e){
                Toolkit.getDefaultToolkit().beep();
                mostrarVentanaInfo("Error al guardar la imagen.\nPuede que la ruta sea incorrecta.");
            }
        }
    }

    public File comprobarRenombrarArchivo(File archivo){
        int opcion = 0;
        if(archivo.exists()){
            do {
                opcion = opcionesArchivoExistente(archivo);
                switch (opcion){
                    case 0:
                        opcion = confirmarReemplazar(archivo);
                        if(opcion == 0){
                            return archivo;
                        }
                        break;
                    case 1:
                        return renombrarArchivo(archivo);
                    case 2:
                        return null;
                }
            }while(opcion != 2);
        }
        return archivo;
    }

    public int opcionesArchivoExistente(File archivo){
        int opcion;
        String[] botones = {"Reemplazar", "Guardar ambos", "Cancelar"};
        opcion = JOptionPane.showOptionDialog(
                        vista.getFrame(),
                        "¿Quieres sobreescribirlo o guardar ambos?",
                        "El archivo " + archivo.getName() + " ya existe",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        botones,
                        botones[0]);
        //Opciones -> 0: Reemplazar, 1: Renombrar y guardar ambos, 2: Cancelar
        return opcion;
    }

    public int confirmarReemplazar(File archivo){
        int opcion;
        opcion = JOptionPane.showOptionDialog(
                        vista.getFrame(),
                        "Se va a sobreescribir el archivo " + archivo.getName(),
                        "Reemplazar",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, null, null);
        //Opciones -> 0: Conformar reemplazar, 1: Cancelar
        return opcion;
    }

    public static File renombrarArchivo(File archivo){
        File archivoRenombrado;
        String[] rutaDescompuesta = archivo.getPath().split("\\.");
        Pattern patron = Pattern.compile("\\(\\d+\\)$");
        Matcher matcher = patron.matcher(rutaDescompuesta[0]);
        int contador = 1;
        if(matcher.find()) {
            rutaDescompuesta[0] = rutaDescompuesta[0].substring(
                    0,
                    rutaDescompuesta[0].lastIndexOf("(")
            );
            String contadorCadena = archivo.getPath().substring(
                    matcher.start() + 1,
                    matcher.end() - 1
            );
            contador = Integer.parseInt(contadorCadena);
            archivoRenombrado = new File(
                    rutaDescompuesta[0] +
                            "(" + ++contador + ")." +
                            rutaDescompuesta[1]
            );
        }else{
            archivoRenombrado = new File(
                    rutaDescompuesta[0] +
                            "(" + contador + ")." +
                            rutaDescompuesta[1]
            );
        }
        while(archivoRenombrado.exists()){
            archivoRenombrado = renombrarArchivo(archivoRenombrado);

        }
        return archivoRenombrado;
    }

    public void mostrarVentanaInfo(String info){
        JOptionPane.showMessageDialog(
                vista.getFrame(),
                info);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Guardar")){
            String nombreDefecto = "Recorte_" + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".png";
            File rutaEscritorio = FileSystemView.getFileSystemView().getHomeDirectory();
            ruta =  rutaEscritorio + barra +  nombreDefecto;
            guardarImagen(ruta);
        }
        if (e.getActionCommand().equals("ApproveSelection")){
            JFileChooser fc = (JFileChooser) e.getSource();
            ruta = fc.getSelectedFile().getPath();
            guardarImagen(ruta);
        }

    }


}
