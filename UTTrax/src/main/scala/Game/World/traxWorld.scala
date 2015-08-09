package Game.World

import GUI.traxGUI
import Game.MinMaxPlayer.AlphaBeta

//import Game.GeneticAlgorithmPlayer.GAPlayer
import Game.TestPlayer._
//import Game.World.traxColor.traxColor

import scala.collection.mutable
import scala.util.control.Breaks._
import scala.util.{Failure, Success, Try}

/**
 * Created by proska on 6/29/15.
 */


object traxWorld {

  var state = new gameState

  var whitePlayer:Player = new testPlayerScala(traxColor.WHITE)//new GAPlayer(false)//
  var blackPlayer:Player = new testPlayerScala(traxColor.BLACK)//new AlphaBeta(traxColor.BLACK)//new testPlayerScala(traxColor.BLACK)//

  val gui = new traxGUI(30)

  def startGame: Unit ={

    //    testGUI

    initializeBoard

    doGame


    println("GAME ENDED!")
    //    state.whiteRoutes.append(mutable.LinkedList(new Route))
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  var counter = 0;
  val LIMIT = 10;
  def doGame: Unit = {

    var gameEnd = 0
    val a = ()

    breakable {
      while ( gameEnd == 0 && counter < LIMIT ) {

        println("----------------------------------")

        getPlayerMove(traxColor.BLACK) match {
          case Failure(_) => break()
          case Success(_) =>
        }
        println("----------------------------------")

        assert(whitePlayer.getState().compare(blackPlayer.getState()) , "state mismatch!")
        assert(whitePlayer.getState().compare(state) , "state mismatch!")

        getPlayerMove(traxColor.WHITE) match {
          case Failure(_) => break()
          case Success(_) =>
        }


        assert(whitePlayer.getState().compare(blackPlayer.getState()) , "state mismatch!")
        assert(whitePlayer.getState().compare(state) , "state mismatch!")

        println("----------------------------------")
        counter +=1
        gameEnd = isEndofGame()
      }
    }

    if(counter == LIMIT){
      println("Move Limit Reached")
    }

    if(gameEnd == 1){
      println("White Player Won!")
    } else if(gameEnd == 2){
      println("Black Player Won!")
    }

  }

  private def testGUI: Unit = {
    var i: Int = 0
    for (i <- 0 to 10) {
      //      gui.addTile(0, -i, traxTiles.BBWW)
      addMovetoGUI(Move(traxTiles.BBWW,Coordinate(0,-i)))
    }
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////


  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  private def isEndofGame():Int = {

    def ifLoop(route: Route):Boolean = {
      route.start._1 == route.end._1
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

      //      val a = whitePlayer.getState().compare(blackPlayer.getState())

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

    println("[INFO] Server is adding player "+side+"'s move:"+move+" to Map." )

    //addMovetoGUI(move)

    gui.addTile(move.TileType,move.pos)

    //    gui.addTile(move._pos.X, move._pos.Y, move.TileType)

    println("[INFO] Server is updating player "+side+"'s move:"+move+" in states." )
    state.updateState(move, side,addMovetoGUI)
    return true

  }

  private def addMovetoGUI(move: Move) = {
    gui.addTile(move.TileType,move.pos)
  }

  private def testBoardInitializer(move: Move): Unit ={
    addMovetoGUI(move)
    state.updateState(move,traxColor.WHITE,addMovetoGUI)
  }

  private def initializeBoard(): Unit ={

    testBoardInitializer(Move(traxTiles.WWBB,Coordinate(0,0)))


        testBoardInitializer(Move(traxTiles.WBBW,Coordinate(0,-1)))
        testBoardInitializer(Move(traxTiles.BBWW,Coordinate(-1,-1)))
        testBoardInitializer(Move(traxTiles.BBWW,Coordinate(-2,-1)))
        testBoardInitializer(Move(traxTiles.BWBW,Coordinate(0,1)))
        testBoardInitializer(Move(traxTiles.BBWW,Coordinate(-3,-1)))
        testBoardInitializer(Move(traxTiles.WBBW,Coordinate(-1,2)))
        testBoardInitializer(Move(traxTiles.BBWW,Coordinate(-4,-1)))
//        testBoardInitializer(Move(traxTiles.BBWW,Coordinate(-3,0)))

    whitePlayer.setState(state)
    blackPlayer.setState(state)


    whitePlayer.initialize()
    blackPlayer.initialize()

  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  def main (args: Array[String]): Unit ={
    startGame

  }
}
