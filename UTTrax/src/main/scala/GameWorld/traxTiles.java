package GameWorld;

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
    BWBW(5);    //  | 0 1 0 1|  0 1 1 0|
    //      b       //  |        |         |
    //      b       //  |        |         |
    //w w w   b b b //  |        |         |
    //      w       //  |        |         |
    //      w       //  |        |         |
    //--------------------------------------


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


}
