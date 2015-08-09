import java.io._

import Game.World._

import scala.io.Source
import scala.util.matching.Regex

//val patern = new Regex("^\(\(\d\,\d\)\,\S{4}\)")
//val patern = new Regex("^/(")
val a = "((0,0),WWBB)"
val spl = a.replaceAll("[()]","").split(',')

def str2Tile(in: String): traxTiles = {
  if (in == "WWBB") return traxTiles.WWBB
  if (in == "BBWW") return traxTiles.BBWW
  if (in == "WBWB") return traxTiles.WBWB
  if (in == "BWBW") return traxTiles.BWBW
  if (in == "WBBW") return traxTiles.WBBW
  if (in == "BWWB") return traxTiles.BWWB
  return traxTiles.INVALID
}

spl(2) == "WWBB"

val move = Move(str2Tile(spl(2)),Coordinate(spl(0).toInt,spl(1).toInt))