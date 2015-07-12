package Game.Player

import Game.World._

//import TraxWorld._

/**
 * Created by proska on 6/29/15.
 */
trait Player {

  var state = new gameState

  def play(): Move
  def updateState(move:Move):Unit



}

