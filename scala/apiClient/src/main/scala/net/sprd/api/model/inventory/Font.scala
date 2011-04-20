package net.sprd.api.model.inventory


import net.sprd.api._
import net.sprd.api.model._

class Font(val root :RootResource, val fontFamily: FontFamily, data: => scala.xml.Node) extends ReadOnlyEntity(data)  {

  lazy val name = (data \ "name").text

  lazy val weight = (data \ "weight").text
  lazy val style = (data \ "style").text
  lazy val isBold = ("bold" == weight)
  lazy val isItalic = ("italic" == style)

  lazy val minimalSize = (data \ "minimalSize").text.toDouble
    
}
