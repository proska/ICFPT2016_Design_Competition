//package HW.Utils
//
//import Chisel._
//import Game.World.{Margin, traxWorld}
//
///**
//* Created by mohammad on 8/24/15.
//*/
//class UpdateMap extends Module{
//  val io = new Bundle{
//    val wRamPort = new RamPort_HW().flip()
//    val bRamPort = new RamPort_HW().flip()
//    val newMove =  new DecoupledIO(new Move_HW).flip()
//    val wCount = UInt(INPUT, width = 5)
//    val bCount = UInt(INPUT, width = 5)
//  }
//
//  val idle :: findCoordinateInRam :: updateMap:: writeToFIFO :: addToFifo :: checkWithRout :: end  :: Nil = Enum (UInt(), 7)
//  val state = Reg (init = idle)
//  val iBuffer = Module(new Queue(new Coordinate_HW , 8))
//  val wAutoFifo = Module(new Queue(new TerminalIndex_HW , 8))
//  val bAutoFifo = Module(new Queue(new TerminalIndex_HW , 8))
//  val bReg = Reg(new Terminal_HW)
//  val wReg = Reg(new Terminal_HW)
//  val moveReg = Reg(new Move_HW)
//  val first =Reg(init = Bool(false))
//  val wAddrReg = Reg(init = UInt(0 , 5))
//  val bAddrReg = Reg(init = UInt(0 , 5))
//  val blackCounter = Reg(init = UInt(0 , 2))
//  val whiteCounter = Reg(init = UInt(0 , 2))
//  val wIndex1 = Reg(init = new TerminalIndex_HW)
//  val wIndex2 = Reg(init = new TerminalIndex_HW)
//  val bIndex1 = Reg(init = new TerminalIndex_HW)
//  val bIndex2 = Reg(init = new TerminalIndex_HW)
//
//  //Defaults
//  io.bRamPort.req.valid := Bool(false)
//  io.wRamPort.req.valid := Bool(false)
//  io.bRamPort.resp.ready := Bool(true)
//  io.wRamPort.resp.ready := Bool(true)
//  io.newMove.ready := Bool(false)
//  switch(state){
//    is(idle){
//      io.newMove.ready := Bool(true)
//      when(io.newMove.valid){
//        moveReg := io.newMove.bits
//        first := Bool(true)
//        state := findCoordinateInRam
//        bAddrReg := UInt(0)
//        wAddrReg := UInt(0)
//      }
//    }
//    is(findCoordinateInRam){
//      io.wRamPort.req.valid := Bool(true)
//      io.bRamPort.req.valid := Bool(true)
//      when(first){
//        when(moveReg.coord === io.wRamPort.resp.bits.readData.start.coord){
//          wIndex2 := wIndex1
//          wIndex1.ramIndex := wAddrReg
//          wIndex1.startTerminal := Bool(true)
//          whiteCounter := whiteCounter + UInt(1)
//
//        }.elsewhen(moveReg.coord === io.wRamPort.resp.bits.readData.end.coord){
//          wIndex2 := wIndex1
//          wIndex1.ramIndex := wAddrReg
//          wIndex1.startTerminal := Bool(false)
//          whiteCounter := whiteCounter + UInt(1)
//        }
//
//        when(moveReg.coord === io.bRamPort.resp.bits.readData.start.coord){
//          bIndex2 := bIndex1
//          bIndex1.ramIndex := bAddrReg
//          bIndex1.startTerminal := Bool(true)
//          blackCounter := blackCounter + UInt(1)
//
//        }.elsewhen(moveReg.coord === io.wRamPort.resp.bits.readData.end.coord){
//          bIndex2 := bIndex1
//          wIndex1.ramIndex := wAddrReg
//          wIndex1.startTerminal := Bool(false)
//          whiteCounter := whiteCounter + UInt(1)
//        }
//
//        when(bAddrReg === io.bCount-UInt(1) && wAddrReg === io.wCount-UInt(1)){
//          state := updateMap
//        }
//
//        when(bAddrReg != io.bCount - UInt(1)){
//          bAddrReg := bAddrReg +UInt(1)
//        }
//
//        when(wAddrReg != io.wCount - UInt(0)){
//          wAddrReg := wAddrReg +UInt(1)
//        }
//
//
//
//      }
//
//    }
//    is(checkWithRout){
//
//      bAddrReg := UInt(0)
//      wAddrReg := UInt(0)
//
//      io.wRamPort.req.valid := Bool(true)
//      io.bRamPort.req.valid := Bool(true)
//
//      wAutoFifo.do_deq
//      bAutoFifo.do_deq
//
//      wReg := wAutoFifo.io.deq.bits
//      bReg := bAutoFifo.io.deq.bits
//
//
//      when(wReg === null && bReg === null){
//        state := end
//      }
//
//
//      when (io.wRamPort.resp.bits.readData.start.coord === wReg.coord){
//        whiteCounter :=  UInt(0) /////////nemidunam har bar khodesh 0 mishe ya na
//        state := findCoordinateInRam
//      }.elsewhen(io.wRamPort.resp.bits.readData.end.coord === wReg.coord){
//        whiteCounter :=  UInt(0) /////////nemidunam har bar khodesh 0 mishe ya na
//        state := findCoordinateInRam
//      }.otherwise{
//        whiteCounter := whiteCounter + UInt(1)
//        wAddrReg := wAddrReg +UInt(1)
//      }
//
//
//      when (io.bRamPort.resp.bits.readData.start.coord === bReg.coord){
//        blackCounter :=  UInt(0) /////////nemidunam har bar khodesh 0 mishe ya na
//        state := findCoordinateInRam
//      }.elsewhen(io.bRamPort.resp.bits.readData.end.coord === bReg.coord){
//        blackCounter :=  UInt(0) /////////nemidunam har bar khodesh 0 mishe ya na
//        state := findCoordinateInRam
//      }.otherwise{
//        blackCounter := blackCounter + UInt(1)
//        bAddrReg := bAddrReg +UInt(1)
//      }
//
////      when(bAddrReg != io.bCount - UInt(1)){
////        bAddrReg := bAddrReg +UInt(1)
////      }
////
////      when(wAddrReg != io.wCount - UInt(1)){
////        wAddrReg := wAddrReg +UInt(1)
////      }
//
//
//    }
//
//    io.wRamPort.req.bits.addr := wAddrReg
//    io.bRamPort.req.bits.addr := bAddrReg
//
//
//  }
//}
//
////class updateMapTest (c: Top) extends Tester(c){
////  val state = traxWorld.getGameState()
////  val moveList = traxWorld.getMovesList()
////  step(2)
////  poke(c.moveFinderModule.iBuffer.io.deq.ready,0)
////
////  for(i <- 0 to state.whiteRoutes.length-1 ){
////    poke(c.io.wRamPort.req.bits.addr, i)
////    poke(c.io.wRamPort.req.bits.write, 1)
////    poke(c.io.wRamPort.req.bits.writeData.start.coord.x, state.whiteRoutes(i).start._1.X)
////    poke(c.io.wRamPort.req.bits.writeData.start.coord.y, state.whiteRoutes(i).start._1.Y)
////    poke(c.io.wRamPort.req.bits.writeData.start.margin.margin, Margin.getVal(state.whiteRoutes(i).start._2))
////    poke(c.io.wRamPort.req.bits.writeData.end.coord.x, state.whiteRoutes(i).end._1.X)
////    poke(c.io.wRamPort.req.bits.writeData.end.coord.y, state.whiteRoutes(i).end._1.Y)
////    poke(c.io.wRamPort.req.bits.writeData.end.margin.margin,Margin.getVal(state.whiteRoutes(i).end._2))
////    poke(c.io.wRamPort.req.bits.writeData.length,state.whiteRoutes(i).length)
////    poke(c.io.wRamPort.req.valid, 1)
////    step(1)
////  }
////
////  poke(c.io.wRamPort.req.bits.write, 0)
////  for(i <- 0 to state.blackRoutes.length-1 ){
////    poke(c.io.bRamPort.req.bits.addr, i)
////    poke(c.io.bRamPort.req.bits.write, 1)
////    poke(c.io.bRamPort.req.bits.writeData.start.coord.x, state.blackRoutes(i).start._1.X)
////    poke(c.io.bRamPort.req.bits.writeData.start.coord.y, state.blackRoutes(i).start._1.Y)
////    poke(c.io.bRamPort.req.bits.writeData.start.margin.margin, Margin.getVal(state.blackRoutes(i).start._2))
////    poke(c.io.bRamPort.req.bits.writeData.end.coord.x, state.blackRoutes(i).end._1.X)
////    poke(c.io.bRamPort.req.bits.writeData.end.coord.y, state.blackRoutes(i).end._1.Y)
////    poke(c.io.bRamPort.req.bits.writeData.end.margin.margin,Margin.getVal(state.blackRoutes(i).end._2))
////    poke(c.io.bRamPort.req.bits.writeData.length,state.blackRoutes(i).length)
////    poke(c.io.bRamPort.req.valid, 1)
////    step(1)
////  }
////
////  poke(c.io.bRamPort.req.bits.write, 0)
////  poke(c.io.wRamPort.resp.ready, 1)
////  step(10)
////
////  poke(c.io.newMove.bits.coord.x, 3)
////  poke(c.io.newMove.bits.coord.y, -5)
////  poke(c.io.newMove.bits.tileType.tileType, 6)
////  poke
////}