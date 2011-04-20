package net.sprd.api.model.inventory

import net.sprd.api._
import java.awt.geom.Point2D

class ViewMap(productType :ProductType, data: => scala.xml.Node) extends ReadOnlyEntity(data)  {
  
  lazy val offset = ((data \ "offset" \"x").text.toDouble, (data \ "offset" \"y").text.toDouble)
  lazy val printAreaId = (data \ "printArea"\"@id").text
  lazy val printArea = productType.printAreas(printAreaId)
  
}
