package net.sprd.api.model.design

import net.sprd.api._
import net.sprd.api.model._

class Design (val root: RootResource, data: => scala.xml.Node, full:Boolean) extends ListEntity(data, full) with DesignInterface {
    
    lazy val name = (fullData \ "name").text
    lazy val description = (fullData \ "description").text
    lazy val userId =   (fullData \ "user" \ "@id").text
    lazy val price = (fullData \ "price" \ "vatIncluded").text.toDouble
    lazy val unit = (fullData \ "size" \ "@unit").text
    lazy val size = 
      ((data \ "size"\"width").text.toDouble, (data \ "size"\"height").text.toDouble)
    lazy val printTypeIds = List.empty ++ (fullData \ "printTypes" \ "printType" \ "@id") map (a => a.text)
    lazy val minScale = (fullData \ "restrictions" \ "minimumScale").text.toDouble
    
    lazy val colors = Map.empty ++ ((fullData \ "colors" \ "color") map (a=>((a\"origin").text,(a\"default").text)))
    
}
