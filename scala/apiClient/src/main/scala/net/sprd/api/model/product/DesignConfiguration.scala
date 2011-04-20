package net.sprd.api.model.product

import scala.collection._
import net.sprd.api.model._

class DesignConfiguration (root :RootResource, product:Product, data: => scala.xml.Node) extends Configuration(root, product, data) with DesignConfigurationInterface{
    lazy val designId = (data \ "designs"\ "design"\ "@id").text
    lazy val printColorIds = (svg \"image"\"@printColorIds").text.split(Array(',',' ')) filter (!_.isEmpty())
    lazy val printColorRGBs = (svg \"image"\"@printColorRGBs").text.split(Array(',',' ')) filter (!_.isEmpty())
    lazy val size = ((svg \"image"\"@width").text.toDouble,(svg \"image"\"@height").text.toDouble)
    lazy val transform = (svg \"image"\"@transform").text

    lazy val printColorIdSet = immutable.Set.empty ++ printColorIds
    lazy val printColorRGBSet = immutable.Set.empty ++ printColorRGBs

}
