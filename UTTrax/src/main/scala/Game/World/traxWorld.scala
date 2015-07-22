package Game.World

import GUI.traxGUI
import Game.TestPlayer.{testPlayer, moveFinder, Player}
import Game.World.traxColor.traxColor

import scala.collection.mutable
import scala.util.control.Breaks._
import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 6/29/15.
 */


object traxWorld extends moveFinder {

  var state = new gameState

  var whitePlayer:Player = new testPlayer(traxColor.WHITE)
  var blackPlayer:Player = new testPlayer(traxColor.BLACK)

  def startGame: Unit ={
    startGUI()

//    testGUI
    initializeBoard

    doGame


    println("GAME ENDED!")
//    state.whiteRoutes.append(mutable.LinkedList(new Route))
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  def doGame: Unit = {
    breakable {
      while (isEndofGame() == 0) {

        getPlayerMove(traxColor.WHITE) match {
          case Failure(_) => break()
        }

        getPlayerMove(traxColor.BLACK) match {
          case Failure(_) => break()
        }

      }
    }
  }

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
        blackPlayer.update(move)
      } else {
        whitePlayer.update(move)
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

    traxGUI.addTile(move._pos.X, move._pos.Y, move.TileType)
    try {
      state.updateState(move, side)
      return true
    }
    catch {
      case _ => return false
    }
  }
  private def initializeBoard(): Unit ={
    traxGUI.addTile(0,0, traxTiles.WWBB)
    var tmp = new Route
    tmp.start = (Coordinate(0,1),Margin.DOWN)
    tmp.end   = (Coordinate(0,-1),Margin.TOP)
    tmp.length = 1
    state.whiteRoutes = List(tmp)

    tmp.start = (Coordinate(1,0),Margin.LEFT)
    tmp.end   = (Coordinate(-1,0),Margin.RIGHT)
    tmp.length = 1
    state.blackRoutes = List(tmp)


  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  def main (args: Array[String]): Unit ={
    startGame
  }
}
