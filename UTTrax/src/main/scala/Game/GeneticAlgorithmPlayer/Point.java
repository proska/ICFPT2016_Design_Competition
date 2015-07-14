package Game.GeneticAlgorithmPlayer;

/**
 * Created by mohammad on 7/9/15.
 */
public class Point {
    int x;
    int y;

    public Point () {}
    public Point (int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point (Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void set (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set (Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public int getX () {
        return this.x;
    }

    public int getY () {
        return this.y;
    }
}
