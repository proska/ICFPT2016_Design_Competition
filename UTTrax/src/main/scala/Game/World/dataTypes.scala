package Game.World

//import Game.Margin.Margin
import Game.TestPlayer.moveFinder
import Game.TestPlayer.moveFinder.Terminal
import Game.World.Margin.Margin
import Game.montecarlo.MontecarloAlgorithm

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

class Route {

  var start = new moveFinder.Terminal(new Coordinate,Margin.TOP)
  var end = new moveFinder.Terminal(new Coordinate,Margin.TOP)

  var length = 0

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
    moveFinder.isTerminalCompatibleWithMove(end, move)
  }

  private def checkStart(move: Move): Boolean = {
    moveFinder.isTerminalCompatibleWithMove(start, move)
  }

  def doMerge(that:Route ,serverF:Move => Any = null , isBlack:Boolean = false , stateUpdate:Move =>Any = null):Option[(Route,Move)] = {
    var contact: (Terminal,Terminal) = null
    var out:Route = null

    if(this.end._1 == that.end._1){
      contact = (this.end , that.end);out = (Route(this.start,that.start,this.length + that.length + 1))
    } else if(this.end._1 == that.start._1){
      contact = (this.end , that.start);out = (Route(this.start,that.end,this.length + that.length + 1))
    } else if(this.start._1 == that.end._1){
      contact = (this.start , that.end);out = (Route(this.end,that.start,this.length + that.length + 1))
    } else if(this.start._1 == that.start._1){
      contact = (this.start , that.start);out = (Route(this.end,that.end,this.length + that.length + 1))
    } else
      out = null

    serverF match {
      case null =>
      case _ => out match {
        case _:Route => {
          val newMove = Move(contact._1._1,contact._1._2,contact._2._2,isBlack)
          println("[INFO] Automatic move:"+newMove+" is done.")
          if(newMove._tile == traxTiles.INVALID){
            println("Error")
          }
          serverF(newMove)
        }
        case _ =>
      }
    }

    if(out != null)
      Some(out,Move(contact._1._1,contact._1._2,contact._2._2,isBlack))
    else
      None
  }

  def compare(that:Route): Boolean = {
    this.start == that.start &&
      this.end   == that.end   &&
      this.length == that.length
  }

}

object Route {
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

class gameState{
  var whiteRoutes : List[Route] = List()
  var blackRoutes : List[Route] = List()


  def dump: Any = {
    println("[TEST] WhiteRoute:");
    this.whiteRoutes.foreach(x => println(x))
    println("[TEST] BlackRoutes:");
    this.blackRoutes.foreach(x => println(x))
  }

  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////

  def updateStateNew(move: Move,side:traxColor , serverF:Move => Any = null):Try[_]= {

    def giveAdjacentCoordinates(coordinate: Coordinate) : List[Coordinate] = {
      List(
        Coordinate(coordinate.X + 1 , coordinate.Y    ) ,
        Coordinate(coordinate.X - 1 , coordinate.Y    ) ,
        Coordinate(coordinate.X     , coordinate.Y + 1) ,
        Coordinate(coordinate.X     , coordinate.Y - 1))
    }

    var autoList = giveAdjacentCoordinates(move._pos)

    var needUpdate= true

    var newMove = move

    var autoMove:List[Move] = List()


    updateStateWithMove

    while(autoList.length > 0){

      checkMEcooridnates(autoList(0),whiteRoutes,traxColor.WHITE)
      checkMEcooridnates(autoList(0),blackRoutes,traxColor.BLACK)

      autoList = autoList.drop(1)
    }

    println("Nothing")


    def updateRouteList(list:List[Route],color:traxColor): Unit = {

      val tmpRouteList = moveFinder.giveCompatibleRoutesWithMove(list, {if(color == traxColor.WHITE)newMove else newMove.flip()})

      assert(tmpRouteList.length < 3, "Illegal Move!")

      if (tmpRouteList.length == 0) {
        val tmpRoute: Route = getNewlyAddedRoute(newMove, color)

        if(color == traxColor.WHITE)
          whiteRoutes = list ++ List(tmpRoute)
        else
          blackRoutes = list ++ List(tmpRoute)
      }

      if (tmpRouteList.length == 1) {
        println("[TEST] Route Updated"+tmpRouteList(0).hashCode())
        tmpRouteList(0).update(newMove, color)
      }
    }

    def checkMEcooridnates(pos: Coordinate , list: List[Route] , color:traxColor):Unit = {

      val tmpList = list.filter(x => (x.start._1 == pos) || (x.end._1 == pos) )

      assert(tmpList.length <=2 ,"Invalid Update: more that 2 comming routes")

      if(tmpList.length == 2){

        val isBlack = color == traxColor.BLACK
        tmpList(0).doMerge(tmpList(1), serverF, isBlack, null) match {
          case Some(x) => {

            needUpdate = true

            autoList = autoList ++ giveAdjacentCoordinates(pos)

            newMove = x._2

            if(color == traxColor.WHITE){
              whiteRoutes = list.diff(List(tmpList(0))).diff(List(tmpList(1))) ++ List(x._1)
              println("[ASSERT] BlackDone")
              updateRouteList(blackRoutes, traxColor.BLACK)
            }
            else{
              blackRoutes = list.diff(List(tmpList(0))).diff(List(tmpList(1))) ++ List(x._1)
              println("[ASSERT] whiteDone")
              updateRouteList(whiteRoutes, traxColor.WHITE)
            }
          }
          case _ => //autoList = autoList.drop(0)
        }
      }
    }

    def updateStateWithMove: Unit = {
      println("[ASSERT] Update States")

      updateRouteList(whiteRoutes, traxColor.WHITE)
      println("[ASSERT] whiteDone")
      updateRouteList(blackRoutes, traxColor.BLACK)
      println("[ASSERT] BlackDone")
    }


    Success(null)
  }

  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////

