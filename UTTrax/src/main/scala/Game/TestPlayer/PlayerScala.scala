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
  }

  def play(): Move

  override def update(move: Move, reAction: lang.Boolean = false): Unit =  {

    println("[INFO] Player "+side+" updated its state with "+move + {if(reAction) ",in reaction mode" else ""})

    state.updateState(move, side)
  }


  def setState(st:gameState) = state = gameState(st)

  def getState():gameState = state

}

