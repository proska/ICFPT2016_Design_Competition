package Game.World

import GUI.traxGUI
import Game.Player.{moveFinder, Player}
import Game.World.traxColor.traxColor

import scala.util.control.Breaks._
import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 6/29/15.
 */


object traxWorld extends moveFinder {

  var state = new gameState

  var whitePlayer:Player = null
  var blackPlayer:Player = null

  def startGame: Unit ={
    startGUI()

    testGUI

//    breakable{
//      while( isEndofGame() == 0 ){
//
//        getPlayerMove(traxColor.WHITE) match {
//          case Failure(_) => break()
//        }
//
//        getPlayerMove(traxColor.BLACK) match {
//          case Failure(_) => break()
//        }
//
//      }
//    }


    println("GAME ENDED!")
//    state.whiteRoutes.append(mutable.LinkedList(new Route))
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  private def testGUI: Unit = {
    var i: Int = 0
    for (i <- 0 to 7) {
      traxGUI.addTile(i * 2, 0, traxTiles.BBWW)
    }
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  private def startGUI(): Unit ={
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(
      new Runnable {
        def run {
          traxGUI.startGUI();
        }
      }
    )
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  private def isEndofGame():Int = {

    def ifLoop(route: Route):Boolean = {
      route.start._1 == route.end._2
    }


    if (state.whiteRoutes.map(x => ifLoop(x)).reduceLeft(_||_))  1
    else if(state.blackRoutes.map(x => ifLoop(x)).reduceLeft(_||_)) 2
    else 0


  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  private def getPlayerMove(side:traxColor): Try[Any] ={
    val move = if(side == traxColor.WHITE ) whitePlayer.play() else blackPlayer.play()

    if(assignMove(move,side)){

      if(side == traxColor.WHITE){
        blackPlayer.updateState(move)
      } else {
        whitePlayer.updateState(move)
      }

      Success(0)
    } else {
      Failure(new IllegalArgumentException)
    }
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  private def assignMove(move: Move , side:traxColor):Boolean = {

    val indexes = giveCompatibleRoutesWithMove({if(side == traxColor.WHITE) state.whiteRoutes else state.blackRoutes}
      .toList, move)

    assert(indexes.length < 2, "Problem in White players routeList")

    if (indexes.length == 0) {
      println("Invalid move from White player!")
      return false
    } else {

      {if(side == traxColor.WHITE) state.whiteRoutes else state.blackRoutes }.apply(indexes(0)).update(move)
      traxGUI.addTile(move._pos.X, move._pos.Y, move.TileType)
      return true
    }

  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  def main (args: Array[String]): Unit ={
    startGame
  }
}
