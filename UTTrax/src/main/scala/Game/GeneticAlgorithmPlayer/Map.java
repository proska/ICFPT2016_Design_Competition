package Game.GeneticAlgorithmPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mohammad on 7/9/15.
 */
public class Map {

    ArrayList<Tile>  map;
    ArrayList<Point> nextMoveCadidates = new ArrayList<Point>();
    private ArrayList<Path> redPaths = new ArrayList<Path> ();
    private ArrayList<Path> whitePaths = new ArrayList<Path> ();
    boolean finishedMap = false;
    boolean winner;
    static int numOfFiles = 1;
    Path finishedPath = new Path ();
    static int fixedTileNumber = 0;
    int fitness = 0;

    public Map (){
        this.map = new ArrayList<Tile>();
    }

    public Map (ArrayList map) {
        this.map = new ArrayList<Tile>(map);
        creatPaths();
        findTileCandidates();
    }

    public void addTile (Tile newTile) {
        map.add(newTile);
    }

    public void fillRandom () {
        while (finishedMap == false) {
            addRandomTile();
            autoFill();
            creatPaths();
        }
    }

    public void autoFill () {
        Tile tile = map.get(map.size()-1);
        Point p = new Point(map.get(map.size()-1).coordinate);
        Point np = new Point(p);
        //Auto fill of right tile
        np.set(p);
        np.x++;
        if(findTile(np) == -1) {
            Tile.Direction newDir = Tile.Direction.UP;
            int numOfDirections = 0;
            int ti = findTile(new Point(np.getX()+1, np.getY()));
            int tin = 0;
            if (ti != -1 && map.get(ti).dirColor[Tile.left] == tile.dirColor[Tile.right]) {
                newDir = Tile.Direction.RIGHT; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX(), np.getY()+1));
            if (ti != -1 && map.get(ti).dirColor[Tile.up] == tile.dirColor[Tile.right]) {
                newDir = Tile.Direction.DOWN; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX(), np.getY()-1));
            if (ti != -1 && map.get(ti).dirColor[Tile.down] == tile.dirColor[Tile.right]) {
                newDir = Tile.Direction.UP; numOfDirections++; tin = ti;
            }

