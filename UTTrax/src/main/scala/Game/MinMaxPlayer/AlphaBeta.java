package Game.MinMaxPlayer;
import Game.World.*;
import Game.TestPlayer.*;

/**
 * Created by sahand on 7/27/15.
 */
public class AlphaBeta implements moveFinder {
    @Override
    public boolean isPow2(int in) {
        return false;//super.isPow2(in);
    }

    Move AlphaBetaGen(AlphaBetaNode state, traxColor turn, int d){
	    int Depth=4;
    	boolean bw=false;
        boolean ww=false;
	    Functions f=new Functions();
	    boolean AlphaBetaF=true;///alphabeta or minimax
	    int score=0 ;
        int finalsocre=0;

	   	Move[] allmoves=this.giveAllPossibleMoves(state.PeresentState, turn);//////////////////////?
        state.setMchild(allmoves);
        WinCheck winCheck = new WinCheck(state, turn, bw, ww).invoke();
        ww = winCheck.isWw();
        bw = winCheck.isBw();
        for(int j=0;j<state.Mchild.length;j++) {
		    if (AlphaBetaF && (state.alpha > state.beta)) {
    			break;
	    	}
		    else {
		    	while (d < Depth && ww== false && bw==false) {
		    		AlphaBetaNode newstate =new AlphaBetaNode(state);
                    newstate.PeresentState.updateState(state.Mchild[j],turn);
                    state.setChild(j,newstate);
			    	state=state.children.get(j);
			    	d = d + 1;
                    turn = traxColor.flip(turn);
		    		AlphaBetaGen(state, turn, d);
		    	}
                d = d - 1;
                if(d==4) {//////////////////////////////////////////////////score!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        score=state.score()
                    }
                else {
                    for (int i = 0; i < state.children.size(); i++) {
                        if (score < state.children.get(i).score){
                            score=state.children.get(i).score;
                    }
                }
                }
                state=state.returnParent();
			    state.setAlphaBeta(state,turn);/////////////////////////////////////////
                state.setScore();
	    	}
	    }
        int finalid=0;
        for(int i=0;i<state.children.size();i++){
            if (finalsocre < state.children.get(i).score){
                finalsocre=state.children.get(i).score;
                finalid=i;
            }
        }
        return state.Mchild[finalid];
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

