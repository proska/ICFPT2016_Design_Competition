package GUI;

import Game.World.traxTiles;

import java.awt.*;

/*
 * AbsoluteLayoutDemo.java requires no other files.
 */

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import javax.swing.*;

public class traxGUI {


    static final int TILESIZE = 50;

    public static class TileType{
        public JLabel label;
        public int X;
        public int Y;
        TileType(JLabel _label, int x , int y){
            label = _label;
            X = x;
            Y = y;
        }
    }

    static final JPanel panel = new JPanel();
    static final JFrame frame = new JFrame("AbsoluteLayoutDemo");

    static LinkedList tilesOnMap = new LinkedList<TileType> ();

    static int xZero;
    static int yZero;

    static int FrameWidth  = 1300;
    static int FrameHeight = 650;

    public static void addTile(int x, int y , traxTiles tile) {

        System.out.println("[INFO] Tile:"+tile+" Added to GUI in ("+x+","+y+").");

        JLabel    label    = new JLabel();
        ImageIcon icon = createImageIcon("Images/" + tile.getVal() + ".png");
        //home/proska/Desktop/UTTrax/src/main/scala/GUI/Images/
        label.setIcon(icon);

        label.setToolTipText("A drawing of a " + (tile.getVal()));
        if (icon != null) {
            label.setText(null);
        } else {
            label.setText("Image not found");
        }

//        label.updateUI();

        tilesOnMap.add(new TileType(label, x, y));

        if(x < -(int)(xZero/TILESIZE) ){
            shiftAllRigth();
            System.out.println("shiftAllRigth");
        }
        if(y < -(int)((FrameHeight - yZero)/TILESIZE)){
            extendHeight();
            System.out.println("Height Extended");

        }
        if(x >= (int)((FrameWidth - xZero)/TILESIZE)){
            extendWidth();
            System.out.println("Width Extended");
        }
        if(y >= (int)(yZero/TILESIZE)){
            shiftAllDown();
            System.out.println("shiftAllDown");
        }
        panel.removeAll();
        reDrawPane();
    }

    //  Create the GUI and show it.  For thread safety,this method should be invoked from the event-dispatching thread.
    public static void startGUI() {
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane scrol = new JScrollPane(panel);

        //Size and display the window.
        Insets insets = frame.getInsets();
        frame.setSize(  FrameWidth + insets.left + insets.right,
                FrameHeight + insets.top + insets.bottom);

        xZero = FrameWidth /2;
        yZero = FrameHeight /2;

        frame.getContentPane().add(scrol,BorderLayout.CENTER);
        frame.setVisible(true);

        reDrawPane();

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                panel.removeAll();
                reDrawPane();
            }
        });
    }

    private static void reDrawPane() {

//        System.out.println(">> Redraw GUI <<");

        panel.setLayout(null);
        Insets insets = panel.getInsets();

        for(int i =0 ; i < tilesOnMap.size()  ;i++ ){

            TileType tmp = (TileType)tilesOnMap.get(i);

            tmp.label.setBounds(    xZero + tmp.X * TILESIZE - TILESIZE/2,
                    yZero - tmp.Y * TILESIZE - TILESIZE/2,
                    TILESIZE, TILESIZE);

//            System.out.println("("+tmp.label.getAlignmentX()+","+tmp.label.getAlignmentY()+")");
            panel.add(tmp.label);
        }
        panel.setPreferredSize(new Dimension(FrameWidth, FrameHeight));
        panel.setBackground(Color.green);


        assert(frame.getHeight() == FrameHeight);
        assert(frame.getWidth() == FrameWidth);
//        System.out.println(tilesOnMap.size());
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    private static ImageIcon createImageIcon(String path) {

        java.net.URL imgURL = traxGUI.class.getResource(path);
        ImageIcon icon = new ImageIcon(imgURL);
        icon.setImage(icon.getImage().getScaledInstance(TILESIZE,TILESIZE,Image.SCALE_DEFAULT));

        if(icon == null){
            System.err.println("Couldn't find file: " + path);
        }
        return icon;
    }

    private static void shiftAllRigth(){
        xZero += 5*TILESIZE;
        System.out.println("New xZero="+xZero);
    }
    private static void shiftAllDown(){
        yZero += 5*TILESIZE;
        System.out.println("New yZero="+yZero);
    }
    private static void extendWidth(){
        FrameWidth  += 5*TILESIZE;
        updateFrameSize();
//        xZero += 5*TILESIZE;
    }
    private static void extendHeight(){
        FrameHeight += 5*TILESIZE;
        updateFrameSize();
    }
    private static void updateFrameSize(){
        Insets insets = frame.getInsets();
        frame.setSize(  FrameWidth + insets.left + insets.right,
                FrameHeight + insets.top + insets.bottom);

        assert(frame.getHeight() == FrameHeight);
        assert(frame.getWidth() == FrameWidth);

    }



    public static void main(String[] args) {

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startGUI();
            }
        });


//        addTile(0,-10, traxTiles.BBWW);
//        addTile(14,0, traxTiles.BBWW);
//        addTile(0,0, traxTiles.BBWW);

        for(int i = 0 ; i <= 14 ; i++){
            addTile(0,-i, traxTiles.BBWW);
            System.out.println(">> 1 sec passed!");
        }

    }
}
