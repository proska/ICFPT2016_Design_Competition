package Game.montecarlo;

import Game.World.Move;
import Game.World.gameState;

import java.util.Random;
import java.util.Vector;

/**
 * Created by saina on 8/5/15.
 */
class stateMC {

    final static double epsilon = 1e-6;

    static Random r = new Random();

    stateMC(){
    }

    stateMC(gameState st){

        pegah = gameState.apply(st);

        parent = null;
        children = new Vector<stateMC>();
//        isChildMarked = new Boolean[](false);

        score = 0;
        visit = 0;
        move = null;
    }

    public void setChildNumber(int num){ // TODO call this func for root node
        isChildMarked = new boolean[num];

        for(int i=0 ; i<num ; i++)
            isChildMarked[i] = false;

    }

    public void setMove(Move move){
        this.move = move;
    }

    ////////////////////
    public void setParent(stateMC p)
    {
        this.parent = p;
    }

    public void setChildrenNum (int num)
    {
        stateMC MC = new stateMC(this);
         for (int i=0 ; i<num ; i++)
         {
             this.children.add(i,MC);
         }

    }
    ///////////////////

    stateMC(stateMC p){ // Copy Contructor // TODO Change
        pegah = gameState.apply(p.pegah);

        this.parent = p.parent;////this added
        children = new Vector<stateMC>();
        for(int i = 0 ; i < p.children.size() ; i++){
            this.children.add(i,p.children.get(i));
        }

        this.isChildMarked = new boolean[p.isChildMarked.length];
        for (int i=0 ; i<p.isChildMarked.length ; i++)
            this.isChildMarked[i] = false;


        score = 0;
        visit = 0;
        move = null;


    }

    public void updateScore(int score){
        this.score += score;
        this.visit += 1;
    }

    stateMC parent;
    Vector<stateMC> children;
    boolean[] isChildMarked;
    int score;
    int visit;
//    boolean win;
    Move move;
    gameState pegah;

    public boolean isAllMArked(){
        boolean all_isnot_marked = false;
        for( int i = 0  ; i < isChildMarked.length ; i++){
            if(!isChildMarked[i]){
                all_isnot_marked = true;
            }
        }
        return !all_isnot_marked;
    }

    public stateMC select(){
        stateMC selected = null;
        double bestValue = Double.MIN_VALUE;
        for (stateMC c : this.children) {
            double uctValue = c.score / (c.visit + epsilon) +
                    Math.sqrt(Math.log(c.parent.visit+1) / (c.visit + epsilon)) +
                    r.nextDouble() * epsilon;
            // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        return selected;
    }

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