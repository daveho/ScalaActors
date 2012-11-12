package edu.ycp.cs340.mandelbrot
import scala.actors.Actor

object Mandelbrot {
  val ROWS = 40
  val COLS = 40
  
  val x1 = -2.0
  val y1 = -2.0
  val x2 = 2.0
  val y2 = 2.0
  
  def main(args: Array[String]) {
    val m = new Mandelbrot()
    m.start()
    
    val resultsCollector = new ResultCollector(Mandelbrot.ROWS, m)
    resultsCollector.start()

    for (i <- 0 until Mandelbrot.ROWS) {
      val row = new Row(
          Mandelbrot.x1,
          (Mandelbrot.x2 - Mandelbrot.x1) / Mandelbrot.COLS,
          Mandelbrot.y1 + (Mandelbrot.y2 - Mandelbrot.y1) / Mandelbrot.ROWS,
          i,
          Mandelbrot.COLS)
      val rowActor = new RowActor(resultsCollector)
      rowActor.start()
      rowActor ! row
    }
  }
}

class Mandelbrot extends Actor {
  def act() = {
    loop {
      react {
    	case results : List[List[Int]] => {
    	  results.foreach( rowList => println(rowList) )
    	  exit()
    	}
      }
    }
  }
}