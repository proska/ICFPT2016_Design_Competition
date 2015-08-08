package Game.TestPlayer

import java.lang

import Game.World._
//import Game.World.traxColor.traxColor

//import TraxWorld._

/**
 * Created by proska on 6/29/15.
 */
trait PlayerScala extends Player{

  val side:traxColor
  var state = new gameState

  override def initialize(): Unit ={
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

  override def update(move: Move, reAction: lang.Boolean = false): Unit =  {

    println("[INFO] Player "+side+" updated its state with "+move + {if(reAction) ",in reaction mode" else ""})

    val sidetmp  = {if(!reAction) side else traxColor.flip(side)}

    state.updateState(move, sidetmp)
  }


  def setState(st:gameState) = state = gameState(st)

  def getState():gameState = state

}

