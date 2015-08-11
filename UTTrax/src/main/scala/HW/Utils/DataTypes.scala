package HW.Utils

import Chisel._
/**
 * Created by mohammad on 8/7/15.
 */
class CoordinateT extends Bundle {

  val X = UInt(width = 5)
  val Y = UInt(width = 5)

  def toRelativeBase: CoordinateT = {
    // TODO Body is not implemented
    this
  }
}

class TerminalT extends Bundle{
  val pos = new CoordinateT
}

class MoveT extends Bundle {

}
