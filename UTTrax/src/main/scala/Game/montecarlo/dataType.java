package Game.montecarlo;

import Game.World.Move;
import Game.World.gameState;

import java.util.Vector;

/**
 * Created by saina on 8/5/15.
 */
class stateMC {
    stateMC(){
    }

    stateMC(gameState st){

        pegah = gameState.apply(st);

        parent = null;
        children = new Vector<stateMC>();
        is_marked = 0;

        score = 0;
        visit = 0;
        move = null;
    }


    stateMC(stateMC p){
        parent = p;
    }
    stateMC parent;
    Vector<stateMC> children;
    int is_marked;
    int score;
    int visit;
//    boolean win;
    Move move;
    gameState pegah;

}

class coordinate{
    int x;
    int y;
    int dir;

}
class Rout{
    coordinate start;
    coordinate end;
}