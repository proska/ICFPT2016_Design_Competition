package GameWorld

import java.util.concurrent.TimeUnit

import GUI.traxGUI

import scala.collection.mutable

/**
 * Created by proska on 6/29/15.
 */
class Route{

  object Margin extends Enumeration {
    type Margin = Value
    val TOP, BOTTOM, LEFT, RIGHT = Value
  }

  var start = (0,0,Margin.TOP)
  var end = (0,0,Margin.TOP)

  var length = 0
}

class gameState{
  var whiteRoutes = new mutable.LinkedList[Route]
  var blackRoutes = new mutable.LinkedList[Route]
}

class Move(_tile:traxTiles , posX:Int , posY:Int){
  val TileType  = _tile
  val X         = posX
  val Y         = posY
}

object traxWorld {

  var state = new gameState

  var whitePlayer:Player = null
  var blackPlayer:Player = null

  def startGame: Unit ={
    startGUI()

    var i: Int = 0
    while (i <= 5) {
      {
        traxGUI.addTile(i * 2, 0, traxTiles.BBWW)
        try {
          TimeUnit.SECONDS.sleep(0.1.toLong)
        }
        catch {
          case e: InterruptedException => {
          }
        }
        System.out.println(">> 1 sec passed!")
      }
      ({
        i += 1; i - 1
      })
    }
//    for (i <- 0 until 3) {
//      {
//        traxGUI.addTile(i * 2, 0, traxTiles.BBWW)
//        try {
//          TimeUnit.SECONDS.sleep(0.1.toLong)
//        }
//        catch {
//          case e: InterruptedException => {
//          }
//        }
//        System.out.println(">> 1 sec passed!")
//      }
//    }
  }

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


  private def addTile(x: Int, y: Int, tile: traxTiles): Unit ={

  }

  def main (args: Array[String]): Unit ={
    startGame
  }
}
