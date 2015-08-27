package GUI;

import Game.World.Coordinate;
import Game.World.traxTiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class traxGUI {

    /** The stragey that allows us to use accelerate page flipping */
    private BufferStrategy strategy;

    /** The stragey that allows us to use accelerate page flipping */
    private Canvas window = new Canvas();

    JFrame container = null;

    private int xZero;
    private int yZero;

    private int screenWidth  = 600;//1200;
    private int screenHeight = 500;//700;


    private void updateZero(){
        xZero = screenWidth/2;
        yZero = screenHeight/2;
    }

    /** The datatype containing info on each tile on screen **/
    private class tileGUI{
        public Coordinate pos;
        public traxTiles tile;

        public tileGUI(Coordinate pos, traxTiles tile) {
            int size  = SpriteStore.getSpriteSize();
            this.pos = new Coordinate(pos.X() * size , pos.Y()*size);
            this.tile = tile;
        }
        public Coordinate getPositioninScreen(){
            return new Coordinate(pos.X() + xZero , -pos.Y() + yZero);
        }
    }

    /** The list containing all info of all tiles on screen **/
    private LinkedList<tileGUI> tileList = null;

    /** redrawing the screen based on tileList with use of double buffering to prevent flickering **/
    public void redraw (){

        // Set screen and container size
        window.setSize(screenWidth,screenHeight);
//        container.getContentPane().setSize(screenWidth, screenHeight);
//
//        strategy = window.getBufferStrategy();

        // Get hold of a graphics context for the accelerated
        // surface and blank it out

        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setBackground(Color.GREEN);
//        g.fillRect(0, 0, screenWidth, screenHeight);

        int baseLen = 1;

        for(int i=0 ; i < tileList.size() ; i++){
            Sprite sprite = SpriteStore.get().getSprite("GUI/Images/"+tileList.get(i).tile.getVal()+".png");

            sprite.draw(g, tileList.get(i).getPositioninScreen());
        }

        int markerSize = 10;
        int size = SpriteStore.getSpriteSize()/2 - markerSize/2;
        g.setColor(Color.BLUE);
        g.fillRect(xZero+size,yZero+size,markerSize,markerSize);//(xZero - baseLen, yZero -baseLen,xZero+baseLen,yZero+baseLen );//draw(new Rectangle(xZero-baseLen,yZero-baseLen,xZero+baseLen,yZero+baseLen));

        // finally, we've completed drawing so clear up the graphics
        // and flip the buffer over

        g.dispose();
        strategy.show();

    }

    /** Generating the screen **/
    public traxGUI(int size) {

        SpriteStore.setSpriteSize(size);

        updateZero();
        // create a frame to contain our game

        container = new JFrame("Proska's TRAX");

        // get hold the content of the frame

        JPanel panel = (JPanel) container.getContentPane();

        //set up the resolution of the game
        panel.setPreferredSize(new Dimension(screenWidth,screenHeight));
        panel.setLayout(null);

        // setup our canvas size and put it into the content of the frame
        window.setBounds(0, 0, screenWidth, screenHeight);
        window.setBackground(Color.GREEN);
        panel.add(window);


        // Closing Strategy
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tell AWT not to bother repainting our canvas since we're

        // going to do that our self in accelerated mode

        window.setIgnoreRepaint(true);

        // finally make the window visible

        container.pack();
//        container.setResizable(false);
        container.setVisible(true);

        // add a listener to respond to the user closing the window. If they

        // do we'd like to exit the game

        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics

        window.createBufferStrategy(2);
        strategy = window.getBufferStrategy();


        container.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                updateScreenDimension(container);

                updateZero();
//                System.out.println("("+container.getWidth()+","+container.getHeight()+")");
                redraw();

                try { Thread.sleep(1000); } catch (Exception d) {}
            }
            @Override
            public void componentHidden(ComponentEvent componentEvent) {

            }
            @Override
            public void componentShown(ComponentEvent componentEvent) {

            }
            @Override
            public void componentMoved(ComponentEvent componentEvent) {

            }
        });

        tileList = new LinkedList<tileGUI>();
    }

    private void updateScreenDimension(JFrame container) {
        screenWidth = container.getWidth();
        screenHeight = container.getHeight();
    }

    ///////////////////////////////////////////////////////////

    public void addTile(traxTiles tile , Coordinate pos){

        System.out.println("[INFO] Tile:"+tile+" Added to GUI in "+pos);

        tileList.add(new tileGUI(pos, tile));

        checkCorners(pos);

        redraw();

    }

    private void checkCorners(Coordinate pos) {
        if(pos.X() < -(int)(xZero/tileSize()) ){
            shiftAllRigth();

        }
        if(pos.Y() < -(int)((screenHeight - yZero)/tileSize())){
            extendHeight();
        }
        if(pos.X() >= (int)((screenWidth - xZero)/tileSize())){
            extendWidth();
        }
        if(pos.Y() >= (int)(yZero/tileSize())){
            shiftAllDown();

        }
    }

    private int tileSize(){
        return SpriteStore.getSpriteSize();
    }

    private void shiftAllRigth(){
        xZero += 5*tileSize();
        System.out.println("shiftAllRigth");
        System.out.println("New xZero="+xZero);
    }
    private void shiftAllDown(){
        yZero += 5*tileSize();
        System.out.println("shiftAllDown");
        System.out.println("New yZero="+yZero);
    }
    private void extendWidth(){
        screenWidth += 5*tileSize();
        System.out.println("Width Extended");
    }
    private void extendHeight(){
        screenHeight += 5*tileSize();
        System.out.println("Height Extended");
    }

}