import Game.World._

val a = List(0,1,2,3)

for ((outer,outerIdx) <- a.zipWithIndex;
     (inner,innerIdx) <- a.zipWithIndex.drop(outerIdx+1)) {
  println(outerIdx +"->"+innerIdx)
     }