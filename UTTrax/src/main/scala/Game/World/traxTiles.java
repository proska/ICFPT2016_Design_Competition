package Game.World;

/**
 * Created by proska on 6/10/15.
 */
public enum traxTiles {
                    //  W:N S E W B:N S E W
    WWBB(12),       //  | 1 1 0 0|  0 0 1 1|
    //      w       //  |        |         |
    //      w       //  |        |         |
    //b b b w b b b //  |        |         |
    //      w       //  |        |         |
    //      w       //  |        |         |
    //--------------------------------------
    BBWW(3),    //  | 0 0 1 1|  1 1 0 0|
    //      b       //  |        |         |
    //      b       //  |        |         |
    //w w w b w w w //  |        |         |
    //      b       //  |        |         |
    //      b       //  |        |         |
    //--------------------------------------
    WBBW(9),    //  | 1 0 0 1|  0 1 1 0|
    //      w       //  |        |         |
    //      w       //  |        |         |
    //w w w   b b b //  |        |         |
    //      b       //  |        |         |
    //      b       //  |        |         |
    //--------------------------------------
    BWWB(6),    //  | 0 1 1 0|  1 0 0 1|
    //      b       //  |        |         |
    //      b       //  |        |         |
    //b b b   w w w //  |        |         |
    //      w       //  |        |         |
    //      w       //  |        |         |
    //--------------------------------------
    WBWB(10),   //  | 1 0 1 0|  0 1 1 0|
    //      w       //  |        |         |
    //      w       //  |        |         |
    //b b b   w w w //  |        |         |
    //      b       //  |        |         |
    //      b       //  |        |         |
    //--------------------------------------
    BWBW(5),    //  | 0 1 0 1|  0 1 1 0|
    //      b       //  |        |         |
    //      b       //  |        |         |
    //w w w   b b b //  |        |         |
    //      w       //  |        |         |
    //      w       //  |        |         |
    //--------------------------------------
    INVALID(0);

    private int numVal;
    traxTiles(int val){
        this.numVal = val;
    }

    public int getVal(){
        return this.numVal;
    }

//  val tileList = Set(WWBB , BBWW , WBBW , BWWB , WBWB , BWBW).toIndexedSeq

    // TODO :
    //  design trax tiles and trax map:
    //    > have functions to put tiles in the map in given place
    //    > use the line representation used in PAToMAT's work to save state of the game internally
    //    >

//  def getTileCode(in:traxType): Int ={
//    Map(traxType -> 12 , BBWW -> 3 , WBBW -> 9 , BWWB -> 6 , WBWB -> 10 , BWBW -> 5)
//  }

    public traxTiles str2Tile(String in){

        if (in == "WWBB") return WWBB;
        if (in == "BBWW") return BBWW;

        if (in == "WBWB") return WBWB;
        if (in == "BWBW") return BWBW;

        if (in == "WBBW") return WBBW;
        if (in == "BWWB") return BWWB;

        return INVALID;
    }

    public traxTiles flip(){
        if (this == WWBB) return BBWW;
        if (this == BBWW) return WWBB;

        if (this == WBWB) return BWBW;
        if (this == BWBW) return WBWB;

        if (this == WBBW) return BWWB;
        if (this == BWWB) return WBBW;

        return INVALID;
    }

    public traxTiles num2Tile(int in){
        if(in == 12) return WWBB;
        if(in == 3 ) return BBWW;
        if(in == 9 ) return WBBW;
        if(in == 6 ) return BWWB;
        if(in == 10) return WBWB;
        if(in == 5) return BWBW;
        return INVALID;
    }
}
