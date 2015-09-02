package Game.montecarlo;
//import Game.TestPlayer.*;
import Game.TestPlayer.Player;
import Game.TestPlayer.moveFinder;
//import Game.TestPlayer.moveFinder$class;
import Game.World.*;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;


import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

/**
 * Created by saina on 7/28/15.
 */
public class MontecarloAlgorithm implements Player {


    traxColor side;
    traxColor sidetmp;

    public TreeNode root = new TreeNode();

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
        long startTime = System.currentTimeMillis();
        Move move =  root.play(this.state);
        this.state.updateState(move,side,null);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        println("total time in mili seconds is : ");
        System.out.println(totalTime);
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

        private void dumpMove(Move move){
            try {
                Files.write(Paths.get("myfile.txt"), (move.toString()+"\n").getBytes() , StandardOpenOption.APPEND);
//                System.out.println("[DUMP] Move:"+move);
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }

        stateMC root = null;

        public Move play(gameState st){


            root = createRoot(st);

//            tmpMCState = selected;
            int simcount = 1000;//root.children.size();

            for(int co=0 ; co<simcount ; co++){

                clearDumpFile();

                stateMC tmpMCState = root;//new stateMC(root);
                stateMC selected = root;//new stateMC(root);
                //tmpMCState =new  stateMC(root); // Copy///pegah deleted!!!!

                /*stateMC*/ tmpMCState = selection(tmpMCState);

                selected = expansion(tmpMCState);
                tmpMCState = selected;

                int score = simulation(tmpMCState);

                backpropagation(selected,score);

                System.out.println(co);
            }
            Move finalmove = null;

            finalmove = (root.select()).move;


            return finalmove;


        }

        private void clearDumpFile() {

            try {
                Files.delete(Paths.get("myfile.txt"));
                Files.copy(Paths.get("dump.txt"), Paths.get("myfile.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Files.write(Paths.get("myfile.txt"), ("").getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private stateMC selection(stateMC tmpMCState) {

            while(tmpMCState.isAllMArked()){
                stateMC tmp = tmpMCState.select();

                if(isGameEnded(tmpMCState.pegah.blackRoutes()) || isGameEnded(tmpMCState.pegah.whiteRoutes())){
                    tmp = root.select();
                    while(true){
                        tmpMCState = root.children.get(r.nextInt(root.children.size()));
                        if(!tmpMCState.pegah.isEqual(tmp.pegah)){
                            break;
                        }
                    }

                } else {
                    tmpMCState = tmp;
                }
                dumpMove(tmpMCState.move);
            }



            return tmpMCState ;///pegah deleted + new kardan ra ham bardashtim!!!!!!!
        }

        private stateMC expansion(stateMC tmpMCState){

            assert(tmpMCState.isAllMArked()):"Flag 1";

            scala.collection.immutable.List<Game.World.Move> allmoves =  moveFinder.giveAllPossibleMoves(tmpMCState.pegah, traxColor.flip(side));

            assert(allmoves.length() > 0):"end of game reached";

            boolean flag = true;
            while(flag){
                int randVal = r.nextInt(allmoves.length());

                if(! (tmpMCState.isChildMarked[randVal])){

                    tmpMCState.isChildMarked[randVal] = true;

                    stateMC child = new stateMC(tmpMCState);///pegah deleted!!!
                    child.setParent(tmpMCState);

                    child.pegah.updateState(allmoves.apply(randVal),side,null);

                    child.setMove(allmoves.apply(randVal));

                    allmoves =  moveFinder.giveAllPossibleMoves(child.pegah, traxColor.flip(side));

                    child.setChildNumber(allmoves.length());

                    assert(tmpMCState.children.get(randVal) == null):"I don't know why!";

                    tmpMCState.children.remove(randVal);/////removing at first

                    tmpMCState.children.add(randVal,child);

                    tmpMCState = tmpMCState.children.get(randVal);

                    //break;
                    flag = false;

                    dumpMove(child.move);
                }
            }
            return  tmpMCState;
        }

        private int simulation (stateMC tmpGame)
        {

            stateMC help = new stateMC(tmpGame);

            boolean B_iswon = isGameEnded(help.pegah.blackRoutes());
            boolean W_iswon = isGameEnded(help.pegah.whiteRoutes());

            Random e = new Random();

            int cnt = 0;

            while( !(B_iswon || W_iswon) )
            {
                cnt +=1;
                scala.collection.immutable.List<Game.World.Move> allmoves = moveFinder.giveAllPossibleMoves(help.pegah, side);
                int r = e.nextInt(allmoves.size());

                boolean res = true;
                try {
                    dumpMove(allmoves.apply(r));
                    res = help.pegah.updateState(allmoves.apply(r),side,null);
                } catch (Exception e1) {
                    help.pegah.dump();
                    println(allmoves.apply(r).toString());
                    println(e1.getMessage());
//                    e1.printStackTrace();
                }

                if(!res){
                    return -1;
                } else {
                    B_iswon = isGameEnded(help.pegah.blackRoutes());
                    W_iswon = isGameEnded(help.pegah.whiteRoutes());
                }
            }

            boolean flagwor = side == traxColor.WHITE;

            if(B_iswon && flagwor){
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

        private void backpropagation (stateMC selected , int score){

//            assert ((root != selected) == (root != selected[0]));

            while(true) {
                selected.updateScore(score);
                if (selected.parent == null){break;}
                selected = selected.parent;

            }

        }

        public boolean isGameEnded(scala.collection.immutable.List<Route> list) {
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

//            root.rootSetChildrenNum(allmoves.length());

            return root;
        }

    }

}
