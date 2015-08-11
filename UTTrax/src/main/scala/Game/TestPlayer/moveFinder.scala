package Game.TestPlayer

//import javafx.geometry.Side

import Game.World.Margin.Margin
import Game.World.Margin.Margin
import Game.World._
//import Game.World.traxColor.traxColor

import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 7/11/15.
 */
object moveFinder {

  type Terminal = Tuple2[Coordinate,Margin]

  ///////////////////////////////////////////
  ///////////////////////////////////////////
  ///////////////////////////////////////////


  def giveAllPossibleMoves(state:gameState , side:traxColor): List[Move] = {
    var movesList:List[Move] = List()

    listAvailableMoves(state.whiteRoutes,traxColor.WHITE)
    listAvailableMoves(state.blackRoutes,traxColor.BLACK)


    def listAvailableMoves(list:List[Route],side: traxColor): Unit = {
      for (myRoute <- list) {
        //      val a = giveValidMoves(myRoute.start)
        if(movesList.forall(x => !(x.pos == myRoute.start._1)))
          movesList = movesList ++ giveValidMoves(myRoute.start, side)
        if(movesList.forall(x => !(x.pos == myRoute.end._1)))
          movesList = movesList ++ giveValidMoves(myRoute.end, side)
      }
    }

    def giveValidMoves(terminal: Terminal,side:traxColor): List[Move] = {
      val tmpList: List[Move] = giveBasicPossibleMovesOfTerminal(terminal, side).get
      def isPow2(in: Int):Boolean = ((in & (in-1)) == 0) & (in != 0)



      def checkShared: List[Move] = {
        giveSharedTerminal(terminal, side) match {
          case Some(opp) => {

            assert(tmpList.map(x => !isPow2(x.TileType.getVal & (1 << Margin.getVal(opp._2)))).reduceLeft(_||_),"shared failed")

            tmpList.filter(x => !isPow2(x.TileType.getVal & (1 << Margin.getVal(opp._2))))
          }
          case None => tmpList
        }
      }

      val list  = checkShared

      list
    }

    def giveSharedTerminal(myTerminal: Terminal,side:traxColor): Option[Terminal] = {
      for ( oppRoute <-
            if (side == traxColor.WHITE ) state.blackRoutes else state.whiteRoutes) {

        if (oppRoute.start._1 == myTerminal._1 )
          return Some(oppRoute.start)
        else if( oppRoute.end._1 == myTerminal._1 )
          return Some(oppRoute.end)
      }
      None
    }

    movesList
  }

  private def giveBasicPossibleMovesOfTerminal(terminal: (Coordinate,Margin) , color:traxColor): Try[List[Move]] = {

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

  ///////////////////////////////////////////
  ///////////////////////////////////////////
  ///////////////////////////////////////////

  def giveCompatibleRoutesWithMove( routes:List[Route] , move:Move ): List[Route] = {

    routes match {
      case _:List[Route] =>
        routes.filter( {
          case (route:Route) => (isTerminalCompatibleWithMove(route.start,move) ) ||
            (isTerminalCompatibleWithMove(route.end,move) )
        } )
      case _ => List()
    }

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

  private def isPathContinue(dir:Margin , move:Move):Boolean = {

    ((dir == Margin.TOP   )  & ((move.TileType.getVal & (1<<3))!=0)) |
      ((dir == Margin.DOWN)  & ((move.TileType.getVal & (1<<2))!=0)) |
      ((dir == Margin.RIGHT )  & ((move.TileType.getVal & (1<<1))!=0)) |
      ((dir == Margin.LEFT  )  & ((move.TileType.getVal & (1<<0))!=0))
  }


  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
}
