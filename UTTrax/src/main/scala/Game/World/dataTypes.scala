package Game.World

//import Game.Margin.Margin
import Game.TestPlayer.moveFinder
import Game.World.Margin.Margin
//import Game.World.traxColor.traxColor

import scala.Option
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 7/12/15.
 */
//object traxColor extends Enumeration {
//  type traxColor = Value
//  val WHITE,BLACK = Value
//
//  def flip(in:traxColor):traxColor = if(in == WHITE) BLACK else WHITE
//}



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
  def flip(margin: Margin):Margin = {
    margin match {
      case TOP => DOWN
      case DOWN => TOP
      case LEFT => RIGHT
      case RIGHT => LEFT
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

  override def toString: String = "("+X+","+Y+")"//super.toString
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

  override def toString: String = start +"<->"+end//super.toString

  def isLoop:Boolean = start._1 == end._1

  def update(move:Move,side:traxColor): Unit ={
    val tmpMove = if(side == traxColor.WHITE) move else move.flip()
    val st = checkStart(tmpMove)
    val en = checkEnd(tmpMove)

    assert( !(st&en) ,"Loop Route!")

    assert( st|en , "Check Terminal Failed!")

    if(st){
      start = getNewTerminal(move,start._2,side)
    } else if(en){
      end = getNewTerminal(move,end._2,side)
    }

    length +=1

  }

  def getNewTerminal(move: Move , margin: Margin,side:traxColor):(Coordinate,Margin) = {

    val tmpMove = if(side == traxColor.WHITE) move else move.flip()

    assert((tmpMove.TileType.getVal & (1<< Margin.getVal(margin))) != 0 , "The requested move cannot continue the terminal!")

    val otherMargin = tmpMove.TileType.getVal - (1<< Margin.getVal(margin))

    assert((otherMargin & (otherMargin - 1)) == 0 , "OtherMargin is not pow2!" )

    if(otherMargin == 1){ ( tmpMove.pos.xMinus, Margin.RIGHT ) }
    else  if(otherMargin == 2){ ( tmpMove.pos.xPlus , Margin.LEFT  ) }
    else  if(otherMargin == 4){ ( tmpMove.pos.yMinus, Margin.TOP   ) }
    else                      { ( tmpMove.pos.yPlus , Margin.DOWN  ) }

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

  def doMerge(that:Route ,serverF:Move => Any = null , isBlack:Boolean = false):Option[Route] = {
    var contact: (Terminal,Terminal) = null
    var out:Option[Route] = None

    if(this.end._1 == that.end._1){
      contact = (this.end , that.end);out = Some(Route(this.start,that.start,this.length + that.length + 1))
    } else if(this.end._1 == that.start._1){
      contact = (this.end , that.start);out = Some(Route(this.start,that.end,this.length + that.length + 1))
    } else if(this.start._1 == that.end._1){
      contact = (this.start , that.end);out = Some(Route(this.end,that.start,this.length + that.length + 1))
    } else if(this.start._1 == that.start._1){
      contact = (this.start , that.start);out = Some(Route(this.end,that.end,this.length + that.length + 1))
    } else
      out = None

    serverF match {
      case null =>
      case _ => out match {
        case Some(_) => {
          println("[INFO] Automatic move:"+Move(contact._1._1,contact._1._2,contact._2._2,isBlack)+" is done.")
          serverF(Move(contact._1._1,contact._1._2,contact._2._2,isBlack))
        }
        case _ =>
      }
    }


    out
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
  def apply( pos:Coordinate , m1:Margin , m2:Margin , isWhite:Boolean):Route = {


    var marg1: Margin = {
      if (!isWhite) m1 else Margin.flip(m1)
    }
    var marg2: Margin = {
      if (!isWhite) m2 else Margin.flip(m2)
    }

    if(m1 == Margin.TOP && m2 == Margin.DOWN && isWhite){
      marg1 = Margin.LEFT
      marg2 = Margin.RIGHT
    }

    if(m1 == Margin.LEFT && m2 == Margin.RIGHT && isWhite){
      marg1 = Margin.TOP
      marg2 = Margin.DOWN
    }

    apply((pos,marg1),(pos,marg2),1)
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

class gameState extends moveFinder{
  var whiteRoutes : List[Route] = _
  var blackRoutes : List[Route] = _

  def updateState(move: Move,side:traxColor , serverF:Move => Any = null):Try[_]= {


    val (myRoute: List[Route], oppRoute: List[Route]) = try {
      giveAllCompatibleRoutes(move, side)
    }
    catch {
      case _:Throwable => throw new IllegalArgumentException("giveAllCompatibleRoutes failed!")
    }

    myRoute(0).update(move,side)

    if(oppRoute.length == 1)
      oppRoute.foreach(_.update(move,traxColor.flip(side)))

    try {
      if (oppRoute.length == 0) {

        val tmpRoute: Route = getNewlyAddedRoute(move, traxColor.flip(side))

        if (side == traxColor.WHITE)
          blackRoutes = blackRoutes ++ List(tmpRoute)
        else
          whiteRoutes = whiteRoutes ++ List(tmpRoute)
      }
    }
    catch {
      case _:Throwable => throw new IllegalArgumentException("add new Route failed!")
    }


    try {
      automaticMoves(serverF)
    }
    catch {
      case _:Throwable => new IllegalArgumentException("Auto Move Failed!")
    }

    Success()
  }

  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////

  def getNewlyAddedRoute(move: Move, side: traxColor): Route = {
    def isSideWhite: Boolean = {
      side == traxColor.WHITE
    }
    var tmpRoute = if (move.TileType == traxTiles.BBWW) Route(move.pos, Margin.TOP, Margin.DOWN, isSideWhite)
    else if (move.TileType == traxTiles.BWBW) Route(move.pos, Margin.TOP, Margin.RIGHT, isSideWhite)
    else if (move.TileType == traxTiles.BWWB) Route(move.pos, Margin.TOP, Margin.LEFT, isSideWhite)
    else if (move.TileType == traxTiles.WBBW) Route(move.pos, Margin.DOWN, Margin.RIGHT, isSideWhite)
    else if (move.TileType == traxTiles.WBWB) Route(move.pos, Margin.DOWN, Margin.LEFT, isSideWhite)
    else if (move.TileType == traxTiles.WWBB) Route(move.pos, Margin.LEFT, Margin.RIGHT, isSideWhite)
    else new Route

    tmpRoute.start = tmpRoute.getNewTerminal(move, tmpRoute.start._2,side)
    tmpRoute.end = tmpRoute.getNewTerminal(move, tmpRoute.end._2,side)

    tmpRoute
  }


  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////

  private def giveAllCompatibleRoutes(move: Move, side: traxColor): (List[Route], List[Route]) = {
    val myRoute = if (side == traxColor.WHITE) giveCompatibleRoutesWithMove(whiteRoutes, move)
    else giveCompatibleRoutesWithMove(blackRoutes, move.flip())

    assert(myRoute.length == 1, "Incorrect move!")

    val oppRoute = if (side == traxColor.BLACK) giveCompatibleRoutesWithMove(whiteRoutes, move)
    else giveCompatibleRoutesWithMove(blackRoutes, move.flip())

    assert(oppRoute.length < 2, "Incorrect move: Opponent side")
    (myRoute, oppRoute)
  }

  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////

  def automaticMoves(serverF:Move => Any = null) = {
    var autoMove = 0
    do {
      autoMove = 0

      whiteRoutes = checkListforAutoMoves(whiteRoutes,false)
      blackRoutes = checkListforAutoMoves(blackRoutes,true)

    }while(autoMove < 2)


    def checkListforAutoMoves(list:List[Route],isBlack:Boolean): List[Route] = {

      if(list.length < 2){
        autoMove += 1
        return list
      }

      var tmp = list

      var flag =true

      var test = 0

      for ((outer,outerIdx) <- list.zipWithIndex;
           (inner,innerIdx) <- list.zipWithIndex.drop(outerIdx+1)) {
        inner.doMerge(outer,serverF,isBlack) match {
          case Some(x) => {
            tmp = tmp.diff(List(outer)).diff(List(inner)) ++ List(x)
            flag = false
            assert(tmp.length == list.length - 1 , "error in modifing routes in auto move")
            test +=1
          }
          case None => {

          }
        }
      }
      if(flag)
        autoMove += 1

      tmp
    }
  }


}

object gameState {
  def apply(state:gameState):gameState = {
    val a = new gameState
    a.whiteRoutes = state.whiteRoutes.map(x => Route(x))
    a.blackRoutes = state.blackRoutes.map(x => Route(x))
    a
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

case class Move(_tile:traxTiles , _pos:Coordinate){
  var TileType  = _tile
  var pos       = _pos


  def flip() : Move = {
    Move(TileType.flip(),pos)
  }

  override def toString: String = "("+pos+","+TileType+")"//super.toString
}
object Move{
  def apply(move: Move):Move = {
    new Move(move.TileType,move.pos)
  }
  def apply(coordinate: Coordinate,margin1: Margin,margin2: Margin,isBlack :Boolean):Move = {

    var marg1 = if(isBlack) Margin.flip(margin1) else margin1
    var marg2 = if(isBlack) Margin.flip(margin2) else margin2

    if(margin1 == Margin.TOP && margin2 == Margin.DOWN && isBlack){
      marg1 = Margin.LEFT
      marg2 = Margin.RIGHT
    }

    if(margin1 == Margin.LEFT && margin2 == Margin.RIGHT && isBlack){
      marg1 = Margin.TOP
      marg2 = Margin.DOWN
    }

    val tileNum = (1<<Margin.getVal(marg1)) + (1<<Margin.getVal(marg2))
    val tile = traxTiles.INVALID.num2Tile(tileNum)
    Move(tile,coordinate)
  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////