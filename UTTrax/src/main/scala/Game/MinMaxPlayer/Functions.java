package Game.MinMaxPlayer;

import Game.World.Margin;
import Game.World.Route;
import Game.World.traxTiles;
import Game.TestPlayer.moveFinder;

import java.lang.reflect.Array;

/**
 * Created by sahand on 7/13/15.
 */
public class Functions {

    Game.World.traxTiles w_tileGenerator(Route r){
        Game.World.traxTiles tile= traxTiles.INVALID;//not single tile
        if (Math.abs(r.start()._1().X() - r.end()._1().X()) == 1 && Math.abs(r.start()._1().Y() -r.end()._1().Y())==1){
            if((r.start()._2()== Margin.RIGHT() &&r.end()._2()==Margin.DOWN())||(r.end()._2()==Margin.TOP()&&r.start()._2()==Margin.DOWN()))
                tile= traxTiles.BWWB;
            if((r.start()._2()==Margin.LEFT()&&r.end()._2()==Margin.DOWN())||(r.end()._2()==Margin.LEFT()&&r.start()._2()==Margin.DOWN()))
                tile=traxTiles.BWBW;
            if((r.start()._2()==Margin.LEFT()&&r.end()._2()==Margin.TOP())||(r.end()._2()==Margin.LEFT()&&r.start()._2()==Margin.TOP()))
                tile=traxTiles.WBBW;
            if((r.start()._2()==Margin.RIGHT()&&r.end()._2()==Margin.TOP())||(r.end()._2()==Margin.RIGHT()&&r.start()._2()==Margin.TOP()))
                tile=traxTiles.WBWB;
        }
        else if (Math.abs(r.start()._1().X() - r.end()._1().X())==2 && r.start()._1().Y()==r.end()._1().Y())
            tile=traxTiles.BBWW;
        else if (Math.abs(r.start()._1().Y() - r.end()._1().Y())==2 && r.start()._1().X()==r.end()._1().X())
            tile=traxTiles.WWBB;
        return tile;
    }
   int scoreGenerator(Route s1,Route s2){
       int score=0;
       if((Math.abs(s1.start()._1().X()-s1.end()._1().X())==1 && s1.start()._1().Y()==s1.end()._1().Y() && s1.start()._2()==s1.end()._2()) || (Math.abs(s1.start()._1().Y()-s1.end()._1().Y())==1 && s1.start()._1().X()==s1.end()._1().X() && s1.start()._2()==s1.end()._2())) {
           score=3;
       }
       ///loop atack----1move

       if((Math.abs(s1.start()._1().X()-s1.end()._1().X())==2 && s1.start()._1().Y()==s1.end()._1().Y() && s1.start()._2()==s1.end()._2()) || (Math.abs(s1.start()._1().Y()-s1.end()._1().Y())==2 && s1.start()._1().X()==s1.end()._1().X() && s1.start()._2()==s1.end()._2())) {
           score=2;
       }
       ///loop atack----2moves

       traxTiles tile;
       tile = w_tileGenerator(s1);
       if(tile!=traxTiles.INVALID && tile!=traxTiles.BBWW && tile!=traxTiles.WWBB)
           score=2;
       //possible loop atack--- 2moves

       if((Math.abs(s1.start()._1().X()-s2.start()._1().X())==1 && s1.start()._1().Y()==s2.start()._1().Y() && s1.start()._2()==s2.start()._2()) || (Math.abs(s1.start()._1().X()-s2.end()._1().X())==1 && s1.start()._1().Y()==s2.end()._1().Y() && s1.start()._2()==s2.end()._2())
               ||(Math.abs(s1.start()._1().Y()-s2.start()._1().Y())==1 && s1.start()._1().X()==s2.start()._1().X() && s1.start()._2()==s2.start()._2()) || (Math.abs(s1.start()._1().Y()-s2.end()._1().Y())==1 && s1.start()._1().X()==s2.end()._1().X() && s1.start()._2()==s2.end()._2())
               ||(Math.abs(s1.end()._1().X()-s2.start()._1().X())==1 && s1.end()._1().Y()==s2.start()._1().Y() && s1.end()._2()==s2.start()._2()) || (Math.abs(s1.end()._1().X()-s2.end()._1().X())==1 && s1.end()._1().Y()==s2.end()._1().Y() && s1.end()._2()==s2.end()._2())
               ||(Math.abs(s1.end()._1().Y()-s2.start()._1().Y())==1 && s1.end()._1().X()==s2.start()._1().X() && s1.end()._2()==s2.start()._2()) || (Math.abs(s1.end()._1().Y()-s2.end()._1().Y())==1 && s1.end()._1().X()==s2.end()._1().X() && s1.end()._2()==s2.end()._2())) {
           score=2;
       }
       //possible loop atack--- 2moves
       return score;
   }
//    int lDetector(AlphaBetaNode[] s){
//        int r=0;
//        int size=s.length;
//        for(int i=0;i<size;i++){
//            for(int j=0;j<size;j++) {
//                if ( ((s[i].y-s[j].y)==1 && (s[i].x==s[j].x)) && s[i].tile=6 && s[j].tile==3  ) {
//                    for (int k = 0; k < size; k++) {
//                        if ((s[k].x - s[i].x) == 1 && (s[i].y - s[k].y) == 2 && s[k].tile == 6)
//                            r = r+1;
//                    }
//                }
//
//                if ( ((s[j].x-s[i].x)==1 && (s[i].y==s[j].y)) && s[i].tile=6 && s[j].tile==3  ) {
//                    for(int k=0; k<size; k++){
//                        if( (s[k].x-s[i].x)==2 && (s[k].y-s[i].y)==1 && s[k].tile==6)
//                            r=r+1;
//                    }
//                }
//
//                if ( ((s[i].x-s[j].x)==1 && (s[i].y==s[j].y)) && s[i].tile=6 && s[j].tile==3  ) {
//                    for(int k=0; k<size; k++){
//                        if( (s[i].x-s[k].x)==2 && (s[k].y-s[i].y)==1 && s[k].tile==6)
//                            r=r+1;
//                    }
//                }
//
//                if ( ((s[j].y-s[i].y)==1 && (s[i].x==s[j].x)) && s[i].tile=6 && s[j].tile==3  ) {
//                    for (int k = 0; k < size; k++) {
//                        if ((s[k].x - s[i].x) == 1 && (s[k].y - s[i].y) == 2 && s[k].tile == 6)
//                            r = r+1;
//                    }
//                }
//
//            }
//        }
//        return r;
//    }
//    int winDetector(AlphaBetaNode state) {
//        int wl = 0;
//        int rl =0;
//        int win = 0;
//        int w_size = state.PeresentState.whiteRoutes().size();
//        int r_size = state.PeresentState.blackRoutes().size();
//        int counter_w = 0;
//        int counter_r = 0;
//        int w_total=0;
//        int r_total=0;
//       //  wl = lDetector(ws);
//       // rl = lDetector(rs);
//        for (int i = 0; i < w_size; i++) {
////            if (state.PeresentState.whiteRoutes().get(i).win == 1) {
////                win = 10;
////                break;
////            }
////            Array[] a = state.PeresentState.whiteRoutes();
//              if(state.PeresentState.whiteRoutes().apply(i))/{/.get(i).score==3){
//                counter_w=counter_w+1;
//            }
//
//        }
//        for (int i = 0; i < r_size; i++) {
//            if (rs[i].win == 1) {
//                win = -10;
//                break;
//            }
//            else if(rs[i].score==3){
//                counter_r=counter_r+1;
//            }
//
//        }
//        w_total=counter_w+wl;
//        r_total=counter_r+rl;
//        if (w_total >= 2 && r_total == 0) {
//            win = 10;
//        }//ma 2 ta tak harekati va harif hichi
//        if(r_total>=2 && w_total==0){
//            win=-10;
//        }
//
//        if(r_total==1 ){
//            win=-5;
//        }
///////////shart haye dg ham mishe ezafe kard.
//        return win;
//    }


//    state autoMoves(state s){
//        int wsize= s.white.size();
//        int rsize=s.red.size();
//        route newroute;
//        for (int i=0;i<size;i++){
//            for(int j=0;j<size;j++){
//                if((s.white.get(i).start.x==s.white.get(j).start.x) && (s.white.get(i).start.y==s.white.get(j).start.y)){
//                    s.white.get(i).start.x=s.red.get(j).end.x;
//                    s.white.get(i).start.y=s.red.get(i).end.y;
//                    s.white.remove(j);
//                    if(ws1.dir=="left" && ws2.dir=="right"){
//                        newroute.start.x=s.white.get(i).start.x;
//                        newroute.start.y=s.white.get(i).start.y+1;
//                        newroute.start.dir="down";
//                        newroute.end.x=s.white.get(i).start.x;
//                        newroute.end.y=s.white.get(i).start.y-1;
//                        newroute.start.dir="up";
//                        s.red.add(newroute);
//                    }
//                    if(ws1.dir=="left" && ws2.dir=="down"){
//                        newroute.start.x=s.white.get(i).start.x;
//                        newroute.start.y=s.white.get(i).start.y+1;
//                        newroute.start.dir="down";
//                        newroute.end.x=s.white.get(i).start.x+1;
//                        newroute.end.y=s.white.get(i).start.y;
//                        newroute.start.dir="left";
//                        s.red.add(newroute);
//                    }
//                    if(ws1.dir=="left" && ws2.dir=="up"){
//                        newroute.start.x=s.white.get(i).start.x;
//                        newroute.start.y=s.white.get(i).start.y-1;
//                        newroute.start.dir="up";
//                        newroute.end.x=s.white.get(i).start.x+1;
//                        newroute.end.y=s.white.get(i).start.y;
//                        newroute.start.dir="left";
//                        s.red.add(newroute);
//                    }///1
//                    if(ws1.dir=="down" && ws2.dir=="right"){
//                        newroute.start.x=s.white.get(i).start.x-1;
//                        newroute.start.y=s.white.get(i).start.y;
//                        newroute.start.dir="right";
//                        newroute.end.x=s.white.get(i).start.x;
//                        newroute.end.y=s.white.get(i).start.y+1;
//                        newroute.start.dir="down";
//                        s.red.add(newroute);
//                    }
//                    if(ws1.dir=="down" && ws2.dir=="up"){
//                        newroute.start.x=s.white.get(i).start.x+1;
//                        newroute.start.y=s.white.get(i).start.y;
//                        newroute.start.dir="left";
//                        newroute.end.x=s.white.get(i).start.x-1;
//                        newroute.end.y=s.white.get(i).start.y;
//                        newroute.start.dir="right";
//                        s.red.add(newroute);
//                    }
//
//                    if(ws1.dir=="down" && ws2.dir=="left"){
//                        newroute.start.x=s.white.get(i).start.x;
//                        newroute.start.y=s.white.get(i).start.y+1;
//                        newroute.start.dir="down";
//                        newroute.end.x=s.white.get(i).start.x+1;
//                        newroute.end.y=s.white.get(i).start.y;
//                        newroute.start.dir="left";
//                        s.red.add(newroute);
//                    }/////2
//
//                    if(ws1.dir=="right" && ws2.dir=="up"){
//                        newroute.start.x=s.white.get(i).start.x-1;
//                        newroute.start.y=s.white.get(i).start.y;
//                        newroute.start.dir="right";
//                        newroute.end.x=s.white.get(i).start.x;
//                        newroute.end.y=s.white.get(i).start.y-1;
//                        newroute.start.dir="up";
//                        s.red.add(newroute);
//                    }
//                    if(ws1.dir=="right" && ws2.dir=="left"){
//                        newroute.start.x=s.white.get(i).start.x;
//                        newroute.start.y=s.white.get(i).start.y+1;
//                        newroute.start.dir="down";
//                        newroute.end.x=s.white.get(i).start.x;
//                        newroute.end.y=s.white.get(i).start.y-1;
//                        newroute.start.dir="up";
//                        s.red.add(newroute);
//                    }
//                    if(ws1.dir=="right" && ws2.dir=="down"){
//                        newroute.start.x=s.white.get(i).start.x;
//                        newroute.start.y=s.white.get(i).start.y+1;
//                        newroute.start.dir="down";
//                        newroute.end.x=s.white.get(i).start.x-1;
//                        newroute.end.y=s.white.get(i).start.y;
//                        newroute.start.dir="right";
//                        s.red.add(newroute);
//                    }///3
//
//                    if(ws1.dir=="up" && ws2.dir=="left"){
//                        newroute.start.x=s.white.get(i).start.x+1;
//                        newroute.start.y=s.white.get(i).start.y;
//                        newroute.start.dir="left";
//                        newroute.end.x=s.white.get(i).start.x;
//                        newroute.end.y=s.white.get(i).start.y-1;
//                        newroute.start.dir="up";
//                        s.red.add(newroute);
//                    }
//                    if(ws1.dir=="up" && ws2.dir=="down"){
//                        newroute.start.x=s.white.get(i).start.x+1;
//                        newroute.start.y=s.white.get(i).start.y;
//                        newroute.start.dir="left";
//                        newroute.end.x=s.white.get(i).start.x-1;
//                        newroute.end.y=s.white.get(i).start.y;
//                        newroute.start.dir="right";
//                        s.red.add(newroute);
//                    }
//                    if(ws1.dir=="up" && ws2.dir=="right"){
//                        newroute.start.x=s.white.get(i).start.x-1;
//                        newroute.start.y=s.white.get(i).start.y;
//                        newroute.start.dir="right";
//                        newroute.end.x=s.white.get(i).start.x;
//                        newroute.end.y=s.white.get(i).start.y-1;
//                        newroute.start.dir="up";
//                        s.red.add(newroute);
//                    }/////////4
//
//                }
//                if((s.white.get(i).start.x=s.white.get(j).end.x) && (s.white.get(i).start.y==s.white.get(j).end.y)){
//                    s.white.get(i).start.x=s.red.get(j).start.x;
//                    s.white.get(i).start.y=s.red.get(i).start.y;
//                    s.white.remove(j);
//                }
//                if((s.white.get(i).end.x=s.white.get(j).end.x) && (s.white.get(i).end.y==s.white.get(j).end.y)){
//                    s.white.get(i).end.x=s.red.get(j).start.x;
//                    s.white.get(i).end.y=s.red.get(i).start.y;
//                    s.white.remove(j);
//                }
//                if((s.white.get(i).end.x=s.white.get(j).start.x) && (s.white.get(i).end.y==s.white.get(j).start.y)){
//                    s.white.get(i).end.x=s.red.get(j).end.x;
//                    s.white.get(i).end.y=s.red.get(i).end.y;
//                    s.white.remove(j);
//                }
//            }
//            if((s.white.get(i).start.x==s.white.get(i).end.x)&&(s.white.get(i).start.y==s.white.get(i).end.y)){
//                s.win=true;
//            }
//            for (int j=0;j<rsize;j++){
//                if((s.white.get(i).start.x==s.red.get(j).start.x) && (s.white.get(i).start.y==s.red.get(j).start.y)){
//
//                }
//                if((s.white.get(i).start.x=s.red.get(j).end.x) && (s.white.get(i).start.y==s.red.get(j).end.y)){
//
//                }
//                if((s.white.get(i).end.x=s.red.get(j).end.x) && (s.white.get(i).end.y==s.red.get(j).end.y)){
//
//                }
//                if((s.white.get(i).end.x=s.red.get(j).start.x) && (s.white.get(i).end.y==s.red.get(j).start.y)){
//
//                }
//
//            }
//        }
//        return s;
//    }
//    void AlphaBeta(AlphaBetaNode[] state){
//        int Depth=4;
//        int w=0;
//        int d=0;
//        boolean turn=true;//our turn
//        boolean AlphaBetaF=true;///alpha
//        //AlphaBetaNode state;
//        int score =0;
//        MoveGenerator(state, d, ws, rs);
//        w=winDetector(ws,rs);
//        for(int j=0;j<state.children.size();j++) {
//            if (AlphaBetaF && (state.alpha > state.beta)) {
//                break;
//            }
//            else {
//                while (d < Depth && w != 10) {
//                    ws = statusUpdate(ws, state.children.get(j));
//                    state=state.children.get(j);
//                    d = d + 1;
//                    if (!turn) turn = true;
//                    else turn = false;
//                    //AlphaBeta(ws,rs,d);
//                    MoveGenerator(d, ws, rs);
//                }
//                d = d - 1;
//                setAlphaBeta(state, score, turn);
//            }
//        }
//    }
}