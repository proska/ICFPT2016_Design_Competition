package testCodes.doubleBufferTest;


import GUI.traxGUI;
import Game.World.Coordinate;
import Game.World.traxTiles;

/**
 * Created by proska on 7/30/15.
 */
public class doubleBufTest {

    public static void main(String[] a) {

        traxGUI gui = new traxGUI(30);

        int lim = 100;
        testerGUI(gui, lim);

    }

    private static void testerGUI(traxGUI gui, int lim) {
        for(int i=0 ;  i<lim ; i++){
            Coordinate pos = new Coordinate(0,-i);

            gui.addTile(traxTiles.BWBW, pos);


            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }
}


