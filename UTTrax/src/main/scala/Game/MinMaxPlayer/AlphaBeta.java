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
	    int Depth=4;
    	boolean bw=false;
        boolean ww=false;
//        Functions f=new Functions();
	    boolean AlphaBetaF=false;///alphabeta or minimax
	    int score=0 ;
        int finalsocre=0;

	   	scala.collection.immutable.List<Game.World.Move> allmoves= moveFinder.giveAllPossibleMoves(state.PeresentState, turn);//////////////////////?
        state.setMchild(allmoves);
        WinCheck winCheck = new WinCheck(state, turn, bw, ww).invoke();
        ww = winCheck.isWw();
        bw = winCheck.isBw();
        for(int j=0;j<state.Mchild.size();j++) {
            int MchSize = state.Mchild.size();
            Move mch = state.Mchild.get(j);
            gameState tmpstate = gameState.apply(state.PeresentState);
            tmpstate.updateState(state.Mchild.get(j), turn, null);
            AlphaBetaNode newstate = new AlphaBetaNode(tmpstate,state);
            state.setChild(j, newstate);
            state = state.children.get(j);
            d = d + 1;
            turn = traxColor.flip(turn);
            allmoves = moveFinder.giveAllPossibleMoves(state.PeresentState, turn);
            state.setMchild(allmoves);
            for (int k = 0; k < state.Mchild.size(); k++) {
                Move mch1 = state.Mchild.get(k);
                gameState tmpstate1 = gameState.apply(state.PeresentState);
                tmpstate1.updateState(state.Mchild.get(k), turn, null);
                AlphaBetaNode newstate1 = new AlphaBetaNode(tmpstate1,state);
                state.setChild(k, newstate1);
                state = state.children.get(k);
                d = d + 1;
                turn = traxColor.flip(turn);
                allmoves = moveFinder.giveAllPossibleMoves(state.PeresentState, turn);
                state.setMchild(allmoves);
                for (int m = 0; m < state.Mchild.size(); m++) {
                    Move mch2 = state.Mchild.get(m);
                    gameState tmpstate2 = gameState.apply(state.PeresentState);
                    tmpstate2.updateState(state.Mchild.get(m), turn, null);
                    AlphaBetaNode newstate2 = new AlphaBetaNode(tmpstate2,state);
                    state.setChild(m, newstate2);
                    state = state.children.get(m);
                    d = d + 1;
                    turn = traxColor.flip(turn);
                    allmoves = moveFinder.giveAllPossibleMoves(state.PeresentState, turn);
                    state.setMchild(allmoves);
                    for (int n = 0; n < state.Mchild.size(); n++) {
                        Move mch3 = state.Mchild.get(n);
                        score = state.scoreGen(mch3, state.PeresentState, ourcolor);
                        state.setAlphaBeta(state, score, turn, ourcolor);
                    }
                    state.setScore(turn, ourcolor);
                    state = state.returnParent();
                    d = d - 1;
                    turn = traxColor.flip(turn);
                }
                score=0;
                for (int i = 0; i < state.children.size(); i++) {
                    if (score < state.children.get(i).score) {
                        score = state.children.get(i).score;
                        state.setAlphaBeta(state, score, turn, ourcolor);

                    }
                }
                state.setScore(turn, ourcolor);
                state = state.returnParent();
                d = d - 1;
                turn = traxColor.flip(turn);
            }
            score=0;
            for (int i = 0; i < state.children.size(); i++) {
                if (score < state.children.get(i).score) {
                    score = state.children.get(i).score;
                    state.setAlphaBeta(state, score, turn, ourcolor);
                }
                state.setScore(turn, ourcolor);
                state = state.returnParent();
                d = d - 1;
                turn = traxColor.flip(turn);
            }
            state.setScore(turn, ourcolor);
            turn = traxColor.flip(turn);
        }
        int finalid=0;
        for(int i=0;i<state.children.size();i++){
            if (finalsocre < state.children.get(i).score){
                finalsocre=state.parent.children.get(i).score;
                finalid=i;
            }
        }
        return state.Mchild.get(finalid);
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



//    Move AlphaBetaGen(AlphaBetaNode state, traxColor turn, int d){
//	    int Depth=4;
//    	boolean bw=false;
//        boolean ww=false;
//	    Functions f=new Functions();
//	    boolean AlphaBetaF=false;///alphabeta or minimax
//	    int score=0 ;
//        int finalsocre=0;
//
//	   	scala.collection.immutable.List<Game.World.Move> allmoves= moveFinder.giveAllPossibleMoves(state.PeresentState, turn);//////////////////////?
//        state.setMchild(allmoves);
//        WinCheck winCheck = new WinCheck(state, turn, bw, ww).invoke();
//        ww = winCheck.isWw();
//        bw = winCheck.isBw();
//        while(state.j<state.Mchild.size()){// for(j<state.Mchild.size();j++) {
//		    if (AlphaBetaF && (state.alpha > state.beta)) {
//    			break;
//	    	}
//		    else {
//		    	while (d < Depth && ww== false && bw==false) {
//                    int MchSize=state.Mchild.size();
//                    Move mch=state.Mchild.get(state.j);
//                    gameState tmpstate = gameState.apply(state.PeresentState);
//                    tmpstate.updateState(state.Mchild.get(state.j),turn, null);
//                    AlphaBetaNode newstate =new AlphaBetaNode(tmpstate);
//                    state.setChild(state.j,newstate);
//			    	state=state.children.get(state.j);
//			    	d = d + 1;
//                    turn = traxColor.flip(turn);
//		    		AlphaBetaGen(state, turn, d);
//		    	}
//
//                if(d==Depth) {
//                        score=state.scoreGen(state.Mchild.get(state.j),state.PeresentState,ourcolor);
//                        state.setAlphaBeta(state,score,turn,ourcolor);
//                    }
//                else {
//                    for (int i = 0; i < state.children.size(); i++) {
//                        if (score < state.children.get(i).score){
//                            score=state.children.get(i).score;
//                            state.setAlphaBeta(state,score,turn,ourcolor);
//
//                        }
//                }
//                }
//
//	    	}
//            state.j=state.j+1;
//
//	    }
//        state.setScore(turn,ourcolor);
//        state=state.returnParent();
//        d = d - 1;
//        turn = traxColor.flip(turn);
//        int finalid=0;
//
//
//        for(int i=0;i<state.parent.parent.parent.parent.parent.children.size();i++){
//            if (finalsocre < state.parent.parent.parent.parent.children.get(i).score){
//                finalsocre=state.parent.parent.parent.parent.children.get(i).score;
//                finalid=i;
//            }
//        }
//        return state.parent.parent.parent.Mchild.get(finalid);
//    }

