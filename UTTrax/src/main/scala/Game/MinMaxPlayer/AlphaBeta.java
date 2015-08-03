//package Game.MinMaxPlayer;
//import Game.World.*;
//import Game.TestPlayer.*;
//import com.sun.xml.internal.bind.WhiteSpaceProcessor;
//import scala.Enumeration;
//
///**
// * Created by sahand on 7/27/15.
// */
//public class AlphaBeta implements moveFinder,Player {
//
//    gameState state ;
//    traxColor side;
//
//    @Override
//    public Move play() {
//        return null;
//    }
//
//
//    @Override
//    public void initialize() {
//
//    }
//
//    @Override
//    public void update(Move move, Boolean reAction) {
//
//    }
//
//    void AlphaBetaGen(AlphaBetaNode state, traxColor turn){
//	    int Depth=4;
//    	int w=0;
//	    int d=0;////////////////static??????????????
//        Functions f=new Functions();
//	    boolean AlphaBetaF=true;///alphabeta or minimax
//	    int[] score ;
//        int finalsocre=0;
//
//	   	Game.World.Move[] allmoves=this.giveAllPossibleMoves(state.PeresentState, turn);//////////////////////?
//        state.setMchild(allmoves);
//    	w=winDetector(state.PeresentState);
//       	for(int j=0;j<state.Mchild.length;j++) {
//		    if (AlphaBetaF && (state.alpha > state.beta)) {
//    			break;
//	    	}
//		    else {
//		    	while (d < Depth && w != 10) {
//		    		AlphaBetaNode newState =new AlphaBetaNode();
//                    newState= statusUpdate(state, state.Mchild[j]);
//                    state.setChild(j,newState);
//			    	state=state.children.get(j);
//			    	d = d + 1;
//                    turn = traxColor.flip(turn);///////////////////////?;
//		    		AlphaBetaGen(state, turn);
//		    	}
//                d = d - 1;
//                if(d==4) {//////////////////////////////////////////////////score!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                    for (int k = 0; k<allmoves.length;k++)
//                      for (int m = 0; m<allmoves.length;m++) {
//                        for (int n=0;n<allmoves.length;n++)
//                            score[k][m] = f.scoreGenerator(state.children.get(k).PeresentState.whiteRoutes().get(m),state.children.get(k).PeresentState.whiteRoutes().get(n));
//                    }
//                }
//                state=state.returnParent();
//			    state.setAlphaBeta(state,turn);
//	    	}
//	    }
//    }
//}
//
