package net.sprd.api.model.product

import net.sprd.api._
import net.sprd.api.model._

class TextConfigurationUpdate (root :RootResource, product:ProductUpdate,
    offset :(Double,Double),
    svg :scala.xml.NodeSeq,
    printAreaId: String,
    printTypeId:String,
    printColorIdSet:Set[String],
    printColorRGBSet:Set[String],
    id : String = ""
  ) extends ConfigurationUpdate(root, product, ConfigurationInterface.text , offset, svg, printAreaId,
  printTypeId, printColorIdSet, printColorRGBSet, id) with TextConfigurationInterface {

  def this(config:TextConfigurationInterface, product:ProductUpdate) {
    this(config.root, product, config.offset,
         config.svg, config.printAreaId, config.printTypeId,
         config.printColorIdSet, config.printColorRGBSet,
         config.id)
  }

  def textTag() = {
    (svg \ "text")(0)
  }

  def tspanTags() = {
    textTag() \ "tspan"
  }
 
}
