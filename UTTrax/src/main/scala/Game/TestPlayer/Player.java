package Game.TestPlayer;

import Game.World.*;


/**
* Created by proska on 8/1/15.
*/
public interface Player {

    traxColor side = traxColor.WHITE;

    gameState state = new gameState();

    public void initialize();

    public Move play();

    public void update(Move move , Boolean reAction );

}