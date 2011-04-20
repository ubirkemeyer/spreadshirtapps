package net.sprd.api.model.inventory
import net.sprd.api._
import scala.collection._
import java.awt.geom.Rectangle2D


class View(val productType : ProductType, data: => scala.xml.Node) extends ReadOnlyEntity(data)  {
	lazy val name = (data \ "name").text
    lazy val size = 
      ((data \ "size"\"width").text.toDouble, (data \ "size"\"height").text.toDouble)
    lazy val perspective = (data \ "perspective").text
    
    lazy val viewMaps = immutable.Map.empty ++ 
            ((data \ "viewMaps"\ "viewMap") map 
                (a => (a\"printArea"\"@id").text -> new ViewMap(productType,a)))
  
}
