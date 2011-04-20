


package net.sprd.api.model.product

import net.sprd.api._
import net.sprd.api.model._

trait ConfigurationInterface extends Entity {
    def root :RootResource
    def product:ProductInterface
    def configurationType: String
    def offset :(Double,Double)
    def svg :scala.xml.NodeSeq
    def printAreaId: String
    def printTypeId:String

    lazy val printType = root.printTypes(printTypeId)
    lazy val printArea = product.productType.printAreas(printAreaId)

    def printColorIdSet:Set[String]
    def printColorRGBSet:Set[String]
    def price = printColorIdSet.foldLeft(printType.price)(_+printType.printColors(_).price)

}

object ConfigurationInterface{
  val text = "text"
  val design = "design"
}
