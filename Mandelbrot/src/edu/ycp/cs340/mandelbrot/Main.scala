package edu.ycp.cs340.mandelbrot

object Main {
  val ROWS = 10
  val COLS = 10
  
  val x1 = -2.0
  val y1 = -2.0
  val x2 = 2.0
  val y2 = 2.0
  
  def main(args: Array[String]) {
    val m = new Mandelbrot(x1, y1, x2, y2, COLS, ROWS)
    m.start()
    val f = m !! Get
    val untypedResults = f()
    untypedResults match {
      case results : List[(Int, List[Int])] => {
        results.foreach( pair => {
          println(pair._1 + ": " + pair._2)
        })
      }
    }
  }
}