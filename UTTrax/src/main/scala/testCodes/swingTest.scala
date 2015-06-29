package testCodes

/**
 * Created by proska on 6/11/15.
 */

//import java.awt.Button
//
//import scala.swing._
//
//import java.awt.Dimension
//import swing._
//import swing.event._
//
//object HelloWorld extends SimpleSwingApplication {
//  def top = new MainFrame {
//    title = "Hello, World!"
//    val c:Component = (new Button {"Click Me!"})
//    contents = (new Button {"Click Me!"})
//  }
//}


//import helloSwing_form
//
//import swing._

/**
 * A simple swing demo.
 */
object HelloWorld {

  def main(args: Array[String]): Unit = {
    val chiselArgs = args.slice(1, args.length)
    val gui = new helloSwing_form()

//    gui.setUpGUI()

    for(i<- 0 to 4){
      gui.setImageIndex(i)
//      for(j <- 0 until 10000){
//
//      }
    }

  }
}