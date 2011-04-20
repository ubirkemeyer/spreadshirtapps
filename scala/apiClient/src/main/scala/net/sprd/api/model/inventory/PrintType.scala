package net.sprd.api.model.inventory

import net.sprd.api._
import net.sprd.api.model._
import java.awt.geom.Rectangle2D
import scala.collection._

class PrintType(root :RootResource, data: => scala.xml.Node, full:Boolean) extends ListEntity(data, full)  {
	lazy val name = (fullData \ "name").text
  lazy val description = (fullData \ "description").text
  lazy val price = (fullData \ "price" \ "vatIncluded").text.toDouble
  lazy val currencyId = (data \"price"\"currency"\"@id").text
  lazy val size = ((data \ "size"\"width").text.toDouble, (data \ "size"\"height").text.toDouble)
  lazy val dpi = (fullData \ "dpi").text.toDouble
  
  lazy val printColors = immutable.Map.empty ++ ((fullData \ "colors"\ "color") map (a => (a\"@id").text -> new PrintColor(root,a)))
  lazy val scaleability = (fullData \ "restrictions" \ "scaleability").text

  lazy val formattedPrice = ApiClient.currencies(currencyId).format(price)
    
}

object PrintType {
  val ENLARGEABLE = "enlargeable"
  val SHRINKABLE = "shrinkable"
}


