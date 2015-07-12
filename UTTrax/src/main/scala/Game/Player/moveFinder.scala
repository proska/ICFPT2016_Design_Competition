package Game.Player

import Game.World.Margin.Margin
import Game.World.Margin.Margin
import Game.World.{Margin, Coordinate, Move, Route}

/**
 * Created by proska on 7/11/15.
 */
trait moveFinder {


  def giveCompatibleRoutesWithMove( routes:List[Route] , move:Move ): List[Int] = {
    routes.zipWithIndex.filter( {
      case (route:Route , index:Int) => (isTerminalCompatibleWithMove(route.start,move) ) ||
        (isTerminalCompatibleWithMove(route.end,move) )
    } ).map(_._2)
  }

  // Valid Move Rules
  def isTerminalCompatibleWithMove(terminal:(Coordinate,Margin),move:Move): Boolean = {
    (terminal._1 == move.pos) && isPathContinue(terminal._2,move)
  }

  def isPathContinue(dir:Margin , move:Move):Boolean = {

    ((dir == Margin.TOP   )  & ((move.TileType.getVal & (1<<3))==1)) |
      ((dir == Margin.DOWN)  & ((move.TileType.getVal & (1<<2))==1)) |
      ((dir == Margin.RIGHT )  & ((move.TileType.getVal & (1<<1))==1)) |
      ((dir == Margin.LEFT  )  & ((move.TileType.getVal & (1<<0))==1))
  }
}
