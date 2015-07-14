import scala.collection.mutable

var tmpList:scala.collection.mutable.MutableList[Int] = null


tmpList = mutable.MutableList(0,1,2,3,4,5,6,7,8,9)
tmpList = tmpList.filter(_ > 5)

tmpList