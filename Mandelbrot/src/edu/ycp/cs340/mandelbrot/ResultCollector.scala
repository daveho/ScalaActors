package edu.ycp.cs340.mandelbrot
import scala.actors.Actor

case class Done

class ResultCollector(numRows : Int, whenFinished : Actor) extends Actor {
  var results : List[List[Int]] = List()
  var rowsRecevied : Int = 0
  
  def act() = {
    loop {
      react {
        case (row : Row, rowResult : List[Int]) => {
          results = rowResult :: results
          rowsRecevied = rowsRecevied + 1
          if (rowsRecevied == numRows) {
            println("Received all rows?")
            whenFinished ! results
            exit()
          }
        }
      }
    }
  }
}