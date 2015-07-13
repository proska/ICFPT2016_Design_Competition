package Game.Player

import Game.World.Margin.Margin
import Game.World._
import Game.World.traxColor.traxColor
import scala.util.control.Breaks._

import scala.util.{Random, Failure, Success, Try}

/**
 * Created by proska on 7/1/15.
 */
class testPayer(color:traxColor) extends Player with moveFinder{

  state = new gameState

  // Gives the indicated move
  override def play(): Move = {
    val moveList = giveAllPossibleMoves(state,color)

    val r = new Random()

    val move = moveList(r.nextInt(moveList.length))

    updateState(move,state)

    move
  }

  override def update(move: Move):Any = {
    updateState(move,state)
  }

}
