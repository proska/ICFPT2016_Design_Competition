package Game.GeneticAlgorithmPlayer;

import java.util.ArrayList;

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

    public boolean equalPoint (Point p) {
        if (this.x == p.x && this.y == p.y) {
            return true;
        }
        return false;
    }

    public boolean existIn (ArrayList <Point> arrayP) {
        for (int i=0; i<arrayP.size(); i++) {
            if (this.equalPoint(arrayP.get(i))) {
                return true;
            }
        }
        return false;
    }

    public int getX () {
        return this.x;
    }

    public int getY () {
        return this.y;
    }
}
