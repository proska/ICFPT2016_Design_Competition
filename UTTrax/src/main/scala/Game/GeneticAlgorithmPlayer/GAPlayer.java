package Game.GeneticAlgorithmPlayer;

import Game.TestPlayer.Player;
import Game.TestPlayer.PlayerScala;
import Game.World.Coordinate;
import Game.World.Move;
import Game.World.traxTiles;

/**
 * Created by mohammad on 7/28/15.
 */
public class GAPlayer implements Player {//implements Player
    Map mainMap = new Map();
    boolean myColor = Tile.red;

    public GAPlayer(boolean myColor) {
        this.myColor = myColor;
    }

    @Override
    public void initialize() {

        update(new Move(traxTiles.WWBB , new Coordinate(0,0)) , false);
    }

    @Override
    public void update(Move move, Boolean reAction)  {
        char ch = '+';
        Tile.Direction dir = Tile.Direction.UP;
        boolean color = Tile.red;

        switch (move.TileType()) {
            case WWBB:
                ch = '+';color = Tile.white;
                break;
            case BBWW:
                ch = '+';color = Tile.red;
                break;
            case WBBW:
                ch = '/';color = Tile.white;
                break;
            case BWWB:
                ch = '/';color = Tile.red;
                break;
            case WBWB:
                ch = '\\';color = Tile.white;
                break;
            case BWBW:
                ch = '\\';color = Tile.red;
                break;
            default:
                System.out.println("Invalid Tile Type");
        }
        Tile newTile = new Tile(new Point(move.pos().X(), move.pos().Y()));
        newTile.setTile(ch, dir, color);
        mainMap.addTile(newTile);
        mainMap.autoFill();
    }

    @Override
    public Move play() {
        GA GeneticAlgorithmTraxPlayer = new GA (mainMap, myColor);
        Tile nextMoveTile = GeneticAlgorithmTraxPlayer.getNextMove();
        traxTiles tile;
        if (nextMoveTile.getTileChar() == '+') {
            if (nextMoveTile.dirColor[Tile.up] == Tile.white) {
                tile = traxTiles.WWBB;
            } else {
                tile = traxTiles.BBWW;
            }
        }
        else if (nextMoveTile.getTileChar() == '/') {
            if (nextMoveTile.dirColor[Tile.up] == Tile.white) {
                tile = traxTiles.WBBW;
            } else {
                tile = traxTiles.BWWB;
            }
        }
        else {
            if (nextMoveTile.dirColor[Tile.up] == Tile.white) {
                tile = traxTiles.WBWB;
            } else {
                tile = traxTiles.BWBW;
            }
        }
        Coordinate coor = new Coordinate(nextMoveTile.coordinate.getX(), nextMoveTile.coordinate.getY());
        Move nextMove = new Move(tile, coor);
        return nextMove;
    }
}
