package Game.GeneticAlgorithmPlayer;

import java.util.ArrayList;

/**
 * Created by mohammad on 7/12/15.
 */
public class GA {
    Population winnerPop = new Population(50);
    Population loserPop = new Population(50);
    Map preMap;
    Tile nextMove;

    public GA (Map nm, boolean myColor) {

        preMap = new Map (new ArrayList<Tile>(nm.map));
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


        for (int i = 0; i < 2000 ; i++) {
            Map newMap = new Map(new ArrayList <Tile> (nm.map));
            newMap.fillRandom();
            newMap.getFitness(myColor);
            int minFitnessIndex = 0;
            int maxFitnessIndex = 0;
            for (int j=1 ; j<winnerPop.popSize ; j++) {
                if (winnerPop.popMaps[j].fitness < winnerPop.popMaps[minFitnessIndex].fitness) {
                    minFitnessIndex = j;
                }
                if (loserPop.popMaps[j].fitness > loserPop.popMaps[maxFitnessIndex].fitness) {
                    maxFitnessIndex = j;
                }
            }
            if (newMap.fitness > winnerPop.popMaps[minFitnessIndex].fitness && newMap.fitness > 30) {
                boolean repetitivePath = false;
                int j = 0;
                while (repetitivePath == false && j<winnerPop.popSize) {
                    if (winnerPop.popMaps[j].equalPath (newMap) && winnerPop.popMaps[j].fitness > 30 && winnerPop.popMaps[j].map.get(Map.fixedTileNumber).coordinate.equalPoint(newMap.map.get(Map.fixedTileNumber).coordinate) ) {
                        repetitivePath = true;
                    }
                    j++;
                }
                if (!repetitivePath) {
                    winnerPop.setMap(minFitnessIndex, newMap);
                }
            } else if (newMap.fitness < loserPop.popMaps[maxFitnessIndex].fitness && newMap.fitness < -30) {
                boolean repetitivePath = false;
                int j = 0;
                while (repetitivePath == false && j<loserPop.popSize) {
                    if (loserPop.popMaps[j].equalPath (newMap) && loserPop.popMaps[j].fitness < -30 && loserPop.popMaps[j].map.get(Map.fixedTileNumber).coordinate.equalPoint(newMap.map.get(Map.fixedTileNumber).coordinate) ) {
                        repetitivePath = true;
                    }
                    j++;
                }
                if (!repetitivePath) {
                    loserPop.setMap(maxFitnessIndex, newMap);
                }
            }
        }

        winnerPop.writePopMapsToFiles("WMap");
        loserPop.writePopMapsToFiles("LMap");

        nm.findPointCandidates(nm.nextMoveCadidates);
        ArrayList <Tile> tileCandidates = new ArrayList<Tile>();
        nm.findTileCandidates (nm.nextMoveCadidates, tileCandidates);

        scoring (tileCandidates);
        int bestTileIndex = 0;
        for (int i=1; i<tileCandidates.size(); i++) {
            if (tileCandidates.get(i).score > tileCandidates.get(bestTileIndex).score) {
                bestTileIndex = i;
            }
        }

        nextMove = tileCandidates.get(bestTileIndex);
        nm.addTile(nextMove);
        nm.autoFill();
    }

    private void scoring (ArrayList<Tile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            Map newMap = new Map(new ArrayList<Tile>(preMap.map));
            newMap.addTile(tiles.get(i));
            newMap.autoFill();
            for (int j = 0; j < winnerPop.popSize; j++) {
                for (int k = Map.fixedTileNumber; k < newMap.map.size(); k++) {
                    if (newMap.map.get(k).coordinate.equalPoint(winnerPop.popMaps[j].map.get(Map.fixedTileNumber).coordinate)) {
                        if (newMap.map.get(k).getTileChar() == winnerPop.popMaps[j].map.get(Map.fixedTileNumber).getTileChar()) {
                            tiles.get(i).score += winnerPop.popMaps[j].fitness;
                        } else {
                            tiles.get(i).score += winnerPop.popMaps[j].antiFitness;
                        }
                        break;
                    }
                }
                for (int k = Map.fixedTileNumber; k < newMap.map.size(); k++) {
                    if (newMap.map.get(k).coordinate.equalPoint(loserPop.popMaps[j].map.get(Map.fixedTileNumber).coordinate)) {
                        if (newMap.map.get(k).getTileChar() == loserPop.popMaps[j].map.get(Map.fixedTileNumber).getTileChar()) {
                            tiles.get(i).score += loserPop.popMaps[j].fitness;
                        } else {
                            tiles.get(i).score += loserPop.popMaps[j].antiFitness;
                        }
                    }
                    break;
                }
            }
        }
    }

    public Tile getNextMove(){
        return this.nextMove;
    }
}
