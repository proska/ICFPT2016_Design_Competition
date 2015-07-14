package Game.Player

import Game.World.Margin.Margin
import Game.World.Margin.Margin
import Game.World._
import Game.World.traxColor.traxColor

import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 7/11/15.
 */
trait moveFinder {

  type Terminal = Tuple2[Coordinate,Margin]

  def giveCompatibleRoutesWithMove( routes:List[Route] , move:Move ): List[Int] = {
    routes.zipWithIndex.filter( {
      case (route:Route , index:Int) => (isTerminalCompatibleWithMove(route.start,move) ) ||
        (isTerminalCompatibleWithMove(route.end,move) )
    } ).map(_._2)
  }

  ///////////////////////////////////////////
  ///////////////////////////////////////////
  ///////////////////////////////////////////

  // Valid Move Rules
  def isTerminalCompatibleWithMove(terminal:(Coordinate,Margin),move:Move): Boolean = {
    (terminal._1 == move.pos) && isPathContinue(terminal._2,move)
  }

  ///////////////////////////////////////////
  ///////////////////////////////////////////
  ///////////////////////////////////////////

  def isPathContinue(dir:Margin , move:Move):Boolean = {

    ((dir == Margin.TOP   )  & ((move.TileType.getVal & (1<<3))==1)) |
      ((dir == Margin.DOWN)  & ((move.TileType.getVal & (1<<2))==1)) |
      ((dir == Margin.RIGHT )  & ((move.TileType.getVal & (1<<1))==1)) |
      ((dir == Margin.LEFT  )  & ((move.TileType.getVal & (1<<0))==1))
  }

  ///////////////////////////////////////////
  ///////////////////////////////////////////
  ///////////////////////////////////////////

  def giveAllPossibleMoves(state:gameState , side:traxColor): List[Move] = {
    var movesList:List[Move] = null


    for( myRoute <- if(side == traxColor.WHITE)  state.whiteRoutes else state.blackRoutes ){

      movesList ++ giveValidMoves(myRoute.start) ++ giveValidMoves(myRoute.end)

    }

    def giveValidMoves(terminal: Terminal): List[Move] = {
      var tmpList: List[Move] = giveBasicPossibleMovesOfTerminal(terminal, side).get

      tmpList = giveSharedTerminal(terminal) match {
        case Some(opp) => {
          tmpList.filter(x => !isPow2(x.TileType.getVal & (1 << Margin.getVal(opp._2))))
        }
        case None => tmpList
      }

      tmpList
    }

    def giveSharedTerminal(myTerminal: Terminal): Option[Terminal] = {
      for ((oppRoute, index) <- {
        if (side == traxColor.WHITE) state.blackRoutes else state.whiteRoutes
      }.zipWithIndex) {

        if (oppRoute.start._1 == myTerminal._1 )
          return Some(oppRoute.start)
        else if( oppRoute.end._1 == myTerminal._1 )
          return Some(oppRoute.end)
      }
      None
    }

    movesList
  }

  def giveBasicPossibleMovesOfTerminal(terminal: (Coordinate,Margin) , color:traxColor): Try[List[Move]] = {

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

  def isPow2(in: Int):Boolean = (in & (in-1)) == 0

  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////

  def updateState(move: Move,state:gameState):Any = {

  }
}
