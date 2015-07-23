package Game.TestPlayer

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

  def giveCompatibleRoutesWithMove( routes:List[Route] , move:Move ): List[Route] = {
    routes.filter( {
      case (route:Route) => (isTerminalCompatibleWithMove(route.start,move) ) ||
        (isTerminalCompatibleWithMove(route.end,move) )
    } )
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

    ((dir == Margin.TOP   )  & ((move.TileType.getVal & (1<<3))!=0)) |
      ((dir == Margin.DOWN)  & ((move.TileType.getVal & (1<<2))!=0)) |
      ((dir == Margin.RIGHT )  & ((move.TileType.getVal & (1<<1))!=0)) |
      ((dir == Margin.LEFT  )  & ((move.TileType.getVal & (1<<0))!=0))
  }

  ///////////////////////////////////////////
  ///////////////////////////////////////////
  ///////////////////////////////////////////

  def giveAllPossibleMoves(state:gameState , side:traxColor): List[Move] = {
    var movesList:List[Move] = List()

    val comp = side.compare(traxColor.WHITE) == 0

    for( myRoute <- if( side.compare(traxColor.WHITE) == 0 )  state.whiteRoutes else state.blackRoutes ){

//      val a = giveValidMoves(myRoute.start)
      movesList = movesList ++ giveValidMoves(myRoute.start)
      movesList = movesList ++ giveValidMoves(myRoute.end)

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
      for ( oppRoute <-
            if (side.compare(traxColor.WHITE) == 0) state.blackRoutes else state.whiteRoutes) {

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
      else if(terminal._2 == Margin.DOWN)
        Success(  List( new Move(traxTiles.BWBW, terminal._1),
          new Move(traxTiles.WWBB, terminal._1),
          new Move(traxTiles.BWWB, terminal._1)))
      else if(terminal._2 == Margin.LEFT)
        Success(  List( new Move(traxTiles.WBBW, terminal._1),
          new Move(traxTiles.BBWW, terminal._1),
          new Move(traxTiles.BWBW, terminal._1)))
      else if(terminal._2 == Margin.RIGHT)
        Success(  List( new Move(traxTiles.BBWW, terminal._1),
          new Move(traxTiles.BWWB, terminal._1),
          new Move(traxTiles.WBWB, terminal._1)))
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
}
