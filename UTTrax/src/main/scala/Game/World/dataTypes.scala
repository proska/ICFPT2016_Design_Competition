package Game.World

//import Game.Margin.Margin
import Game.TestPlayer.moveFinder
import Game.World.Margin.Margin
import Game.World.traxColor.traxColor

import scala.Option
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 7/12/15.
 */
object traxColor extends Enumeration {
  type traxColor = Value
  val WHITE,BLACK = Value
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

object Margin extends Enumeration {
  type Margin = Value
  val TOP, DOWN, LEFT, RIGHT = Value

  def getVal(margin: Margin):Int = {
    margin match {
      case TOP    => 3
      case DOWN   => 2
      case RIGHT  => 1
      case LEFT   => 0
    }
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

case class Coordinate(_x:Int = 0 , _y :Int = 0){
  var X:Int = _x
  var Y:Int = _y

  def == (that:Coordinate): Boolean ={
    X == that.X & Y == that.Y
  }
  def assign (that:Coordinate){
    this.X = that.X
    this.Y = that.Y
  }

  def xPlus: Coordinate ={
    Coordinate(X + 1,Y);
  }

  def xMinus: Coordinate ={
    Coordinate(X - 1,Y);
  }

  def yPlus: Coordinate ={
    Coordinate(X,Y+1);
  }

  def yMinus: Coordinate ={
    Coordinate(X,Y-1);
  }

}

object Coordinate {
  def apply(coor: Coordinate):Coordinate = {
    new Coordinate(coor.X,coor.Y)
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

class Route extends moveFinder{

  var start = new Terminal(new Coordinate,Margin.TOP)
  var end = new Terminal(new Coordinate,Margin.TOP)

  var length = 0

//  def Route={
//    start = (Coordinate(0,1),Margin.DOWN)
//    end = (Coordinate(0,-1),Margin.TOP)
//  }


  def isLoop:Boolean = start._1 == end._1

  def update(move:Move): Unit ={
    val st = checkStart(move)
    val en = checkEnd(move)

    assert( !(st&en) ,"Loop Route!")

    if(st){
      start = getNewTerminal(move,start._2)
    } else if(en){
      end = getNewTerminal(move,end._2)
    }

    length +=1

  }

  def getNewTerminal(move: Move , margin: Margin):(Coordinate,Margin) = {
    assert((move.TileType.getVal & (1<< Margin.getVal(margin))) != 0 , "The requested move cannot continue the terminal!")

    val otherMargin = move.TileType.getVal - (1<< Margin.getVal(margin))

    assert((otherMargin & (otherMargin - 1)) == 0 , "OtherMargin is not pow2!" )

    if(otherMargin == 1){ ( move.pos.xMinus, Margin.RIGHT ) }
    else  if(otherMargin == 2){ ( move.pos.xPlus , Margin.LEFT  ) }
    else  if(otherMargin == 4){ ( move.pos.yMinus, Margin.TOP   ) }
    else                      { ( move.pos.yPlus , Margin.DOWN  ) }

  }

  private def isCompatible(move: Move):Boolean = {
    checkEnd(move) || checkStart(move)
  }

  private def checkEnd(move: Move): Boolean = {
    isTerminalCompatibleWithMove(end, move)
  }

  private def checkStart(move: Move): Boolean = {
    isTerminalCompatibleWithMove(start, move)
  }

  def doMerge(that:Route):Option[Route] = {
    if(this.end == that.end){
      Some(Route(this.start,that.start,this.length + that.length + 1))
    } else if(this.end == that.start){
      Some(Route(this.start,that.end,this.length + that.length + 1))
    } else if(this.start == that.end){
      Some(Route(this.end,that.start,this.length + that.length + 1))
    } else if(this.start == that.start){
      Some(Route(this.end,that.end,this.length + that.length + 1))
    } else
      None
  }
}

object Route extends moveFinder{
  def apply(route: Route):Route = {
    val out = new Route
    out.start = new Terminal(route.start._1,route.start._2)
    out.end = new Terminal(route.end._1,route.end._2)
    out.length = route.length + 0
    out
  }
  def apply(t1: Terminal,t2: Terminal,len:Int):Route = {
    val out = new Route
    out.start = t1
    out.end = t2
    out.length = len
    out
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

class gameState extends moveFinder{
  var whiteRoutes : List[Route] = _
  var blackRoutes : List[Route] = _

  def updateState(move: Move,side:traxColor):Try[_]= {

    val myRoute = if(side == traxColor.WHITE) giveCompatibleRoutesWithMove(whiteRoutes,move)
                  else                        giveCompatibleRoutesWithMove(blackRoutes,move)

    assert(myRoute.length == 1,"Incorrect move!")

    myRoute(0).update(move)

    val oppRoute = if(side ==traxColor.BLACK) giveCompatibleRoutesWithMove(whiteRoutes,move)
                   else                       giveCompatibleRoutesWithMove(blackRoutes,move)

    assert(oppRoute.length < 2,"Incorrect move: Opponent side")


    oppRoute.foreach(_.update(move))

    if(oppRoute.length == 0){
//      var tmpRoute:Route = if(move.TileType == traxTiles.BBWW)
//        tmpRoute = tmpRoute.getNewTerminal(move,)
    }

    automaticMoves

    Success()
  }

  def automaticMoves = {
    var autoMove = 0
    do {
      autoMove = 0

      whiteRoutes = checkListforAutoMoves(whiteRoutes)
      blackRoutes = checkListforAutoMoves(blackRoutes)

    }while(autoMove < 2)


    def checkListforAutoMoves(list:List[Route]): List[Route] = {

      if(list.length < 2){
        autoMove += 1
        return list
      }

      var tmp = list

      for ((outer, idxOut) <- list.zipWithIndex; (inner, idxIn) <- list.zipWithIndex;if(idxIn != idxOut)) {
        inner.doMerge(outer) match {
          case Some(x) => {
            tmp = tmp.diff(List(outer)).diff(List(inner)) ++ List(x)
          }
          case None => {
            autoMove += 1
          }
        }
      }
      tmp
    }
  }


}

object gameState {
  def apply(state:gameState):gameState = {
    val a = new gameState
    a.whiteRoutes = state.whiteRoutes
    a.blackRoutes = state.blackRoutes
    a
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

case class Move(_tile:traxTiles , _pos:Coordinate){
  var TileType  = _tile
  var pos       = _pos
}
object Move{
  def apply(move: Move):Move = {
    new Move(move.TileType,move.pos)
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////