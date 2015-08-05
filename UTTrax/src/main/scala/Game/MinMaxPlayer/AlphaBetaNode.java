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
    int alpha=-1000;
    int beta=1000;
    int Depth=0;
    int score=0;
    AlphaBetaNode parent;
    int Id;
    List<AlphaBetaNode> children= new ArrayList<AlphaBetaNode>();
    Move[] Mchild;

    public AlphaBetaNode(){}
    public AlphaBetaNode(gameState start){
        PeresentState =start;
    }
    public AlphaBetaNode(AlphaBetaNode c){
        this.PeresentState.apply(c.PeresentState);
        this.alpha=c.alpha;
        this.beta=c.beta;
        this.Depth=c.Depth;
        this.score=c.score;
        this.parent=c.parent;
        this.Id=c.Id;
        for(int i=0;i<c.children.size();i++)
            this.children.add(i, c.children.get(i));
        for(int i=0;i<c.Mchild.length ;i++)
            this.Mchild[i]=c.Mchild[i];


    }

    void setMchild(Move[] possibleMoves){
        for(int i=0;i<possibleMoves.length ;i++)
            this.Mchild[i]=possibleMoves[i];
    }
    void setParent (AlphaBetaNode parent){
        this.parent=parent;
         parent.setChild(this.Id,this);
    }
    void setChild(int i,AlphaBetaNode child){
        children.add(i,child);
        children.get(i).Id=i;
        children.get(i).parent=this;
        children.get(i).alpha=this.alpha;
        children.get(i).beta=this.beta;
    }
    void setId(int i){
        this.Id=i;
    }
    void setAlphaBeta(AlphaBetaNode parent,int score,traxColor turn){
        if (turn==traxColor.WHITE()){//////////////////////////////////////////////////?!
            if(score>parent.alpha){
                parent.alpha=score;
            }
        }
        else{
            if(score<parent.beta){
                parent.beta=score;
            }
        }

    }
    AlphaBetaNode returnParent(){
        return this.parent;
    }
    boolean prune(AlphaBetaNode s){
        if(s.alpha<=s.beta){
            return false; ///not pruning
        }
        else
            return true;
    }
    void setScore(){
        score=10;
    }//////////////////////////////////////////////////////////////////////
}