            if (numOfDirections == 1) {
                Tile newTile = new Tile(new Point(np));
                newTile.findAndSetTile(Tile.Direction.LEFT, newDir, tile.dirColor[Tile.right]);
                if (tin < fixedTileNumber) {
                    newTile.fixedAutoTile = true;
                }
                newTile.autoTile = true;
                map.add(newTile);
                autoFill();
            }
            //else if (numOfDirections > 1) {
            //    finishedMap = true;
            //}
        }

        //Auto fill of left tile
        np.set(p);
        np.x--;
        if(findTile(np) == -1) {
            Tile.Direction newDir = Tile.Direction.UP;
            int numOfDirections = 0;
            int ti = findTile(new Point(np.getX()-1, np.getY()));
            int tin = 0;
            if (ti != -1 && map.get(ti).dirColor[Tile.right] == tile.dirColor[Tile.left]) {
                newDir = Tile.Direction.LEFT; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX(), np.getY()+1));
            if (ti != -1 && map.get(ti).dirColor[Tile.up] == tile.dirColor[Tile.left]) {
                newDir = Tile.Direction.DOWN; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX(), np.getY()-1));
            if (ti != -1 && map.get(ti).dirColor[Tile.down] == tile.dirColor[Tile.left]) {
                newDir = Tile.Direction.UP; numOfDirections++; tin = ti;
            }

            if (numOfDirections == 1) {
                Tile newTile = new Tile(new Point(np));
                newTile.findAndSetTile(Tile.Direction.RIGHT, newDir, tile.dirColor[Tile.left]);
                if (tin < fixedTileNumber) {
                    newTile.fixedAutoTile = true;
                }
                newTile.autoTile = true;
                map.add(newTile);
                autoFill();
            }
            else if (numOfDirections > 1) {
                finishedMap = true;
            }
        }

        //Auto fill of down tile
        np.set(p);
        np.y++;
        if(findTile(np) == -1) {
            Tile.Direction newDir = Tile.Direction.UP;
            int numOfDirections = 0;
            int ti = findTile(new Point(np.getX()+1, np.getY()));
            int tin = 0;
            if (ti != -1 && map.get(ti).dirColor[Tile.left] == tile.dirColor[Tile.down]) {
                newDir = Tile.Direction.RIGHT; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX(), np.getY()+1));
            if (ti != -1 && map.get(ti).dirColor[Tile.up] == tile.dirColor[Tile.down]) {
                newDir = Tile.Direction.DOWN; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX()-1, np.getY()));
            if (ti != -1 && map.get(ti).dirColor[Tile.right] == tile.dirColor[Tile.down]) {
                newDir = Tile.Direction.LEFT; numOfDirections++; tin = ti;
            }

            if (numOfDirections == 1) {
                Tile newTile = new Tile(new Point(np));
                newTile.findAndSetTile(Tile.Direction.UP, newDir, tile.dirColor[Tile.down]);
                if (tin < fixedTileNumber) {
                    newTile.fixedAutoTile = true;
                }
                newTile.autoTile = true;
                map.add(newTile);
                autoFill();
            }
            else if (numOfDirections > 1) {
                finishedMap = true;
            }
        }

        //Auto fill of up tile
        np.set(p);
        np.y--;
        if(findTile(np) == -1) {
            Tile.Direction newDir = Tile.Direction.UP;
            int numOfDirections = 0;
            int ti = findTile(new Point(np.getX()+1, np.getY()));
            int tin = 0;
            if (ti != -1 && map.get(ti).dirColor[Tile.left] == tile.dirColor[Tile.up]) {
                newDir = Tile.Direction.RIGHT; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX(), np.getY()-1));
            if (ti != -1 && map.get(ti).dirColor[Tile.down] == tile.dirColor[Tile.up]) {
                newDir = Tile.Direction.UP; numOfDirections++; tin = ti;
            }
            ti = findTile(new Point(np.getX()-1, np.getY()));
            if (ti != -1 && map.get(ti).dirColor[Tile.right] == tile.dirColor[Tile.up]) {
                newDir = Tile.Direction.LEFT; numOfDirections++; tin = ti;
            }

            if (numOfDirections == 1) {
                Tile newTile = new Tile(new Point(np));
                newTile.findAndSetTile(Tile.Direction.DOWN, newDir, tile.dirColor[Tile.up]);
                if (tin < fixedTileNumber) {
                    newTile.fixedAutoTile = true;
                }
                newTile.autoTile = true;
                map.add(newTile);
                autoFill();
            }
            else if (numOfDirections > 1) {
                finishedMap = true;
            }
        }


    }

    public void addRandomTile () {
        if (map.size() == 0) {
            Tile newTile = new Tile (new Point(0,0));
            while(!newTile.setTile(getRandomTileChar(), Tile.Direction.UP, Tile.white, Tile.Direction.RIGHT, Tile.red));
            map.add(newTile);
        }
        else {
            ArrayList<Point> tileCadidate = new ArrayList<Point>();
            for (int tileIndex = 0; tileIndex < map.size(); tileIndex++) {
                Point p = new Point (map.get(tileIndex).coordinate);
                Point np = new Point(p);
                np.x++;
                if (findTile(np) == -1 && !tileCadidate.contains(np)) {
                    tileCadidate.add(new Point(np));
                }
                np.set(p);
                np.x--;
                if (findTile(np) == -1 && !tileCadidate.contains(np)) {
                    tileCadidate.add(new Point(np));
                }
                np.set(p);
                np.y++;
                if (findTile(np) == -1 && !tileCadidate.contains(np)) {
                    tileCadidate.add(new Point(np));
                }
                np.set(p);
                np.y--;
                if (findTile(np) == -1 && !tileCadidate.contains(np)) {
                    tileCadidate.add(new Point(np));
                }
            }
            Point newTilePoint = new Point(tileCadidate.get((int) (Math.random() * tileCadidate.size())));
            Tile.Direction[] newDir = new Tile.Direction[4];
            boolean[] newColor = new boolean[4];
            int numOfDirections = 0;
            int ti;

            Point np;
            np = new Point(newTilePoint);
            np.x++;
            ti = findTile(np);
            if (ti != -1) {
                newDir[numOfDirections] = Tile.Direction.RIGHT;
                newColor[numOfDirections] = map.get(ti).dirColor[Tile.left];
                numOfDirections++;
            }
            np.set(newTilePoint);
            np.x--;
            ti = findTile(np);
            if (ti != -1) {
                newDir[numOfDirections] = Tile.Direction.LEFT;
                newColor[numOfDirections] = map.get(ti).dirColor[Tile.right];
                numOfDirections++;
            }
            np.set(newTilePoint);;
            np.y++;
            ti = findTile(np);
            if (ti != -1) {
                newDir[numOfDirections] = Tile.Direction.DOWN;
                newColor[numOfDirections] = map.get(ti).dirColor[Tile.up];
                numOfDirections++;
            }
            np.set(newTilePoint);
            np.y--;
            ti = findTile(np);
            if (ti != -1) {
                newDir[numOfDirections] = Tile.Direction.UP;
                newColor[numOfDirections] = map.get(ti).dirColor[Tile.down];
                numOfDirections++;
            }

            Tile newTile = new Tile(newTilePoint);

            if (numOfDirections == 1) {
                newTile.setTile(getRandomTileChar(), newDir[0], newColor[0]);
            } else {
                while (!newTile.setTile(getRandomTileChar(), newDir[0], newColor[0], newDir[1], newColor[1])) ;
            }
            map.add(newTile);
        }


    }

    public void findTileCandidates () {
        for (int tileIndex = 0; tileIndex < map.size(); tileIndex++) {
            Point p = new Point (map.get(tileIndex).coordinate);
            Point np = new Point(p);
            np.x++;
            if (findTile(np) == -1 && !nextMoveCadidates.contains(np)) {
                nextMoveCadidates.add(new Point(np));
            }
            np.set(p);
            np.x--;
            if (findTile(np) == -1 && !nextMoveCadidates.contains(np)) {
                nextMoveCadidates.add(new Point(np));
            }
            np.set(p);
            np.y++;
            if (findTile(np) == -1 && !nextMoveCadidates.contains(np)) {
                nextMoveCadidates.add(new Point(np));
            }
            np.set(p);
            np.y--;
            if (findTile(np) == -1 && !nextMoveCadidates.contains(np)) {
                nextMoveCadidates.add(new Point(np));
            }
        }
    }

    public char getRandomTileChar() {
        short randNum = (short)(Math.random()*3);
        char ch = '+';
        switch (randNum){
            case 0: ch = '+'; break;
            case 1: ch = '/'; break;
            case 2: ch = '\\'; break;
        }
        return ch;
    }

    public void creatPaths () {
        redPaths.clear();
        whitePaths.clear();
        boolean [] tileRedConsidered = new boolean[map.size()];
        boolean [] tileWhiteConsidered = new boolean[map.size()];

        for (int tileIndex=0 ; tileIndex<map.size() ; tileIndex++) {
            tileRedConsidered [tileIndex] = false;
            tileWhiteConsidered [tileIndex] = false;
        }

        for (int tileIndex=0 ; tileIndex<map.size() ; tileIndex++) {
            Tile tile = map.get(tileIndex);
            //for red path
            if (!tileRedConsidered [tileIndex]) {
                tileRedConsidered [tileIndex] = true;
                Path newPath = new Path ();
                newPath.addToEnd(map.get(tileIndex));
                Tile.Direction dir1 = Tile.Direction.UP;
                Tile.Direction dir2 = Tile.Direction.UP;
                if (tile.getTileChar() == '+') {
                    if (tile.dirColor[Tile.up] == Tile.red) {
                        dir1 = Tile.Direction.UP; dir2 = Tile.Direction.DOWN;
                    } else {
                        dir1 = Tile.Direction.RIGHT; dir2 = Tile.Direction.LEFT;
                    }
                } else if (tile.getTileChar() == '/') {
                    if (tile.dirColor[Tile.up] == Tile.red) {
                        dir1 = Tile.Direction.UP; dir2 = Tile.Direction.LEFT;
                    } else {
                        dir1 = Tile.Direction.DOWN; dir2 = Tile.Direction.RIGHT;
                    }
                } else if (tile.getTileChar() == '\\') {
                    if (tile.dirColor[Tile.up] == Tile.red) {
                        dir1 = Tile.Direction.UP; dir2 = Tile.Direction.RIGHT;
                    } else {
                        dir1 = Tile.Direction.DOWN; dir2 = Tile.Direction.LEFT;
                    }
                }
                boolean closedPath = followPath (newPath, tileIndex, dir1, true, tileRedConsidered);
                if (closedPath) {
                    finishedMap = true;
                    winner = Tile.red;
                    finishedPath = newPath;
                } else {
                    followPath(newPath, tileIndex, dir2, false, tileRedConsidered);
                }

                redPaths.add(newPath);
            }

            //for white path
            if (!tileWhiteConsidered [tileIndex]) {
                tileWhiteConsidered [tileIndex] = true;
                Path newPath = new Path ();
                newPath.addToEnd(map.get(tileIndex));
                Tile.Direction dir1 = Tile.Direction.UP;
                Tile.Direction dir2 = Tile.Direction.UP;
                if (tile.getTileChar() == '+') {
                    if (tile.dirColor[Tile.up] == Tile.white) {
                        dir1 = Tile.Direction.UP; dir2 = Tile.Direction.DOWN;
                    } else {
                        dir1 = Tile.Direction.RIGHT; dir2 = Tile.Direction.LEFT;
                    }
                } else if (tile.getTileChar() == '/') {
                    if (tile.dirColor[Tile.up] == Tile.white) {
                        dir1 = Tile.Direction.UP; dir2 = Tile.Direction.LEFT;
                    } else {
                        dir1 = Tile.Direction.DOWN; dir2 = Tile.Direction.RIGHT;
                    }
                } else if (tile.getTileChar() == '\\') {
                    if (tile.dirColor[Tile.up] == Tile.white) {
                        dir1 = Tile.Direction.UP; dir2 = Tile.Direction.RIGHT;
                    } else {
                        dir1 = Tile.Direction.DOWN; dir2 = Tile.Direction.LEFT;
                    }
                }
                boolean closedPath = followPath (newPath, tileIndex, dir1, true, tileWhiteConsidered);
                if (closedPath) {
                    finishedMap = true;
                    winner = Tile.white;
                    finishedPath = newPath;
                } else {
                    followPath(newPath, tileIndex, dir2, false, tileRedConsidered);
                }

                whitePaths.add(newPath);

            }


        }
    }

    private boolean followPath (Path path, int tileIndex, Tile.Direction dir, boolean endOfPath, boolean[] tileConsidered) {
        Point p = new Point (map.get (tileIndex).coordinate);
        int newTileIndex;
        if (dir == Tile.Direction.UP) {
            p.y--;
        } else if (dir == Tile.Direction.DOWN) {
            p.y++;
        } else if (dir == Tile.Direction.RIGHT) {
            p.x++;
        } else {
            p.x--;
        }
        newTileIndex = findTile(p);
        while (newTileIndex != -1) {
            if (endOfPath) {
                path.addToEnd(map.get (newTileIndex));
            } else {
                path.addToStart(map.get(newTileIndex));
            }
            tileConsidered[newTileIndex] = true;
            dir = map.get(newTileIndex).follow(dir);
            if (dir == Tile.Direction.UP) {
                p.y--;
            } else if (dir == Tile.Direction.DOWN) {
                p.y++;
            } else if (dir == Tile.Direction.RIGHT) {
                p.x++;
            } else {
                p.x--;
            }
            newTileIndex = findTile(p);
            if (newTileIndex == tileIndex) {
                path.closedPath = true;
                return true;
            }
        }
        return false;
    }

    public int findTile (Point c ) {
        for (int i=0 ; i<map.size() ; i++) {
            if (c.x == (map.get(i).coordinate).x && c.y == (map.get(i).coordinate).y  ) {
                return i;
            }
        }
        return (-1);
    }

    public int getFitness (boolean playerColor) {
        int cost = 0;
        boolean flag = false;

        for (int i=0 ; i<finishedPath.pathSize() ; i++){
            Point p = new Point(finishedPath.path.get(i).coordinate);
            int index = findTile(p);
            if (index >= fixedTileNumber && !finishedPath.path.get(i).fixedAutoTile ) {
                cost++;
            }
            for (int j = 0; j < nextMoveCadidates.size(); j++) {
                if (p.x == nextMoveCadidates.get(j).getX() && p.y == nextMoveCadidates.get(j).getY()) {
                    flag = true;
                }
            }
        }

        if (!flag){
            cost = cost + 10;
        }

        if (this.winner == playerColor) {
            if (cost == 1) {
                fitness = 10000;
            } else if (cost == 2) {
                fitness = 100;
            } else if (cost == 3) {
                fitness = 30;
            } else if (cost == 4) {
                fitness = 18;
            } else if (cost == 5) {
                fitness = 14;
            } else if (cost == 6) {
                fitness = 12;
            } else if (cost == 0) {
                System.out.println("Error Cost = 0");
                fitness = 10;
            } else {
                fitness = 10;
            }
        } else {
            if (cost == 1) {
                fitness = -20000;
            } else if (cost == 2) {
                fitness = -5000;
            } else if (cost == 3) {
                fitness = -60;
            } else if (cost == 4) {
                fitness = -24;
            } else if (cost == 5) {
                fitness = -15;
            } else if (cost == 6) {
                fitness = -12;
            } else if (cost == 0) {
                System.out.println("Error Cost = 0");
                fitness = -10;
            } else {
                fitness = -10;
            }
        }
        return fitness;
    }


    public void writeToFile () {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Maps/map" + numOfFiles + ".trx"));
            numOfFiles++;
            writer.write ("Trax\n");
            int minX = 0;
            int minY = 0;
            int number = 1;
            for (int tileIndex=0 ; tileIndex<map.size() ; tileIndex++) {
                if (!map.get(tileIndex).autoTile) {
                    int x = map.get(tileIndex).coordinate.x - minX;
                    int y = map.get(tileIndex).coordinate.y - minY;
                    char xchar = (char) (x + 64);
                    writer.write("  " + number + " " + xchar + y + map.get(tileIndex).getTileChar() + "\n");
                    number++;

                    if (x == 0) {
                        minX--;
                    }
                    if (y == 0) {
                        minY--;
                    }
                }
            }
            writer.write("#");
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromFile () {
        map.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader("test.trx"));
            int minX = 0;
            int minY = 0;
            String line = br.readLine();
            line = br.readLine();
            while (line.charAt(0) != '#') {
                line = line.trim();
                int index1 = line.indexOf(' ');
                int index2;
                if (line.charAt(index1+3) >= '0' && line.charAt(index1+3) <= '9') {
                    index2 = index1 + 4;
                } else {
                    index2 = index1 + 3;
                }
                char charX = line.charAt(index1+1);
                String stringY = line.substring(index1+2, index2);
                int x = charX - 64;
                int y = Integer.parseInt(stringY);
                Point p = new Point(x + minX, y + minY);
                char tileChar = line.charAt(index2);
                addTile(p, tileChar);
                autoFill();

                line = br.readLine();

                if (x == 0) {
                    minX--;
                }
                if (y == 0) {
                    minY--;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addTile (Point p, char ch) {
        Tile newTile;
        newTile = new Tile(new Point(p));
        if (p.getX() == 0 && p.getY() == 0) {
            newTile.setTile(ch, Tile.Direction.UP, Tile.white);
        }
        else {
            Tile.Direction dir = Tile.Direction.UP;
            boolean color = Tile.red;
            int ti;
            if ((ti = findTile(new Point(p.getX()+1,p.getY()))) != -1) {
                dir = Tile.Direction.RIGHT;
                color = map.get(ti).dirColor[Tile.left];
            } else if ((ti = findTile(new Point(p.getX()-1,p.getY()))) != -1) {
                dir = Tile.Direction.LEFT;
                color = map.get(ti).dirColor[Tile.right];
            } else if ((ti = findTile(new Point(p.getX(),p.getY()+1))) != -1) {
                dir = Tile.Direction.DOWN;
                color = map.get(ti).dirColor[Tile.up];
            } else if ((ti = findTile(new Point(p.getX(),p.getY()-1))) != -1) {
                dir = Tile.Direction.UP;
                color = map.get(ti).dirColor[Tile.down];
            } else {
                System.out.println("Error in reading file");
            }
            newTile.setTile(ch, dir, color);

        }
        map.add(newTile);
    }


}

