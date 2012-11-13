package edu.ycp.cs340.mandelbrot
import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.OutputChannel

case object Get

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
          Mandelbrot.y1 + i * ((Mandelbrot.y2 - Mandelbrot.y1) / Mandelbrot.ROWS),
          i,
          Mandelbrot.COLS)
      val rowActor = new RowActor(resultsCollector)
      rowActor.start()
      rowActor ! row
    }
    
    val futureResults = m !! Get
    
    println("Forcing future...")
    val results = futureResults()
    println("Future is ready?")
    
    results match {
      case results : List[(Int, List[Int])] => {
        results.foreach( pair => {
          println(pair._1 + ": " + pair._2)
        })
      }  
    }
  }
}

class Mandelbrot extends Actor {
  var results : List[(Int, List[Int])] = null
  var actorWaitingForResults : OutputChannel[Any] = null
  
  def act() = {
    loop {
      react {
    	case unsortedResults : List[(Int, List[Int])] => {
    	  results = unsortedResults.sortWith( (l, r) => l._1 < r._1 )
    	  if (actorWaitingForResults != null) {
    	    println("Sending results to waiting actor")
    	    actorWaitingForResults ! results
    	  }
    	}
    	case Get => {
    	  if (results != null) {
    	    sender ! results
    	  } else {
    	    println("Get received early?")
    	    actorWaitingForResults = sender
    	  }
    	}
      }
    }
  }
}