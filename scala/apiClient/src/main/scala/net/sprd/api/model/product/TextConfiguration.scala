package net.sprd.api.model.product

import scala.collection._
import net.sprd.api.model._

class TextConfiguration (root :RootResource, product:Product, data: => scala.xml.Node) extends Configuration(root, product, data) with TextConfigurationInterface{
  lazy val printColorIdSet = immutable.Set.empty ++ ((svg \"text"\\"@printColorId") map (_.toString))
  lazy val printColorRGBSet = immutable.Set.empty ++ ((svg \"text"\\"@fill") map (_.toString))
}
