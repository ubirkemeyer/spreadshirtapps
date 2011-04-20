package net.sprd.api.model.product

import java.awt.geom.Point2D
import net.sprd.api._
import net.sprd.api.model._

abstract class Configuration (val root :RootResource, val product:Product, data: => scala.xml.Node) extends ReadOnlyEntity(data) with ConfigurationInterface {
    lazy val configurationType = (data \ "@type").text
    lazy val offset = ((data \ "offset" \"x").text.toDouble, (data \ "offset" \"y").text.toDouble)
    lazy val changeable = (data \ "restrictions"\ "changeable").text.toBoolean
    lazy val imageUrl = data \ "resources" \ "resource"\"@{http://www.w3.org/1999/xlink}href"
    lazy val svg = data \ "content" \ "svg"
    lazy val viewBox = (svg \"@viewBox").text.split(Array(',',' '))
    lazy val printAreaId = (data \ "printArea"\ "@id").text
    lazy val printTypeId = (data \ "printType"\ "@id").text
    lazy val svgString = svg.toString
}

object Configuration{
    def apply(root :RootResource, product:Product, data: => scala.xml.Node) = 
        if ((data \ "@type").text==ConfigurationInterface.text) new TextConfiguration(root, product, data) else new DesignConfiguration(root, product, data)
}
