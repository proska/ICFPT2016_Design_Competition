package Game.GeneticAlgorithmPlayer;

import java.util.ArrayList;

/**
 * Created by mohammad on 7/13/15.
 */
public class Population {
    Map[] popMaps;
    int popSize;


    public Population(int populationSize) {
        popMaps = new Map[populationSize];
        this.popSize = populationSize;
    }

    public void initialization (Map mp) {
        for (int i=0 ; i<popSize ; i++) {
            popMaps[i] = new Map(new ArrayList <Tile> (mp.map));
        }
        Map.fixedTileNumber = mp.map.size();
    }

    public void fillRandomPop () {
        for (int i=0 ; i<popSize ; i++) {
            //popMaps[i] = new Map();
            popMaps[i].fillRandom();
        }
    }

    public void writePopMapsToFiles () {
        for (int i=0 ; i<popSize ; i++) {
            popMaps[i].writeToFile();
        }
    }

    public void setMap (int index, Map map) {
        popMaps[index] = map;
    }
}
