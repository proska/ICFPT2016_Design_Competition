package Game.World

import GUI.traxGUI
import Game.MinMaxPlayer.AlphaBeta
import Game.montecarlo.MontecarloAlgorithm

import scala.io.Source
import scala.util.matching.Regex

//import Game.GeneticAlgorithmPlayer.GAPlayer
import Game.TestPlayer._
//import Game.World.traxColor.traxColor

import scala.collection.mutable
import scala.util.control.Breaks._
import scala.util.{Failure, Success, Try}
import java.io._
/**
 * Created by proska on 6/29/15.
 */


object traxWorld {

  var state = new gameState

  var whitePlayer:Player = new testPlayerScala(traxColor.WHITE)//new GAPlayer(false)//
  var blackPlayer:Player = new MontecarloAlgorithm(traxColor.BLACK)//new AlphaBeta(traxColor.BLACK)//new testPlayerScala(traxColor.BLACK)//

  val gui = new traxGUI(30)

  def startGame: Unit ={

    //    testGUI

    initializeBoard

    try {
      doGame
    }
    catch {
      case e:Exception => pw.close()
        println("GAME FAILED!")
        println(e.getMessage)
    } finally {
      pw.close()
      val a = 0
    }




    println("GAME ENDED!")
    //    state.whiteRoutes.append(mutable.LinkedList(new Route))
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  var counter = 0;
  val LIMIT = 15;
  def doGame: Unit = {

    var gameEnd = 0

    breakable {
      while ( gameEnd == 0 && counter < LIMIT ) {

        println("----------------------------------")

        getPlayerMove(traxColor.BLACK) match {
          case Failure(_) => break()
          case Success(_) =>
        }
        println("----------------------------------")

        getPlayerMove(traxColor.WHITE) match {
          case Failure(_) => break()
          case Success(_) =>
        }

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

      if(side == traxColor.WHITE){
        blackPlayer.update(move)
      } else {
        whitePlayer.update(move)
      }

      assert(whitePlayer.getState().compare(blackPlayer.getState()) , "state mismatch!")
      assert(whitePlayer.getState().compare(state) , "state mismatch!")


      Success(0)
    } else {
      Failure(new IllegalArgumentException)
    }
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////

  var doDump:Boolean = false
  private def assignMove(move: Move , side:traxColor):Boolean = {

    println("[INFO] Server is adding player "+side+"'s move:"+move+" to Map." )

    doDump = true
    addMovetoGUI(move)
    doDump = false

    println("[INFO] Server is updating player "+side+"'s move:"+move+" in states." )
    state.updateState(move, side,addMovetoGUI)
    return true

  }
  val pw = new PrintWriter(new File("dump.txt"))
  private def addMovetoGUI(move: Move) = {
    gui.addTile(move.TileType,move.pos)
    if(doDump)
      pw.append(move.toString + "\n")
  }

  private def testBoardInitializer(move: Move): Unit ={
    println("[ASSERT] init Move:"+move)
    addMovetoGUI(move)
    state.updateState(move,traxColor.WHITE,addMovetoGUI)
  }

  private def initializeBoard(): Unit ={

    testBoardInitializer(Move(traxTiles.WWBB,Coordinate(0,0)))

    readInitFile

    whitePlayer.setState(state)
    blackPlayer.setState(state)

    whitePlayer.initialize()
    blackPlayer.initialize()

    def str2Tile(in: String): traxTiles = {
      if (in == "WWBB") return traxTiles.WWBB
      if (in == "BBWW") return traxTiles.BBWW
      if (in == "WBWB") return traxTiles.WBWB
      if (in == "BWBW") return traxTiles.BWBW
      if (in == "WBBW") return traxTiles.WBBW
      if (in == "BWWB") return traxTiles.BWWB
      return traxTiles.INVALID
    }

    def readInitFile: Unit = {
      val a = Source.fromFile("dumpin.txt").getLines()

      for (line <- a) {
        val spl = line.replaceAll("[()]","").split(',')
        val move = Move(str2Tile(spl(2)),Coordinate(spl(0).toInt,spl(1).toInt))
        testBoardInitializer(move)
      }
    }
  }

  ////////////////////////////////////
  ////////////////////////////////////
  ////////////////////////////////////
  def main (args: Array[String]): Unit ={
    startGame

  }
}
