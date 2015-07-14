package Game.GeneticAlgorithmPlayer;

/**
 * Created by mohammad on 7/9/15.
 */
public class Tile {
    enum Direction {UP, RIGHT, DOWN, LEFT}
    static  final int up = 0;
    static  final int right = 1;
    static  final int down = 2;
    static  final int left = 3;
    Point coordinate;
    private char tileChar;
    boolean[] dirColor;
    static final boolean red = true;
    static final boolean white = false;
    boolean autoTile = false;
    boolean fixedAutoTile = false;

    public Tile (Point coordinate ) {
        this.coordinate = coordinate;
        dirColor = new boolean [4];
    }

    public Point getCoordinate () {
        return this.coordinate;
    }

    public void findAndSetTile(Direction dir1, Direction dir2, boolean color) {
        if ((dir1 == Direction.UP && dir2 == Direction.RIGHT) || (dir1 == Direction.RIGHT && dir2 == Direction.UP) || (dir1 == Direction.DOWN && dir2 == Direction.LEFT) || (dir1 == Direction.LEFT && dir2 == Direction.DOWN)) {
            setTile('\\', dir1, color);
        }else if ((dir1 == Direction.UP && dir2 == Direction.LEFT) || (dir1 == Direction.LEFT && dir2 == Direction.UP) || (dir1 == Direction.DOWN && dir2 == Direction.RIGHT) || (dir1 == Direction.RIGHT && dir2 == Direction.DOWN)) {
            setTile('/', dir1, color);
        }else if ((dir1 == Direction.UP && dir2 == Direction.DOWN) || (dir1 == Direction.DOWN && dir2 == Direction.UP) || (dir1 == Direction.RIGHT && dir2 == Direction.LEFT) || (dir1 == Direction.LEFT && dir2 == Direction.RIGHT)) {
            setTile('+', dir1, color);
        }
    }

    public boolean setTile (char ch, Direction dir, boolean color) {
        this.tileChar = ch;

        if (tileChar == '+') {
            if (dir == Direction.UP || dir == Direction.DOWN) {
                dirColor [up] = color;
            } else {
                dirColor [up] = !color;
            }
            dirColor [right] = !dirColor [up];
            dirColor [down]  =  dirColor [up];
            dirColor [left]  = !dirColor [up];
        }
        else if (tileChar == '/') {
            if (dir == Direction.UP || dir == Direction.LEFT) {
                dirColor[up] = color;
            } else {
                dirColor[up] = !color;
            }
            dirColor[right] = !dirColor[up];
            dirColor[down] =  !dirColor[up];
            dirColor[left] =   dirColor[up];
        }
        else if (tileChar == '\\') {
            if (dir == Direction.UP || dir == Direction.RIGHT) {
                dirColor[up] = color;
            } else {
                dirColor[up] = !color;
            }
            dirColor[right] = dirColor[up];
            dirColor[down] = !dirColor[up];
            dirColor[left] = !dirColor[up];
        }
        return true;
    }

    public boolean setTile (char ch, Direction dir1, boolean color1, Direction dir2, boolean color2) {
        setTile(ch, dir1, color1);
        if (dirColor [dir2.ordinal()] == color2) {
            return true;
        } else {
            return false;
        }
    }

    public char getTileChar () {
        return this.tileChar;
    }
    
    public Direction follow(Direction in) {
        if (tileChar == '+') {
            if (in == Direction.UP) {
                return Direction.UP;
            }
            else if (in == Direction.DOWN) {
                return Direction.DOWN;
            }
            else if (in == Direction.RIGHT) {
                return Direction.RIGHT;
            }
            else {
                return Direction.LEFT;
            }
        }
        else if (tileChar == '/') {
            if (in == Direction.UP) {
                return Direction.RIGHT;
            }
            else if (in == Direction.DOWN) {
                return Direction.LEFT;
            }
            else if (in == Direction.RIGHT) {
                return Direction.UP;
            }
            else {
                return Direction.DOWN;
            }
        }
        else {
            if (in == Direction.UP) {
                return Direction.LEFT;
            }
            else if (in == Direction.DOWN) {
                return Direction.RIGHT;
            }
            else if (in == Direction.RIGHT) {
                return Direction.DOWN;
            }
            else {
                return Direction.UP;
            }
        }
    }


}
