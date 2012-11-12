package edu.ycp.cs340.mandelbrot

object Row {
  val THRESHOLD = 1000
}

class Row(val xMin : Double, val xDiff : Double, val y : Double, val row : Int, val numPoints : Int) {
	def compute() : List[Int] = {
	  (0 until numPoints).map( i => {
	    val C = new Complex(xMin + i*xDiff, y)
	    var Z = new Complex(0.0, 0.0)
	    var count = 0
	    while (Z.magnitude() < 2.0 && count < Row.THRESHOLD) {
	      Z = Z*Z + C
	      count = count + 1
	    }
	    count
	  }).toList
	}
}