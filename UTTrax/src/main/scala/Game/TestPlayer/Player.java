package Game.TestPlayer;

import Game.World.*;
import scala.Boolean;


/**
* Created by proska on 8/1/15.
*/
public interface Player {

    traxColor side = null;

//    gameState state = new gameState();

    public void initialize();

    public Move play();

    public void update(Move move);

    public gameState getState();
    public void setState(gameState st);
}

