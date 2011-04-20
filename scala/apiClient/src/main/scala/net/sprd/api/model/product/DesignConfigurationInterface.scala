package net.sprd.api.model.product

import net.sprd.api._
import net.sprd.api.model._
import scala.collection._

trait DesignConfigurationInterface extends ConfigurationInterface {
    def designId :String
    def printColorIds:Array[String]
    def printColorRGBs: Array[String]

    lazy val design = root.designs(designId)

    override def price = super.price + design.price

}
