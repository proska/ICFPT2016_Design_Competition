package Game.TestPlayer

import Game.World._
import Game.World.traxColor.traxColor

//import TraxWorld._

/**
 * Created by proska on 6/29/15.
 */
trait Player {

  val side:traxColor
  var state = new gameState

  def initialize(): Unit ={
    var tmp = new Route
    tmp.start = (Coordinate(0,1),Margin.DOWN)
    tmp.end   = (Coordinate(0,-1),Margin.TOP)
    tmp.length = 1
    state.whiteRoutes = List(tmp)

    var tmp1 = new Route
    tmp1.start = (Coordinate(1,0),Margin.LEFT)
    tmp1.end   = (Coordinate(-1,0),Margin.RIGHT)
    tmp1.length = 1
    state.blackRoutes = List(tmp1)
  }

  def play(): Move

  def update(move: Move):Any = {
    state.updateState(move,side)
  }
}

