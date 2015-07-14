package Game.GeneticAlgorithmPlayer;

import java.util.ArrayList;

/**
 * Created by mohammad on 7/12/15.
 */
public class Test {
    private static final boolean myColor = Tile.red;

    public static void main (String[] args) {
        Population winnerPop = new Population(20);
        Population loserPop = new Population(20);


        Map nm = new Map ();
        nm.readFromFile();
        winnerPop.initialization(nm);
        loserPop.initialization (nm);
        winnerPop.fillRandomPop();
        loserPop.fillRandomPop();

        for (int i=0 ; i<winnerPop.popSize ; i++) {
            winnerPop.popMaps[i].getFitness(myColor);
        }

        for (int i=0 ; i<loserPop.popSize ; i++) {
            loserPop.popMaps[i].getFitness(myColor);
        }

        for (int i = 0; i < 1000 ; i++) {
            Map newMap = new Map(new ArrayList <Tile> (nm.map));
            newMap.fillRandom();
            newMap.getFitness(myColor);
            int minFitnessIndex = 0;
            int maxFitnessIndex = 0;
            for (int j=1 ; j<winnerPop.popSize - 1 ; j++) {
                if (winnerPop.popMaps[j].fitness < winnerPop.popMaps[minFitnessIndex].fitness) {
                    minFitnessIndex = j;
                }
                if (loserPop.popMaps[j].fitness > loserPop.popMaps[maxFitnessIndex].fitness) {
                    maxFitnessIndex = j;
                }
            }
            if (newMap.fitness > winnerPop.popMaps[minFitnessIndex].fitness) {
                winnerPop.setMap(minFitnessIndex, newMap);
            } else if (newMap.fitness < loserPop.popMaps[maxFitnessIndex].fitness) {
                loserPop.setMap(maxFitnessIndex, newMap);
            }
        }

        winnerPop.writePopMapsToFiles();
        loserPop.writePopMapsToFiles();


    }
}