  def updateState(move: Move,side:traxColor , serverF:Move => Any = null):Try[_]= {


    val (myRoute: List[Route], oppRoute: List[Route]) = try {
      giveAllCompatibleRoutes(move, side)
    }
    catch {
      case _:Throwable => throw new IllegalArgumentException("giveAllCompatibleRoutes failed!")
    }

    if(myRoute.length == 1)
      myRoute(0).update(move,side)


    try{
      if(oppRoute.length == 1){

        val prevlen = if(side == traxColor.WHITE) blackRoutes.length else whiteRoutes.length
        println("[TEST] RouteUpdated:"+oppRoute)
        oppRoute.foreach(_.update(move,traxColor.flip(side)))
        println("[TEST] RouteUpdated:"+oppRoute)
        assert({if(side == traxColor.WHITE) blackRoutes.length else whiteRoutes.length} == prevlen , "Wrong oppRoute Update!")
      }
    } catch {
      case _:Throwable => throw new IllegalArgumentException("updating Opp Route Failed!")
    }

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
    else {throw new IllegalArgumentException();new Route}

//    println("[TEST] RouteAdded:"+tmpRoute)

    tmpRoute.start = tmpRoute.getNewTerminal(move, tmpRoute.start._2,side)
    tmpRoute.end = tmpRoute.getNewTerminal(move, tmpRoute.end._2,side)

    println("[TEST] RouteAdded:"+tmpRoute)
    tmpRoute
  }


  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////

  private def giveAllCompatibleRoutes(move: Move, side: traxColor): (List[Route], List[Route]) = {
    val myRoute = if (side == traxColor.WHITE) moveFinder.giveCompatibleRoutesWithMove(whiteRoutes, move)
    else moveFinder.giveCompatibleRoutesWithMove(blackRoutes, move.flip())

    assert(myRoute.length == 2 || myRoute.length == 1, "Incorrect move!")

    if(myRoute.length == 2)
      println("####[AUTO MOVE]####")

    val oppRoute = if (side == traxColor.BLACK) moveFinder.giveCompatibleRoutesWithMove(whiteRoutes, move)
    else moveFinder.giveCompatibleRoutesWithMove(blackRoutes, move.flip())

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

      def selfUpdate(move: Move): Unit ={
        this.updateState(move,{if(isBlack) traxColor.BLACK else traxColor.WHITE})
      }

      for ((outer,outerIdx) <- list.zipWithIndex;
           (inner,innerIdx) <- list.zipWithIndex.drop(outerIdx+1)) {
        inner.doMerge(outer,serverF,isBlack,selfUpdate) match {
          case Some(x) => {
            tmp = tmp.diff(List(outer)).diff(List(inner)) ++ List(x._1)
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

  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////

  def compare(that:gameState):Boolean = {
    this.whiteRoutes.zip(that.whiteRoutes).map(x => x._1 compare x._2).reduceLeft(_&&_) &
      this.blackRoutes.zip(that.blackRoutes).map(x => x._1 compare x._2).reduceLeft(_&&_)
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
object Move {
  def apply(move: Move): Move = {
    new Move(move.TileType, move.pos)
  }

  def apply(coordinate: Coordinate, margin1: Margin, margin2: Margin, isBlack: Boolean): Move = {

    var marg1 = if (isBlack) Margin.flip(margin1) else margin1
    var marg2 = if (isBlack) Margin.flip(margin2) else margin2

    if (margin1 == Margin.TOP && margin2 == Margin.DOWN && isBlack) {
      marg1 = Margin.LEFT
      marg2 = Margin.RIGHT
    }

    if (margin1 == Margin.LEFT && margin2 == Margin.RIGHT && isBlack) {
      marg1 = Margin.TOP
      marg2 = Margin.DOWN
    }

    val tileNum = (1 << Margin.getVal(marg1)) + (1 << Margin.getVal(marg2))
    val tile = traxTiles.INVALID.num2Tile(tileNum)
    Move(tile, coordinate)
  }

  //    def apply(move:Game.montecarlo.MontecarloAlgorithm.TreeNode.Move):Move {
  //        Move movefinal = new Move
  //        movefinal. = move.x;
  //
  //
  //     return movefinal;
  //  }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////