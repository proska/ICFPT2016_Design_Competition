package Game.montecarlo;
//import Game.TestPlayer.*;
import Game.TestPlayer.Player;
import Game.TestPlayer.moveFinder;
//import Game.TestPlayer.moveFinder$class;
import Game.World.*;


import java.lang.Boolean;
import java.lang.Double;
import java.util.Random;
import java.util.Vector;






import java.lang.String;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

/**
* Created by saina on 7/28/15.
*/
public class MontecarloAlgorithm implements Player {


    traxColor side;
    traxColor sidetmp;

    TreeNode root = new TreeNode();

    public MontecarloAlgorithm(traxColor side) {
        this.side = side;
    }
    @Override
    public void update(Move move, Boolean reAction) {
        println("start");
        reAction = true;
        if(!reAction)
            sidetmp = side;
        else
            sidetmp = side.flip(side);


        this.state.updateState(move, sidetmp, null);
        println("update done");
    }



    @Override
    public Move play() {
        return root.play(this.state);
    }


    @Override
    public void initialize() {
    //state.whiteRoutes().apply(0) = new Route().apply(Coordinate.apply(0,0),true);





    }




    class TreeNode  {
        Random r = new Random();
        int nActions = 5;
        double epsilon = 1e-6;
        TreeNode[] children;
        double nVisits, totValue;

        public stateMC stateMC2;

        public Vector<stateMC> ConvertToState(scala.collection.immutable.List<Game.World.Move> allmoves,stateMC game){
            Vector<stateMC> ret = new Vector<stateMC>();
            int num = allmoves.size();
            Game.World.Move[] moves = new Game.World.Move[num];

//                for (int i=0 ; i< allmoves.size ; i++)
//                {
//                    moves[i] = change (allmoves[i]);
//                }

            for(int i = 0 ; i< allmoves.size(); i++){
                stateMC n = new stateMC();
                n = game;
                n.pegah.updateState(allmoves.apply(i),side,null);
                n.move = allmoves.apply(i);///be move khodeman change mikonad!!!!
                ret.add(n);
            }
            return ret;
        }

        public stateMC simulation (stateMC game)
        {
            Vector <stateMC> moves = new Vector<stateMC> ();
            scala.collection.immutable.List<Game.World.Move> allmoves ;
            stateMC last = new stateMC();

            int r =0;
            boolean flag1 = false;
            boolean flag2 = false;
            boolean W_iswon = false;
            boolean B_iswon = false;
            while( !flag1 && !flag2 )
            {
                for (int i=0 ; i<game.pegah.blackRoutes().length() ; i++)
                {
                    if ( (game.pegah).blackRoutes().apply(i).isLoop() )
                        B_iswon = true;
                }
                for (int i=0 ; i<game.pegah.whiteRoutes().length() ; i++)
                {
                    if ( game.pegah.whiteRoutes().apply(i).isLoop() )
                        W_iswon = true;
                }

                if (!(B_iswon))
                {

                    allmoves = moveFinder.giveAllPossibleMoves(game.pegah, side);
                    moves = ConvertToState(allmoves,game);
                    Random e = new Random();
                    r = e.nextInt(moves.size()-0);
                    (moves.get(r)).is_marked = 1;
                    game = moves.get(r);

                }
                else
                {
                    flag2 = true;
                    last = moves.get(r);

                }

                if (!(W_iswon))
                {
                    allmoves = moveFinder.giveAllPossibleMoves(game.pegah, traxColor.flip(side));
                    moves = ConvertToState(allmoves,game);
                    Random e = new Random();
                    r = e.nextInt(moves.size()-0);
                    (moves.get(r)).is_marked = 1;
                    game = moves.get(r);

                }
                else
                {
                    flag1 = true;
                    last = moves.get(r);

                }
            }
            return last;
        }


        /////////////////////////////////////////////////////////////////

        public void backpropagation (stateMC last,stateMC[] first,boolean flagwor){
            //boolean flagwor; //flagi ke maloom mikonand ma white hastim ya red!if white flag=1 else flag = 0;
            while(first[0] != last) {
                last = last.parent;
                if (last == first[1])
                {
                    first[1].visit++;

                    boolean W_iswon = false;
                    boolean B_iswon = false;

                    for (int i=0 ; i<last.pegah.blackRoutes().length() ; i++)
                    {
                        if ( (last.pegah).blackRoutes().apply(i).isLoop() )
                            B_iswon = true;
                    }
                    for (int i=0 ; i<last.pegah.whiteRoutes().length() ; i++)
                    {
                        if ((last.pegah.whiteRoutes().apply(i)).isLoop() )
                            W_iswon = true;
                    }

                    /////score
                    if(B_iswon && flagwor){ ////farz bar in ast ke ma white hastim!white = 1
                        first[1].score = -1;

                    }
                    else if(flagwor && W_iswon) {
                        first[1].score = 1;
                    }
                    else if(W_iswon && !flagwor){
                        first[1].score = -1;
                    }
                    else if(!flagwor && B_iswon){
                        first[1].score = 1;
                    }


                }
            }
            first[0].visit++;




        }
/////////////////////////////////////////////////////////////////

        private stateMC select(Vector <stateMC> saina ) {
            stateMC selected = null;
            double bestValue = Double.MIN_VALUE;
            for (stateMC c : saina) {
                double uctValue = c.score / (c.visit + epsilon) +
                        Math.sqrt(Math.log(nVisits+1) / (c.visit + epsilon)) +
                        r.nextDouble() * epsilon;
                // small random number to break ties randomly in unexpanded nodes
                if (uctValue > bestValue) {
                    selected = c;
                    bestValue = uctValue;
                }
            }
            return selected;
        }
        public Move play(gameState st){
            int simcount = 100000;
            int counter = 0;
            int flagsel = 0;
            boolean color = true;
            stateMC lastgame = new stateMC(); //= new state[];
            stateMC firstgame = new stateMC(st);
            stateMC selected = new stateMC();
            stateMC finalmove = new stateMC();
            selected = firstgame;
            stateMC[] pegah = new stateMC[2];
            pegah[0] = firstgame;
            pegah[1] = selected;

            for(int co=0 ; co<simcount ; co++){
                flagsel = 0;
                lastgame = simulation(selected);
                counter++;
                backpropagation(lastgame,pegah,color);
                for(stateMC c:firstgame.children){
                    if(c.is_marked == 0){
                        flagsel = 1;
                    }
                }
                if(flagsel == 0){
                    selected = select(firstgame.children);
                    pegah[1]=selected;
                }
            }
            finalmove = select(firstgame.children);
            return finalmove.move;


        }



    }

}
