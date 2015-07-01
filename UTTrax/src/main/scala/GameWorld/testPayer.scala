package GameWorld

import GameWorld.Margin.Margin
import GameWorld.traxColor.traxColor

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 7/1/15.
 */
class testPayer(color:traxColor) extends Player{

  // Gives the indicated move
  def play(state:gameState): Move = {

    new Move(traxTiles.BBWW , new Coordinate)
  }

  def updateState(move:Move): Unit ={

    // Check for terminals
    val whiteIndexes = state.whiteRoutes.zipWithIndex.filter( {
      case (route:Route , index:Int) => (isTerminalCompatibleWithMove(route.start,move) ) ||
        (isTerminalCompatibleWithMove(route.end,move) )
    } )

    val blackIndexes = state.blackRoutes.zipWithIndex.filter( {
      case (route:Route , index:Int) => (isTerminalCompatibleWithMove(route.start,move) ) ||
        (isTerminalCompatibleWithMove(route.end,move) )
    } )

    if(whiteIndexes.length == 0 && blackIndexes.length == 0){
      throw new UnsupportedOperationException("Invalid Move has been given!")
    } else if(whiteIndexes.length == 0 && blackIndexes.length != 0){ //

    } else if(whiteIndexes.length != 0 && blackIndexes.length == 0){

    }


  }

  // Valid Move Rules
  private def isTerminalCompatibleWithMove(terminal:(Coordinate,Margin),move:Move): Boolean = {
    (terminal._1 == move.pos) && isPathContinue(terminal._2,move)
  }
  private def isPathContinue(dir:Margin , move:Move):Boolean = {

      ((dir == Margin.TOP   )  & move.TileType.getVal > (1<<3)) |
      ((dir == Margin.BOTTOM)  & move.TileType.getVal > (1<<2)) |
      ((dir == Margin.RIGHT )  & move.TileType.getVal > (1<<1)) |
      ((dir == Margin.LEFT  )  & move.TileType.getVal > (1<<0))
  }

  // Lists All possible moves
  def listAllMoves(): List[Move] ={

    var movesList:List[Move] = null


    for( myRoute <- if(color == traxColor.WHITE)  state.whiteRoutes else state.blackRoutes ){

      var tmpList:List[Move] = null

      tmpList ++ giveBasicPossibleMoves(myRoute.start,color).get ++ giveBasicPossibleMoves(myRoute.end,color).get

      for( tmpMove <- tmpList ; oppTerminal <- if(color == 1)  state.whiteRoutes else state.blackRoutes ){
        //        if(tmpMove)
      }
    }

    movesList

    def giveBasicPossibleMoves(terminal: (Coordinate,Margin) , color:traxColor): Try[List[Move]] = {

      var out:Try[List[Move]] = null

      out =
        if(terminal._2 == Margin.TOP)
          Success(  List( new Move(traxTiles.WBBW, terminal._1, terminal._2),
            new Move(traxTiles.WBWB, terminal._1, terminal._2),
            new Move(traxTiles.WWBB, terminal._1, terminal._2)))
        else if(terminal._2 == Margin.TOP)
          Success(  List( new Move(traxTiles.WBBW, terminal._1, terminal._2),
            new Move(traxTiles.WBBW, terminal._1, terminal._2),
            new Move(traxTiles.WBBW, terminal._1, terminal._2)))
        else if(terminal._2 == Margin.TOP)
          Success(  List( new Move(traxTiles.WBBW, terminal._1, terminal._2),
            new Move(traxTiles.WBBW, terminal._1, terminal._2),
            new Move(traxTiles.WBBW, terminal._1, terminal._2)))
        else if(terminal._2 == Margin.TOP)
          Success(  List( new Move(traxTiles.WBBW, terminal._1, terminal._2),
            new Move(traxTiles.WBBW, terminal._1, terminal._2),
            new Move(traxTiles.WBBW, terminal._1, terminal._2)))
        else Failure(throw new IllegalArgumentException)

      if(color == traxColor.BLACK)
        out.map(_.map(_.TileType.flip() ))

      out
    }
  }

}
