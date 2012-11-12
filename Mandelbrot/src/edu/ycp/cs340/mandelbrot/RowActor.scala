package edu.ycp.cs340.mandelbrot

import scala.actors.Actor

class RowActor(resultsCollector : ResultCollector) extends Actor {
  def act() = {
    loop {
      react {
        case row : Row => {
          println("Compute row " + row.row)
          resultsCollector ! (row, row.compute())
          exit()
        }
      }
    }
  }
}