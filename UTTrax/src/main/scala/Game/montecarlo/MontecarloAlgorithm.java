//package Game.montecarlo;
////import Game.TestPlayer.*;
//import Game.TestPlayer.Player;
//import Game.TestPlayer.moveFinder;
//import Game.World.Move;
//import Game.World.gameState;
//import Game.World.traxColor;
//import Game.World.*;
//import scala.*;
//import scala.Enumeration;
//
//import java.lang.Boolean;
//import java.util.LinkedList;
//import java.util.*;
//import java.util.Random;
///**
// * Created by saina on 7/28/15.
// */
//public class MontecarloAlgorithm implements moveFinder,Player {
//
//    gameState state ;
//    traxColor side;
//
//    @Override
//    public void update(Move move, Boolean reAction) {
//
//    }
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
//    public class TreeNode  {
//            Random r = new Random();
//            int nActions = 5;
//            double epsilon = 1e-6;
//            TreeNode[] children;
//            double nVisits, totValue;
//
////            public class Move{
////                int x;
////                int y;
////                int tile;
////            }
//            public class state{
//                state(){
//                }
//                state (state p){
//                    parent = p;
//                }
//                state parent;
//                Vector<state> children;
//                int is_marked;
//                int score;
//                int visit;
//                boolean win;
//                Move move;
//                gameState pegah;
//
//            }
//            public class coordinate{
//                int x;
//                int y;
//                int dir;
//
//            }
//            public class Rout{
//                coordinate start;
//                coordinate end;
//            }
//
//            public Vector<state> ConvertToState(Move[] allmoves,state game){
//                Vector<state> ret = new Vector<state>();
//
//                for(int i = 0 ; i< allmoves.length; i++){
//                    state n = new state();
//                    n = game;
//                    n.pegah.updateState(allmoves[i]);
//                    n.move = allmoves[i];
//                    ret.add(n);
//                }
//                return ret;
//            }
//
//            public state simulation (state game)
//            {
//                Vector <state> moves = new Vector<state> ();
//                Move[] allmoves ;
//                state last = new state();
//                int r =0;
//                boolean flag1 = false;
//                boolean flag2 = false;
//                boolean W_iswon = false;
//                boolean B_iswon = false;
//                while( !flag1 && !flag2 )
//                {
//                    for (int i=0 ; i<game.pegah.blackRoutes().length() ; i++)
//                    {
//                        if ( (game.pegah).blackRoutes().apply(i).isLoop() )
//                            B_iswon = true;
//                    }
//                    for (int i=0 ; i<game.pegah.whiteRoutes().length() ; i++)
//                    {
//                        if ( game.pegah.whiteRoutes().apply(i).isLoop() )
//                            W_iswon = true;
//                    }
//
//                    if (!(B_iswon))
//                    {
//                        allmoves = giveAllPossibleMoves(game.pegah ,traxColor.WHITE);
//                        moves = ConvertToState(allmoves,game);
//                        Random e = new Random();
//                        r = e.nextInt(moves.size()-0);
//                        (moves.get(r)).is_marked = 1;
//                        game = moves.get(r);
//
//                    }
//                    else
//                    {
//                        flag2 = true;
//                        last = moves.get(r);
//
//                    }
//
//                    if (!(W_iswon))
//                    {
//                        allmoves = giveAllPossibleMoves(game.pegah , traxColor.BLACK);
//                        moves = ConvertToState(allmoves,game);
//                        Random e = new Random();
//                        r = e.nextInt(moves.size()-0);
//                        (moves.get(r)).is_marked = 1;
//                        game = moves.get(r);
//
//                    }
//                    else
//                    {
//                        flag1 = true;
//                        last = moves.get(r);
//
//                    }
//                }
//                return last;
//            }
//
//
//            /////////////////////////////////////////////////////////////////
//
//            public void backpropagation (state last,state[] first,boolean flagwor){
//                //boolean flagwor; //flagi ke maloom mikonand ma white hastim ya red!if white flag=1 else flag = 0;
//                while(first[0] != last) {
//                    last = last.parent;
//                    if (last == first[1])
//                    {
//                        first[1].visit++;
//
//                        boolean W_iswon = false;
//                        boolean B_iswon = false;
//
//                        for (int i=0 ; i<last.pegah.blackRoutes().length() ; i++)
//                        {
//                            if ( (last.pegah).blackRoutes().apply(i).isLoop() )
//                                B_iswon = true;
//                        }
//                        for (int i=0 ; i<last.pegah.whiteRoutes().length() ; i++)
//                        {
//                            if ((last.pegah.whiteRoutes().apply(i)).isLoop() )
//                                W_iswon = true;
//                        }
//
//                        /////score
//                        if(B_iswon && flagwor){ ////farz bar in ast ke ma white hastim!white = 1
//                            first[1].score = -1;
//
//                        }
//                        else if(flagwor && W_iswon) {
//                            first[1].score = 1;
//                        }
//                        else if(W_iswon && !flagwor){
//                            first[1].score = -1;
//                        }
//                        else if(!flagwor && B_iswon){
//                            first[1].score = 1;
//                        }
//
//
//                    }
//                }
//                first[0].visit++;
//
//
//
//
//            }
///////////////////////////////////////////////////////////////////
//
//            private state select(Vector <state> saina ) {
//                state selected = null;
//                double bestValue = scala.Double.MIN_VALUE;
//                for (state c : saina) {
//                    double uctValue = c.score / (c.visit + epsilon) +
//                            Math.sqrt(Math.log(nVisits+1) / (c.visit + epsilon)) +
//                            r.nextDouble() * epsilon;
//                    // small random number to break ties randomly in unexpanded nodes
//                    if (uctValue > bestValue) {
//                        selected = c;
//                        bestValue = uctValue;
//                    }
//                }
//                return selected;
//            }
//            public Move main(String []args){
//                int simcount = 100000;
//                int counter = 0;
//                int flagsel = 0;
//                boolean color = true;
//                state lastgame = new state(); //= new state[];
//                state firstgame = new state();
//                state selected = new state();
//                state finalmove = new state();
//                selected = firstgame;
//                state[] pegah = new state[2];
//                pegah[0] = firstgame;
//                pegah[1] = selected;
//
//                for(int co=0 ; co<simcount ; co++){
//                    flagsel = 0;
//                    lastgame = simulation(selected);
//                    counter++;
//                    backpropagation(lastgame,pegah,color);
//                    for(state c:firstgame.children){
//                        if(c.is_marked == 0){
//                            flagsel = 1;
//                        }
//                    }
//                    if(flagsel == 0){
//                        selected = select(firstgame.children);
//                        pegah[1]=selected;
//                    }
//                }
//                finalmove = select(firstgame.children);
//                return finalmove.move;
//
//
//            }
//
//
//
//        }
//
//
//
//
//}
