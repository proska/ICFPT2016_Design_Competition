package Game.GeneticAlgorithmPlayer;

import java.util.ArrayList;

/**
 * Created by mohammad on 7/9/15.
 */
public class Path {
    ArrayList<Tile> path = new ArrayList<Tile> ();
    boolean closedPath = false;

    public void addToEnd (Tile a) {
        path.add(a);
    }

    public void addToStart (Tile a) {
        path.add(0, a);
    }

    public Tile getTile (int index) {
        return (Tile)path.get (index);
    }

    public Tile getFirst () {
        return (Tile)path.get (0);
    }

    public Tile getLast () {
        return (Tile)path.get (pathSize()-1);
    }

    public int pathSize () {
        return path.size();
    }

}
