package vista;

import controlador.GestorArchivo;
import controlador.ImagenControlador;
import controlador.PortapapelesControlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

public class Vista {
    private JFrame frame;
    private JPanel pnlPrincipal;
    private JButton btnRecarga;
    private JSlider sldZoom;
    private JButton btnGuardar;
    private JPanel pnlVisor;
    private JScrollPane scrImagen;
    private JLabel lblImagen;
    private JPanel pnlLateral;
    private JPanel pnlHerramientas;
    private JPanel pnlGuardado;
    private JLabel lblInfoDimensiones;
    private JLabel lblInfoFecha;
    private JButton btnExplorar;
    private JRadioButton rbtn3;
    private JRadioButton rbtn2;
    private JRadioButton rbtn1;
    private JPanel pnlZoom;
    private JFileChooser flChGuardar;

    private ImagenControlador ic;
    private PortapapelesControlador pc;
    private GestorArchivo ga;
    private Dimension lblImagenDim;
    private boolean panelHabilitado;
    private int mouseX;
    private int mouseY;

    public Vista() {

        pnlPrincipal.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                ic.prepararImagen();
            }
        });

        btnExplorar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreDefecto = "Recorte_" +
                                LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) +
                                ".png";
                flChGuardar.setDialogTitle("Guardar como");
                flChGuardar.setSelectedFile(new File(nombreDefecto));
                flChGuardar.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
                flChGuardar.showSaveDialog(frame);
            }
        });

        scrImagen.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseY = e.getY();
                mouseX = e.getX();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = mouseX - e.getX();
                int deltaY = mouseY - e.getY();
                scrImagen.getVerticalScrollBar().setValue(scrImagen.getVerticalScrollBar().getValue() + deltaY);
                scrImagen.getHorizontalScrollBar().setValue(scrImagen.getHorizontalScrollBar().getValue() + deltaX);
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    public void iniciar() {
        tituloEIcono();
        flChGuardar = new JFileChooser();
        flChGuardar.setCurrentDirectory(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile()));
        frame.setContentPane(this.pnlPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        tamanoVentana();
        frame.setVisible(true);
        //Listeners
        sldZoom.addChangeListener(ic);
        btnRecarga.addActionListener(pc);
        btnGuardar.addActionListener(ga);
        flChGuardar.addActionListener(ga);

        scrImagen.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        getlblImagenDim();
        habilitarPanel(false);
        setLookAndFeel();

        pc.ordenExtraerImagen();
    }

    public void setPortapapelesControlador(PortapapelesControlador pc) {
        this.pc = pc;
    }

    public void setImagenControlador(ImagenControlador ic) {
        this.ic = ic;
    }

    public void setGestorArchivo(GestorArchivo ga) {
        this.ga = ga;
    }

    public JFrame getFrame(){
        return this.frame;
    }

    public Dimension getlblImagenDim() {
        lblImagenDim = new Dimension(scrImagen.getWidth(), scrImagen.getHeight());
        return lblImagenDim;
    }

    public void colocarImagen(Image img) {
        lblImagen.setText("");
        lblImagen.setIcon(new ImageIcon(img));
        ajustarTamanoVisor(new Dimension(img.getWidth(null), img.getHeight(null)));
        habilitarPanel(true);
    }

    public void ajustarTamanoVisor(Dimension dim) {
        lblImagen.setPreferredSize(dim);
    }

    public void resetearVisor() {
        lblImagen.setPreferredSize(lblImagenDim);
        sldZoom.setValue(1);
    }

    public Dimension getInfoImagenMostrada(){
        Dimension dimension;
        dimension = new Dimension(lblImagen.getIcon().getIconWidth(), lblImagen.getIcon().getIconHeight());
        return dimension;
    }

    public void imprimirInfo(String dim) {
        lblInfoDimensiones.setForeground(new Color(30, 30, 30));
        lblInfoDimensiones.setText(dim);
    }

    public void imprimirFecha(String fecha){
        lblInfoFecha.setText(fecha);
    }

    public void mostrarError(String err) {
        lblInfoDimensiones.setText("");
        lblInfoFecha.setText("");
        lblInfoDimensiones.setForeground(new Color(200, 0, 0));
        lblInfoDimensiones.setText(err);
    }

    public int tamanoGuardar(){
        int tamano = 1;
        if(rbtn2.isSelected()){
            tamano = 2;
        }else if(rbtn3.isSelected()){
            tamano = 3;
        }
        return tamano;
    }

    public void tituloEIcono(){
        frame = new JFrame("Image To File");
        ImageIcon icono = new ImageIcon(getClass().getClassLoader().getResource("ICON.png"));
        frame.setIconImage(icono.getImage());
    }

    public void tamanoVentana(){
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(pantalla.width / 2, pantalla.height / 2);
    }

    public void habilitarPanel(boolean orden){
        panelHabilitado = orden;
        btnExplorar.setEnabled(panelHabilitado);
        btnGuardar.setEnabled(panelHabilitado);
        sldZoom.setEnabled(panelHabilitado);
    }

    public void setLookAndFeel() {
        String sysLook = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(sysLook);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
