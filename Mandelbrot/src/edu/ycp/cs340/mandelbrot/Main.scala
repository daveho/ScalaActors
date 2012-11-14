package edu.ycp.cs340.mandelbrot

object Main {
  val ROWS = 600
  val COLS = 600
  
  val x1 = -1.286667
  val y1 = -0.413333
  val x2 = -1.066667
  val y2 = -0.193333
  
  def main(args: Array[String]) {
    val m = new Mandelbrot(x1, y1, x2, y2, COLS, ROWS)
    m.start()
    val f = m !! Get
    val untypedResults = f()
    untypedResults match {
      case results : List[(Int, List[Int])] => {
//        results.foreach( pair => {
//          println(pair._1 + ": " + pair._2)
//        })
        val renderer = new ImageRenderer
        renderer.render(results, COLS, ROWS, "out.png")
      }
    }
  }
}