package Game.MinMaxPlayer;
import Game.World.*;
import Game.TestPlayer.*;
import scala.*;
import scala.Boolean;
import scala.xml.Null;

/**
 * Created by sahand on 7/27/15.
 */
public class AlphaBeta implements Player  {

    traxColor side = null;
    traxColor ourcolor = null;
    gameState state = null;

    public AlphaBeta(traxColor side) {
        this.side = side;
        this.ourcolor=side;
    }

    @Override
    public void initialize() {

    }

    @Override
    public gameState getState() {
        return state;
    }

    @Override
    public void setState(gameState st) {
        state = gameState.apply(st);
    }

    @Override
    public Move play() {
        Move z=this.AlphaBetaGen(new AlphaBetaNode(state) , side ,0);
        this.update(z);
        return z;
    }

    @Override
    public void update(Move move) {
        state.updateState(move,side,null);
    }


    Move AlphaBetaGen(AlphaBetaNode state, traxColor turn, int d){
	    int Depth=2;
    	boolean bw=false;
        boolean ww=false;
	    Functions f=new Functions();
	    boolean AlphaBetaF=true;///alphabeta or minimax
	    int score=0 ;
        int finalsocre=0;

	   	scala.collection.immutable.List<Game.World.Move> allmoves= moveFinder.giveAllPossibleMoves(state.PeresentState, turn);//////////////////////?
        state.setMchild(allmoves);
        WinCheck winCheck = new WinCheck(state, turn, bw, ww).invoke();
        ww = winCheck.isWw();
        bw = winCheck.isBw();
        for(int j=0;j<state.Mchild.size();j++) {
		    if (AlphaBetaF && (state.alpha > state.beta)) {
    			break;
	    	}
		    else {
		    	while (d < Depth && ww== false && bw==false) {
                    int MchSize=state.Mchild.size();
                    Move mch=state.Mchild.get(j);
                    gameState tmpstate = gameState.apply(state.PeresentState);
                    tmpstate.updateState(state.Mchild.get(j),turn, null);
                    AlphaBetaNode newstate =new AlphaBetaNode(tmpstate);
                    state.setChild(j,newstate);
			    	state=state.children.get(j);
			    	d = d + 1;
                    turn = traxColor.flip(turn);
		    		AlphaBetaGen(state, turn, d);
		    	}

                if(d==2) {
//                    score=state.scoreGen();
                        score=state.scoreGen(state.Mchild.get(j),state.PeresentState,ourcolor);
                        state.setAlphaBeta(state,score,turn,ourcolor);
                    }
                else {
                    for (int i = 0; i < state.children.size(); i++) {
                        if (score < state.children.get(i).score){
                            score=state.children.get(i).score;
                    }
                }
                }

	    	}

	    }
        state.setScore(turn,ourcolor);
        state=state.returnParent();
        d = d - 1;
        turn = traxColor.flip(turn);
        int finalid=0;


        for(int i=0;i<state.parent.children.size();i++){
            if (finalsocre < state.parent.children.get(i).score){
                finalsocre=state.parent.children.get(i).score;
                finalid=i;
            }
        }
        return state.parent.Mchild.get(finalid);
    }

    public class WinCheck {
        private AlphaBetaNode state;
        private traxColor turn;
        private boolean bw;
        private boolean ww;

        public WinCheck(AlphaBetaNode state, traxColor turn, boolean bw, boolean ww) {
            this.state = state;
            this.turn = turn;
            this.bw = bw;
            this.ww = ww;
        }

        public boolean isBw() {
            return bw;
        }

        public boolean isWw() {
            return ww;
        }

        public WinCheck invoke() {
            if (turn== traxColor.WHITE) {
                for (int i = 0; i < state.PeresentState.whiteRoutes().size(); i++) {
                    ww = state.PeresentState.whiteRoutes().apply(i).isLoop();
                    if(ww)
                        break;
                }
            }
            else
            {
                for (int i = 0; i < state.PeresentState.blackRoutes().size(); i++) {
                    bw = state.PeresentState.blackRoutes().apply(i).isLoop();
                    if(bw)
                        break;
                }
            }
            return this;
        }
    }
}

