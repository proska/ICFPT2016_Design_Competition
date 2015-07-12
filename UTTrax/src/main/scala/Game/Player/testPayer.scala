package Game.Player

import Game.World.Margin.Margin
import Game.World._
import Game.World.traxColor.traxColor
import scala.util.control.Breaks._

import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 7/1/15.
 */
class testPayer(color:traxColor) extends Player with moveFinder{

  state = new gameState

  // Gives the indicated move
  override def play(): Move = {
    new Move(traxTiles.BBWW , new Coordinate)
  }


  // Update the internal state of the player
  def updateState(move:Move): Unit ={

    // Check for terminals
    val whiteIndexes = giveCompatibleRoutesWithMove(state.whiteRoutes.toList,move)


  }
  // Lists All possible moves
  def listAllMoves(): List[Move] = {

    var movesList:List[Move] = null

    for( myRoute <- if(color == traxColor.WHITE)  state.whiteRoutes else state.blackRoutes ){

       giveSharedTerminal





      def giveSharedTerminal: Int = {
        for ((oppRoute, index) <- {
          if (color == traxColor.WHITE) state.blackRoutes else state.whiteRoutes
        }.zipWithIndex) {
          if (oppRoute.start._1 == myRoute.start || oppRoute.start._1 == myRoute.start ||
            oppRoute.start._1 == myRoute.start || oppRoute.start._1 == myRoute.start) {
            return index
          }
        }
        -1
      }
    }

    def giveBasicPossibleMoves(terminal: (Coordinate,Margin) , color:traxColor): Try[List[Move]] = {

      var out:Try[List[Move]] = null

            out =
              if(terminal._2 == Margin.TOP)
                Success(  List( new Move(traxTiles.WBBW, terminal._1),
                  new Move(traxTiles.WBWB, terminal._1),
                  new Move(traxTiles.WWBB, terminal._1)))
              else if(terminal._2 == Margin.TOP)
                Success(  List( new Move(traxTiles.WBBW, terminal._1),
                  new Move(traxTiles.WBBW, terminal._1),
                  new Move(traxTiles.WBBW, terminal._1)))
              else if(terminal._2 == Margin.TOP)
                Success(  List( new Move(traxTiles.WBBW, terminal._1),
                  new Move(traxTiles.WBBW, terminal._1),
                  new Move(traxTiles.WBBW, terminal._1)))
              else if(terminal._2 == Margin.TOP)
                Success(  List( new Move(traxTiles.WBBW, terminal._1),
                  new Move(traxTiles.WBBW, terminal._1),
                  new Move(traxTiles.WBBW, terminal._1)))
              else Failure(throw new IllegalArgumentException)

      if(color == traxColor.BLACK)
        out  = out.map(_.map(x => Move(x._tile.flip() , x._pos)))

//      Success(List(new Move(traxTiles.BBWW,Coordinate(0,0))))
      out
    }

    movesList
  }

}
