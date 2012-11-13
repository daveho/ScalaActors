package edu.ycp.cs340.mandelbrot
import scala.actors.Actor
import scala.actors.OutputChannel

case object Get

class ResultCollector(numRows : Int) extends Actor {
  var results : List[(Int, List[Int])] = List()
  var rowsRecevied : Int = 0
  var actorWaitingForResults : OutputChannel[Any] = null
  
  def act() = {
    loop {
      react {
        case (row : Row, rowResult : List[Int]) => {
          results = (row.row, rowResult) :: results
          rowsRecevied += 1
          if (rowsRecevied == numRows) {
            println("Received all rows?")

            // Sort results
            results = results.sortWith( (a, b) => a._1 < b._1 )
            
            // Send results to the actor waiting for them
            finish()
          }
        }
        
        case Get => {
          // Keep track of the sender: it is the actor that will receive the final results
          actorWaitingForResults = sender
          if (rowsRecevied == numRows) {
            finish()
          }
        }
      }
    }
  }
  
  def finish() = {
    // Is there an actor waiting for the results?
    if (actorWaitingForResults != null) {
      actorWaitingForResults ! results
      exit()
    } 
  }
}