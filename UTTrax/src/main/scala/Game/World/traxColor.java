package Game.World;

/**
 * Created by proska on 8/1/15.
 */
public enum traxColor {
    WHITE,
    BLACK;

    public static traxColor flip(traxColor in){
        if(in == WHITE) return BLACK; else return WHITE;
    }
}

//object traxColor extends Enumeration {
//        type traxColor = Value
//        val WHITE,BLACK = Value
//
//        def flip(in:traxColor):traxColor = if(in == WHITE) BLACK else WHITE
//        }
