package edu.ycp.cs340.mandelbrot

import scala.actors.Actor

class RowActor extends Actor {
  def act() = {
    loop {
      react {
        case row : Row => {
          println("Compute row " + row.rowNum)
          sender ! (row.rowNum, row.compute())
          exit()
        }
      }
    }
  }
}