package Game.TestPlayer

import Game.World.Margin.Margin
import Game.World._
//import Game.World.traxColor.traxColor
import scala.util.control.Breaks._

import scala.util.{Random, Failure, Success, Try}

/**
 * Created by proska on 7/1/15.
 */
class testPlayerScala(color:traxColor) extends PlayerScala with moveFinder{

  override val side = color
  state = new gameState
  initialize()
  // Gives the indicated move
  override def play(): Move = {
    val moveList = giveAllPossibleMoves(state,color)

    val r = new Random()

    assert(moveList.length > 0 , "No move is Available!")

    val move = moveList(r.nextInt(moveList.length))
//    val move = Move(traxTiles.WBBW , Coordinate(-1,0))

    update(move)

    println("[INFO] Player "+side+" returned move:"+move)

    move
  }
}
