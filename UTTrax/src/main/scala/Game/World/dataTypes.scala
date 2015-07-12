package Game.World

//import Game.Margin.Margin
import Game.Player.moveFinder
import Game.World.Margin.Margin

import scala.collection.mutable

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
    X += 1;
    this
  }

  def xMinus: Coordinate ={
    X -= 1;
    this
  }

  def yPlus: Coordinate ={
    Y += 1;
    this
  }

  def yMinus: Coordinate ={
    Y -= 1;
    this
  }

}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

class Route extends moveFinder{

  var start = (new Coordinate,Margin.TOP)
  var end = (new Coordinate,Margin.TOP)

  var length = 0

  def Route={
    start = (Coordinate(0,1),Margin.DOWN)
    end = (Coordinate(0,-1),Margin.TOP)
  }


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

  }

  private def getNewTerminal(move: Move , margin: Margin):(Coordinate,Margin) = {
    assert((move.TileType.getVal & (1<< Margin.getVal(margin))) != 0 , "The requested move cannot continue the terminal!")

    val otherMargin = move.TileType.getVal - (1<< Margin.getVal(margin))

    assert((otherMargin & (otherMargin - 1)) == 0 , "OtherMargin is not pow2!" )

    if(otherMargin == 1){ ( move._pos.xMinus, Margin.RIGHT ) }
    else  if(otherMargin == 2){ ( move._pos.xPlus , Margin.LEFT  ) }
    else  if(otherMargin == 4){ ( move._pos.yMinus, Margin.TOP   ) }
    else                      { ( move._pos.yPlus , Margin.DOWN  ) }

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
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

class gameState{
  var whiteRoutes = new mutable.LinkedList[Route]
  var blackRoutes = new mutable.LinkedList[Route]
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

case class Move(_tile:traxTiles , _pos:Coordinate){
  val TileType  = _tile
  val pos       = _pos
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////