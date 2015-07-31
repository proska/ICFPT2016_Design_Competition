package Game.MinMaxPlayer;
import Game.World.*;
import Game.TestPlayer.*;
import com.sun.xml.internal.bind.WhiteSpaceProcessor;

/**
 * Created by sahand on 7/27/15.
 */
public class AlphaBeta implements moveFinder {
    @Override
//    public boolean isPow2(int in) {
//        return false;//super.isPow2(in);
//    }
//
//    Move AlphaBetaGen(AlphaBetaNode state, traxColor turn, int d){
//	    int Depth=4;
//    	boolean bw=false;
//        boolean ww=false;
//	    Functions f=new Functions();
//	    boolean AlphaBetaF=true;///alphabeta or minimax
//	    int score=0 ;
//        int finalsocre=0;
//
//	   	Move[] allmoves=this.giveAllPossibleMoves(state.PeresentState, turn);//////////////////////?
//        state.setMchild(allmoves);
//        if (turn==traxColor.WHITE)
//    	    ww=state.PeresentState.whiteRoutes().isLoop();
//        else
//            bw=state.PeresentState.blackRoutes().isLoop();
//       	for(int j=0;j<state.Mchild.length;j++) {
//		    if (AlphaBetaF && (state.alpha > state.beta)) {
//    			break;
//	    	}
//		    else {
//		    	while (d < Depth && ww== false && bw==false) {
//		    		AlphaBetaNode newstate =new AlphaBetaNode();
//                    newstate=state;
//                    newstate.PeresentState.updateState(state.Mchild[j],turn);
//                    state.setChild(j,newstate);
//			    	state=state.children.get(j);
//			    	d = d + 1;
//                    turn = traxColor.flip(turn);///////////////////////?;
//		    		AlphaBetaGen(state, turn,d);
//		    	}
//                d = d - 1;
//                if(d==4) {//////////////////////////////////////////////////score!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                        score=state.score()
//                    }
//                else {
//                    for (int i = 0; i < state.children.size(); i++) {
//                        if (score < state.children.get(i).score){
//                            score=state.children.get(i).score;
//                    }
//                }
//                }
//                state=state.returnParent();
//			    state.setAlphaBeta(state,turn);
//	    	}
//	    }
//        int finalscore=0;
//        int finalid=0;
//        for(int i=0;i<state.children.size();i++){
//            if (finalscore < state.children.get(i).score){
//                finalscore=state.children.get(i).score;
//                finalid=i;
//            }
//        }
//        return state.Mchild[finalid];
//    }
}

