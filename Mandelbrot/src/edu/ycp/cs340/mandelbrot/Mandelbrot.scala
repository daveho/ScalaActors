package edu.ycp.cs340.mandelbrot
import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.OutputChannel

case object Get

class Mandelbrot(x1 : Double, y1 : Double, x2 : Double, y2 : Double, nCols : Int, nRows : Int) extends Actor {
  var resultSink : OutputChannel[Any] = null
  var partialResults : List[(Int, List[Int])] = List()
  var rowsReceived : Int = 0
  
  def act() = {
    loop {
      react {
        case Get => {
          // Make a note of which actor requested the results of the computation
          resultSink = sender
          
          // Start RowActors to compute each row
          for (j <- 0 until nRows) {
            val xDiff = (x2 - x1) / nCols
            val yDiff = (y2 - y1) / nRows
            val row = new Row(x1, xDiff, y1 + j*yDiff, j, nCols)
            val rowActor = new RowActor()
            rowActor.start()
            rowActor ! row
          }
        }
        
        case r : (Int, List[Int]) => {
          println("Receive result for row " + r._1)
          // A RowActor is sending us completed results for a row
          partialResults = r :: partialResults
          rowsReceived += 1
          
          // Are all rows finished?
          if (rowsReceived == nRows) {
            // Sort rows by row number (since they probably arrived out of order)
            val results = partialResults.sortWith( (l, r) => l._1 < r._1 )
            
            // Send sorted results to the actor that requested them
            resultSink ! results
            
            // Computation is done
            exit()
          }
        }
      }
    }
  }
}
