package edu.ycp.cs340.mandelbrot
import java.awt.image.BufferedImage
import java.awt.Color
import javax.imageio.ImageIO
import java.io.FileOutputStream

class ImageRenderer {
  def render(results : List[(Int, List[Int])], nCols : Int, nRows : Int, fileName : String) = {
    val img = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_ARGB)
    val g = img.getGraphics()
    results.foreach( pair => {
      pair._2.zipWithIndex.foreach {
        case (count, i) => {
          val color =
            if (count < 1000) {
              val intensity = ((Math.log(count) / Math.log(1000)) * 255).toInt
              new Color(0, intensity, 255-intensity)
            } else
              Color.BLACK
          g.setColor(color)
          g.fillRect(i, pair._1, 1, 1)
        }
      }
    })
    val os = new FileOutputStream(fileName)
    ImageIO.write(img, "PNG", os)
    os.close()
  }
}