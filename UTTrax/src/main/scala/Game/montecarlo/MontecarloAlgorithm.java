package Game.montecarlo;
//import Game.TestPlayer.*;
import Game.TestPlayer.Player;
import Game.TestPlayer.moveFinder;
//import Game.TestPlayer.moveFinder$class;
import Game.World.*;


import java.lang.Boolean;
import java.util.Random;
import java.util.Vector;


import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

/**
 * Created by saina on 7/28/15.
 */
public class MontecarloAlgorithm implements Player {


    traxColor side;
    traxColor sidetmp;

    TreeNode root = new TreeNode();

    gameState state = null;

    public MontecarloAlgorithm(traxColor side) {
        this.side = side;
    }
    @Override
    public void update(Move move) {
        println("start");


        this.state.updateState(move, sidetmp, null);
        println("update done");
    }



    @Override
    public Move play() {
        Move move =  root.play(this.state);
        this.state.updateState(move,side,null);
        return move;
    }


    @Override
    public void initialize() {
        //state.whiteRoutes().apply(0) = new Route().apply(Coordinate.apply(0,0),true);
    }


    @Override
    public gameState getState() {
        return state;
    }

    @Override
    public void setState(gameState st) {
        state = gameState.apply(st);
    }


    class TreeNode  {
        Random r = new Random();

//        public Vector<stateMC> ConvertToState(scala.collection.immutable.List<Game.World.Move> allmoves,stateMC game){
//            Vector<stateMC> ret = new Vector<stateMC>();
//            int num = allmoves.size();
//            Game.World.Move[] moves = new Game.World.Move[num];
//
////                for (int i=0 ; i< allmoves.size ; i++)
////                {
////                    moves[i] = change (allmoves[i]);
////                }
//
//            for(int i = 0 ; i< allmoves.size(); i++){
//                stateMC n = new stateMC();
//                n = game;
//                n.pegah.updateState(allmoves.apply(i),side,null);
//                n.move = allmoves.apply(i);///be move khodeman change mikonad!!!!
//                ret.add(n);
//            }
//            return ret;
//        }



        /////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////

        //        private stateMC select(Vector <stateMC> saina ) {

//            stateMC selected = null;
//            double bestValue = Double.MIN_VALUE;
//            for (stateMC c : saina) {
//                double uctValue = c.score / (c.visit + epsilon) +
//                        Math.sqrt(Math.log(c.parent.visit+1) / (c.visit + epsilon)) +
//                        r.nextDouble() * epsilon;
//                // small random number to break ties randomly in unexpanded nodes
//                if (uctValue > bestValue) {
//                    selected = c;
//                    bestValue = uctValue;
//                }
//            }
//            return selected;
//        }
        public Move play(gameState st){
            int simcount = 10;
            stateMC root = createRoot(st);

            for(int co=0 ; co<simcount ; co++){

                stateMC tmpMCState = new stateMC(root); // Copy///pegah deleted!!!!


                stateMC selected = selection(tmpMCState);

                expansion(tmpMCState);

                int score = simulation(tmpMCState);


                backpropagation(root,selected,score);

            }
            Move finalmove = null;
            try {
                finalmove = (root.select()).move;
            } catch (Exception e) {
                e.printStackTrace();

            }

            return finalmove;


        }

        private stateMC selection(stateMC tmpMCState) {

            while(tmpMCState.isAllMArked()){
                tmpMCState = tmpMCState.select();
            }

            return tmpMCState ;///pegah deleted + new kardan ra ham bardashtim!!!!!!!
        }

        private void expansion(stateMC tmpMCState){

//            assert(tmpMCState.isAllMArked());

            scala.collection.immutable.List<Game.World.Move> allmoves =  moveFinder.giveAllPossibleMoves(tmpMCState.pegah, traxColor.flip(side));

            tmpMCState.setChildrenNum(allmoves.length());

            boolean flag = true;
            while(flag){
                int randVal = r.nextInt(allmoves.length());

                if(! (tmpMCState.isChildMarked[randVal])){

                    stateMC child = new stateMC(tmpMCState);///pegah deleted!!!
                    child.setParent(tmpMCState);
                    child.pegah.updateState(allmoves.apply(randVal),side,null);

                    child.setMove(allmoves.apply(randVal));

                    allmoves =  moveFinder.giveAllPossibleMoves(child.pegah, traxColor.flip(side));

                    child.setChildNumber(allmoves.length());

                    tmpMCState.children.add(randVal,child);

                    tmpMCState = tmpMCState.children.get(randVal);

                    //break;
                    flag = false;
                }
            }

        }

        private int simulation (stateMC tmpGame)
        {
            boolean B_iswon = isGameEnded(tmpGame.pegah.blackRoutes());
            boolean W_iswon = isGameEnded(tmpGame.pegah.whiteRoutes());

            Random e = new Random();

            while( !(B_iswon || W_iswon) )
            {

                scala.collection.immutable.List<Game.World.Move> allmoves = moveFinder.giveAllPossibleMoves(tmpGame.pegah, side);
                int r = e.nextInt(allmoves.size());

                tmpGame.pegah.updateState(allmoves.apply(r),side,null);

                B_iswon = isGameEnded(tmpGame.pegah.blackRoutes());
                W_iswon = isGameEnded(tmpGame.pegah.whiteRoutes());
            }

            boolean flagwor = side == traxColor.WHITE;

            if(B_iswon && flagwor){ ////farz bar in ast ke ma white hastim!white = 1
                return -1;

            }
            else if(flagwor && W_iswon) {
                return 1;
            }
            else if(W_iswon && !flagwor){
                return -1;
            }
            else if(!flagwor && B_iswon){
                return 1;
            }

            return 0;
        }

        private void backpropagation (stateMC root ,stateMC selected , int score){

//            assert ((root != selected) == (root != selected[0]));

            while(selected.parent != null) {
                selected.updateScore(score);
                selected = selected.parent;
            }
        }

        private boolean isGameEnded(scala.collection.immutable.List<Route> list) {
            boolean iswon = false;
            for (int i=0 ; i< list.length() ; i++)
            {
                if ( list.apply(i).isLoop() )
                    iswon = true;
            }
            return iswon;
        }

        private stateMC createRoot(gameState st){

            stateMC root = new stateMC(st);

            scala.collection.immutable.List<Game.World.Move> allmoves =  moveFinder.giveAllPossibleMoves(root.pegah, side);

            root.setChildNumber(allmoves.length());

            return root;
        }

    }

}
