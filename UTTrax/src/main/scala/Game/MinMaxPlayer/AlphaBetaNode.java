package Game.MinMaxPlayer;

import java.util.ArrayList;
import java.util.List;

import Game.World.*;
import Game.World.gameState;

/**
 * Created by sahand on 7/15/15.
 */
public class AlphaBetaNode {
    /////
    gameState PeresentState;
    int alpha = -1000;
    int beta = 1000;
    int Depth = 0;
    int score = 0;
    int chScore=0;
    int j=0;
    AlphaBetaNode parent = this;
    int Id;
    List<AlphaBetaNode> children = new ArrayList<AlphaBetaNode>();
    List<Move> Mchild = new ArrayList<Move>();

    public AlphaBetaNode() {
    }
    public AlphaBetaNode(gameState start) {
        PeresentState = start;
        AlphaBetaNode parent = this;

    }
    public AlphaBetaNode(gameState start,AlphaBetaNode p) {
        PeresentState = start;
        AlphaBetaNode parent = this;
        this.alpha=p.alpha;
        this.beta=p.beta;


    }
    public AlphaBetaNode(AlphaBetaNode c) {
        this.PeresentState.apply(c.PeresentState);
        this.alpha = c.alpha;
        this.beta = c.beta;
        this.Depth = c.Depth;
        this.score = c.score;
        this.parent = c.parent;
        this.chScore=c.chScore;
        this.j=c.j;
        this.Id = c.Id;
        for (int i = 0; i < c.children.size(); i++)
            this.children.add(i, c.children.get(i));
        for (int i = 0; i < c.Mchild.size(); i++)
            this.Mchild.add(i, c.Mchild.get(i));


    }

    void setMchild(scala.collection.immutable.List<Game.World.Move> possibleMoves) {
        for (int i = 0; i <possibleMoves.size(); i++)/*possibleMoves.size()*/
            this.Mchild.add(i, possibleMoves.apply(i));
    }

    void setParent(AlphaBetaNode parent) {
        this.parent = parent;
        parent.setChild(this.Id, this);
    }

    void setChild(int i, AlphaBetaNode child) {
        children.add(i, child);
        children.get(i).Id = i;
        children.get(i).parent = this;
        children.get(i).alpha = this.alpha;
        children.get(i).beta = this.beta;
    }

    void setId(int i) {
        this.Id = i;
    }

    void setAlphaBeta(AlphaBetaNode state, int score, traxColor turn,traxColor ourcolor) {
        if (turn == ourcolor) {
            if (chScore > state.alpha) {
                state.alpha = chScore;
            }
        } else {
            if (chScore < parent.beta) {
                state.beta = chScore;
            }
        }

    }

    AlphaBetaNode returnParent() {
        return this.parent;
    }

    boolean prune(AlphaBetaNode s) {
        if (s.alpha <= s.beta) {
            return false; ///not pruning
        } else
            return true;
    }

    void setScore(traxColor turn,traxColor ourColor) {
        if (turn == ourColor) {
                score=alpha;
            }
        else {
                score=beta;
            }
    }
    int scoreGen(Move m, gameState t, traxColor ourColor) {
        score=0;
        gameState s=null;
        s=gameState.apply(t);

        for (int i = 0; i < s.whiteRoutes().length(); i++) {
            if (s.whiteRoutes().apply(i).isLoop()) {
                if (ourColor == traxColor.WHITE)
                    chScore = 100;
                else
                    chScore = -100;
            }
            else if(s.whiteRoutes().apply(i).length()==1){
                if (ourColor == traxColor.WHITE)
                    chScore += 5;
                else
                    chScore -= 5;
            }
        }
        for (int i = 0; i < s.blackRoutes().length(); i++) {
            if (s.blackRoutes().apply(i).isLoop()) {
                if (ourColor == traxColor.BLACK)
                    chScore = 100;
                else
                    chScore = -100;
            }
            else if(s.blackRoutes().apply(i).length()==1){
                if (ourColor == traxColor.BLACK)
                    chScore += 5;
                else
                    chScore -= 5;
            }
        }
        return chScore;
    }
}


