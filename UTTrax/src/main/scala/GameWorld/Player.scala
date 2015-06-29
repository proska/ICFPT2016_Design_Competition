package GameWorld

//import TraxWorld._

/**
 * Created by proska on 6/29/15.
 */
trait Player {
  def play(state:gameState): Move
}


class testPayer extends Player{
  def play(state:gameState): Move = {

    new Move(traxTiles.BBWW , 0 , 0)
  }
}