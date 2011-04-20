package net.sprd.api.creator

import net.sprd.api.model.product._
import scala.xml._

object TextConfigurationCreator {
  
  def updateText(config: TextConfiguration) = {
    val svg = config.svg;
    val text = (svg \ "text").asInstanceOf[Elem]
    val tspans = text \ "tspan"
    val oneLine = if (tspans.size <= 0) true else false
    
  }

}
