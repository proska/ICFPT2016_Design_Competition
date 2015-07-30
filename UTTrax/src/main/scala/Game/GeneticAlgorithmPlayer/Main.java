package Game.GeneticAlgorithmPlayer;

/**
 * Created by mohammad on 7/18/15.
 */
public class Main {
    private static final boolean myColor = Tile.white;

    public static void main (String[] args) {
        Map nm = new Map ();
        nm.readFromFile("test.trx");
        GA GeneticAlgorithmTraxPlayer = new GA (nm, myColor);
        nm.writeToFile("test.trx");
    }
}
